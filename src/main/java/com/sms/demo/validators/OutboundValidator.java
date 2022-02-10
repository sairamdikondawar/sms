package com.sms.demo.validators;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sms.demo.entity.PhoneNumber;
import com.sms.demo.model.MessageModel;
import com.sms.demo.repository.PhoneNumberRepository;
import com.sms.demo.util.Util;

@Component
public class OutboundValidator extends MessageValidator implements Validator {

	@Autowired
	private PhoneNumberRepository phoneNumberRepository;

	public void validate(Object target, Errors errors) {

		MessageModel messageModel = (MessageModel) target;

		super.validate(target, errors);

		if (!errors.hasErrors()) {
			

		}

	}

}
