package com.nvisium.moneyx.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nvisium.moneyx.MoneyxLogger;
import com.nvisium.moneyx.model.Event;
import com.nvisium.moneyx.model.EventMembership;
import com.nvisium.moneyx.model.Friend;
import com.nvisium.moneyx.model.User;
import com.nvisium.moneyx.security.SecurityUtils;
import com.nvisium.moneyx.service.EventService;
import com.nvisium.moneyx.service.FriendService;
import com.nvisium.moneyx.service.UserService;

@RequestMapping(value = "/event")
@Controller
public class EventController {

	@Autowired
	EventService eventService;

	@Autowired
	UserService userService;

	@Autowired
	FriendService friendService;

	@Autowired
	SecurityUtils security;

	@RequestMapping(value = "/list-owner/{user}", method = RequestMethod.GET)
	public String listEventsOwned(@PathVariable("user") Long user, Model model) {

		java.util.List<Event> events = eventService.getEventsByOwner(user);
		if (events.size() == 0) {
			model.addAttribute("info", "User does not own any events!");
		}

		model.addAttribute("events", events);
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/list-owner/"+user);
		return "event/list-owned";
	}

	@RequestMapping(value = "/list-member/{user}", method = RequestMethod.GET)
	public String listEventMembership(@PathVariable("user") Long user, Model model) {
		List<EventMembership> events_m = eventService.getEventsByMembership(user);
		List<Event> events = new ArrayList<Event>();
		Map<Long, User> users = new HashMap<Long, User>();
		Map<Long, EventMembership> memberships = new HashMap<Long, EventMembership>();

		if (events_m.size() == 0) {
			model.addAttribute("info", "User is not part of any events!");
		} else {
			for (EventMembership m : events_m) {
				Event e = eventService.getEventById(m.getEventId());
				events.add(e);
				users.put(m.getEventId(), userService.loadUserById(e.getOwner()));
				memberships.put(m.getEventId(), m);
			}
			model.addAttribute("events", events);
			model.addAttribute("users", users);
			model.addAttribute("memberships", memberships);
		}
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/list-member/"+user);
		return "event/list-member";
	}


	/*
	 * Add a new event
	 * 
	 * 
	 * yungjassy.pyc
	 */
	@RequestMapping(value = "/add", method = { RequestMethod.GET, RequestMethod.POST })
	public String add(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "amount", required = false) BigDecimal amount,
			@RequestParam(value = "users", required = false) LinkedList<Long> users, RedirectAttributes redirectAttrs,
			Model model) {

		/* If we need anything, let's just take the user to the add page */
		if (name == null || amount == null) {

			User currentUser = security.getSecurityContext().getUser();
			java.util.List<User> friends = new java.util.ArrayList<User>();

			for (Friend f : friendService.getFriends()) {
				if (currentUser.getId().equals(f.getUser1().getId())) {
					friends.add(f.getUser2());
				} else {
					friends.add(f.getUser1());
				}
			}

			model.addAttribute("users", friends);
			MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/add:"+amount+":"+users);
			return "event/add-event";
		}

		Long eventId = eventService.addEvent(name, amount);

		if (users != null && users.size() > 0) {
			for (Long tempUserId : users) {
				eventService.addEventMembership(eventId, tempUserId,
						amount.divide(new BigDecimal(users.size()), BigDecimal.ROUND_UP));
			}
		}

		redirectAttrs.addFlashAttribute("success", "Successfully created event");
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/add:"+amount+":"+users);
		return "redirect:/event/list-owner/" + security.getCurrentUserId();
	}


	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id) {
		eventService.deleteEvent(id);
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/list-owner/"+id);
		return "redirect:/event/list-owner/" + security.getCurrentUserId();
	}


	@RequestMapping(value = "/add-member", method = { RequestMethod.GET, RequestMethod.POST })
	public String addMember(@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "amount", required = false) BigDecimal amount, RedirectAttributes redirectAttrs,
			Model model) {

		/* If we need anything, let's just take the user to the add page */
		if (eventId == null || userId == null || amount == null) {
			model.addAttribute("users", userService.getPublicUsers());
			model.addAttribute("events", eventService.getPublicEvents());
			MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/add-member");
			return "event/add-member";
		}

		if (eventService.isUserMember(eventId, userId)) {
			model.addAttribute("info", "User is already a member of that event");
			MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/add-member:"+userId+":"+amount);
			return "event/add-member";
		}

		eventService.addEventMembership(eventId, userId, amount);
		redirectAttrs.addFlashAttribute("success", "Successfully added user to event!");
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/add-member:"+userId+":"+amount);
		return "redirect:/event/list-member/" + userId;
	}


	@RequestMapping(value = "/delete-member/{eventId}/{userId}", method = RequestMethod.GET)
	public String removeMember(@PathVariable Long eventId, @PathVariable Long userId) {
		eventService.deleteEventMembership(eventId, userId);
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/delete-member/"+eventId+"/"+userId);
		return "event/event-member-deleted";
	}


	@RequestMapping(value = "/update/{id}/{owner}/{name}/{amount}/{hidden}", method = RequestMethod.GET)
	public String updateEvent(@PathVariable Long id, @PathVariable Long owner, @PathVariable String name,
			@PathVariable BigDecimal amount, @PathVariable Boolean hidden) {
		eventService.updateEvent(owner, name, amount, hidden, id);
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/update/"+id+"/"+owner+"/"+name+"/"+amount+"/"+hidden);
		return "event/event-updated";
	}

	
	@RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
	public String eventSearch(@RequestParam(value = "q", required = false) String q,
			RedirectAttributes redirectAttrs,
			Model model) {
		if (q != null) {
			String sq = q.toLowerCase();
			List<Event> events = eventService.searchEventsByOwner(security.getCurrentUserId(), q);
			model.addAttribute("my_events",events);
			model.addAttribute("q",q);
			
			List<EventMembership> events_m = eventService.getEventsByMembership(security.getCurrentUserId());
			Map<EventMembership,Event> m_events = new HashMap<EventMembership, Event>();
			Map<Long, User> m_users = new HashMap<Long, User>();
			List<EventMembership> memberships = new ArrayList<EventMembership>();

			if (events_m.size() > 0) {
				for (EventMembership m : events_m) {
					Event e = eventService.getEventById(m.getEventId());
					User o = userService.loadUserById(e.getOwner());
					if ((e.getName().toLowerCase().contains(sq) || 
							o.getUsername().toLowerCase().contains(sq) ||
							o.getFirstname().toLowerCase().contains(sq) ||
							o.getLastname().toLowerCase().contains(sq)) 
							&& security.getCurrentUserId() != o.getId()) {
						m_events.put(m,e);
						m_users.put(m.getId(), o);
						memberships.add(m);
					}

				}
				model.addAttribute("events", events);
				model.addAttribute("m_events", m_events);
				model.addAttribute("m_users", m_users);
				model.addAttribute("memberships", memberships);
			}
		}
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/event/search:"+q);
		return "event/search";
	}
}
