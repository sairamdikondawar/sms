package com.sms.demo.validators;

import org.hibernate.mapping.Array;
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
		Object[] objArr = { "from" };
		if (messageModel.getFrom() == null || messageModel.getFrom().isEmpty()) {

			errors.rejectValue("from", "sms.param.missing", objArr, "from is missing");
		} else {

			if (messageModel.getFrom().length() < 6 || messageModel.getFrom().length() > 16) {

				errors.rejectValue("from", "sms.param.invalid", objArr, "from is invalid");
			}
		}
		Object[] objToArr = { "to" };
		if (messageModel.getTo() == null || messageModel.getTo().isEmpty()) {
			errors.rejectValue("to", "sms.param.missing", objToArr, "to is missing");
		} else {

			if (messageModel.getTo().length() < 6 || messageModel.getTo().length() > 16) {
				errors.rejectValue("to", "sms.param.invalid", objToArr, "to is invalid");
			}
		}

		Object[] objTextArr = { "text" };
		if (messageModel.getText() == null || messageModel.getText().isEmpty()) {
			errors.rejectValue("text", "sms.param.missing", objTextArr, "text is missing");
		} else {

			if (messageModel.getText().length() < 1) {
				errors.rejectValue("text", "sms.param.invalid", objTextArr, "text is invalid");
			}
		}

	}

}
