package com.nvisium.moneyx.service;

import java.util.List;

import com.nvisium.moneyx.exception.AlreadyFriendsException;
import com.nvisium.moneyx.exception.FriendRequestAlreadySentException;
import com.nvisium.moneyx.exception.InvalidFriendException;
import com.nvisium.moneyx.exception.InvalidFriendRequestException;
import com.nvisium.moneyx.model.Friend;
import com.nvisium.moneyx.model.FriendRequest;
import com.nvisium.moneyx.model.User;

public interface FriendService {

	public void addFriend(User sender) throws AlreadyFriendsException, InvalidFriendException;

	public void deleteFriend(User id);

	public List<Friend> getFriends();

	public List<FriendRequest> getSentFriendRequests();

	public List<FriendRequest> getReceivedFriendRequests();

	public void sendFriendRequest(User to)
			throws FriendRequestAlreadySentException, InvalidFriendRequestException;

	public void deleteFriendRequest(Long id);

	public User getFriendRequestSenderId(Long id);
}
