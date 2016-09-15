package com.nvisium.moneyx.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

import com.nvisium.moneyx.MoneyxLogger;
import com.nvisium.moneyx.exception.AlreadyFriendsException;
import com.nvisium.moneyx.exception.FriendRequestAlreadySentException;
import com.nvisium.moneyx.exception.InvalidFriendException;
import com.nvisium.moneyx.exception.InvalidFriendRequestException;
import com.nvisium.moneyx.model.Friend;
import com.nvisium.moneyx.model.User;
import com.nvisium.moneyx.security.SecurityUtils;
import com.nvisium.moneyx.service.FriendService;
import com.nvisium.moneyx.service.UserService;

@RequestMapping(value = "/friend")
@Controller
public class FriendController {

	@Autowired
	SecurityUtils security;

	@Autowired
	FriendService friendService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/delete-friend", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteFriend(@RequestParam(value = "friend", required = false) long friend, Model model) {

		if (friend == 0) {
			return getFriends(model);
		}

		friendService.deleteFriend(userService.loadUserById(friend));
		model.addAttribute("success", "Friend was successfully removed");
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/friend/delete-friend:"+friend);
		return getFriends(model);
	}


	@RequestMapping(value = "/get-friends", method = RequestMethod.GET)
	public String getFriends(Model model) {

		User currentUser = security.getSecurityContext().getUser();
		List<User> friends = new ArrayList<User>();

		for (Friend f : friendService.getFriends()) {
			if (currentUser.getId().equals(f.getUser1().getId())) {
				friends.add(f.getUser2());
			} else {
				friends.add(f.getUser1());
			}
		}

		model.addAttribute("friends", friends);
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/friend/get-friends");
		return "friend/get-friends";
	}


	@RequestMapping(value = "/list-sent-friend-requests", method = RequestMethod.GET)
	public String listSentFriendRequests(Model model) {
		model.addAttribute("friendrequests", friendService.getSentFriendRequests());
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/friend/list-sent-friend-requests");
		return "friend/sent-requests";
	}


	@RequestMapping(value = "/list-received-friend-requests", method = RequestMethod.GET)
	public String listReceivedFriendRequests(Model model) {
		model.addAttribute("friendrequests", friendService.getReceivedFriendRequests());
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/friend/list-received-friend-requests");
		return "friend/received-requests";
	}


	@RequestMapping(value = "/send-friend-request/{receiver}", method = RequestMethod.GET)
	public String send(@PathVariable Long receiver, Model model) {
		try {
			friendService.sendFriendRequest(userService.loadUserById(receiver));
			model.addAttribute("success", "User has been sent a friend request!");
		} catch (FriendRequestAlreadySentException e) {
			model.addAttribute("error", "User has already been sent a friend request!");
		} catch (InvalidFriendRequestException e) {
			model.addAttribute("error", "Cannot send friend request!");
		}
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/friend/send-friend-request/"+receiver);
		return listSentFriendRequests(model);
	}


	@RequestMapping(value = "/accept-friend-request/{id}", method = RequestMethod.GET)
	public String accept(@PathVariable Long id, Model model) {
		User sender = friendService.getFriendRequestSenderId(id);
		try {
			friendService.addFriend(sender);
			friendService.deleteFriendRequest(id);
			model.addAttribute("success", "Successfully accepted friend request!");
		} catch (AlreadyFriendsException e) {
			model.addAttribute("error", "User is already your friend!");
		} catch (InvalidFriendException e) {
			model.addAttribute("error", "Cannot accept friend request!");
		}
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/friend/accept-friend-request/"+id);
		return getFriends(model);
	}


	@RequestMapping(value = "/delete-friend-request/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, Model model) {
		friendService.deleteFriendRequest(id);
		model.addAttribute("success", "Deleted the friend request.");
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/friend/delete-friend-request/"+id);
		return getFriends(model);
	}

}
