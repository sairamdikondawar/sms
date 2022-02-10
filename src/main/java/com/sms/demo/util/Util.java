package com.sms.demo.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.sms.demo.model.UserDetailsImpl;

public class Util {

	public static Integer getLoggedInUserId() {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getId();
	}

}
