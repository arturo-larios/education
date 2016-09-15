package com.nvisium.moneyx.service;

import java.math.BigDecimal;
import java.util.List;

import com.nvisium.moneyx.model.Event;
import com.nvisium.moneyx.model.EventMembership;
import com.nvisium.moneyx.model.User;

public interface EventService {

	public Long addEvent(String name, BigDecimal amount);

	public void updateEvent(Long owner, String name, BigDecimal amount, Boolean ispublic, Long id);

	public void addEventMembership(Long event, Long user, BigDecimal amount);

	public void deleteEvent(Long id);

	public void deleteEventMembership(Long eventId, Long userId);
	
	public boolean isUserMember(Long eventId, Long userId);

	public List<Event> getEventsByOwner(Long owner);
	
	public List<Event> getPublicEvents();

	public List<EventMembership> getEventsByMembership(Long userId);
	
	public List<EventMembership> getMembershipsByEvent(Long eventId);
	
	public List<User> getUsersbyEventMembership(Long eventId);
	
	public Event getEventById(Long id);
	
	public List<Event> searchEventsByOwner(Long owner, String q);
	
//	public List<Event> searchMembershipsByOwner(Long owner, String q);
	
}
