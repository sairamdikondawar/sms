package com.sms.demo.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.sms.demo.model.UserDetailsImpl;

public class Util {

	public static Integer getLoggedInUserId() {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getId();
	}

	public static long expirationTimeInMillies(Integer hourse) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, hourse);
		cal.getTime().getTime();
		long time = cal.getTime().getTime();
		return time;
	}
	
	public static void main(String[] args) {
		String test="STOP\n\r Hello";
		test=test.replace("\r", "");
		test=test.replace("\n", "");
		List<String> words=Arrays.asList(test.toLowerCase().split(" "));
		System.out.println(words.contains("stop"));
		
	}

}
