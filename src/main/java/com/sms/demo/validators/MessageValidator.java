package com.sms.demo.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sms.demo.model.MessageModel;

public class MessageValidator {

	public boolean supports(Class<?> clazz) {
		return MessageModel.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {

		MessageModel messageModel = (MessageModel) target;

		if (messageModel.getFrom() == null || messageModel.getFrom().isEmpty()) {
			errors.rejectValue("from", "from.empty", null, "from is missing");
		} else {

			if (messageModel.getFrom().length() < 6 || messageModel.getFrom().length() > 16) {
				errors.rejectValue("from", "from.min.max.length", null, "from is invalid");
			}
		}

		if (messageModel.getTo() == null || messageModel.getTo().isEmpty()) {
			errors.rejectValue("to", "to.empty", null, "to is missing");
		} else {

			if (messageModel.getTo().length() < 6 || messageModel.getTo().length() > 16) {
				errors.rejectValue("to", "to.min.max.length", null, "to is invalid");
			}
		}

		if (messageModel.getText() == null || messageModel.getText().isEmpty()) {
			errors.rejectValue("text", "text.empty", null, "text is missing");
		} else {

			if (messageModel.getText().length() < 1) {
				errors.rejectValue("text", "text.min.max.length", null, "text is invalid");
			}
		}

	}

}
