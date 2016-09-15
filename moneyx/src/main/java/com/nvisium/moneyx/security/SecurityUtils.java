package com.nvisium.moneyx.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nvisium.moneyx.model.NvUserDetails;
import com.nvisium.moneyx.service.UserService;

@Component
public class SecurityUtils {
	
	@Autowired
	UserService userService;

	public NvUserDetails getSecurityContext() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NvUserDetails nvUserDetails = null;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			nvUserDetails = (NvUserDetails) userService.loadUserByUsername(auth.getName());
		}
		
		return nvUserDetails;
	}

	public long getCurrentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return getSecurityContext().getUser().getId();
		} else {
			return 0;
		}
	}
}
