package com.sms.demo.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.map.IMap;
import com.sms.demo.model.MessageModel;
import com.sms.demo.model.ResponseModel;
import com.sms.demo.service.SmsService;
import com.sms.demo.validators.InboundValidator;
import com.sms.demo.validators.OutboundValidator;

@RestController
public class SmsController {

	private static final Logger log = LoggerFactory.getLogger(SmsController.class);

	@Autowired
	private InboundValidator inboundValidator;

	@Autowired
	private OutboundValidator outboundValidator;;

	@Autowired
	private SmsService smsService;

	@Autowired
	private IMap<String, String> stopMap;

	@Autowired
	private MessageSource messageSource;

	Locale currentLocale = LocaleContextHolder.getLocale();

	private Object[] argumentsToReplace = new String[5];

	@GetMapping("/sms")
	public String test() {
		return "service running";
	}

	@PostMapping("/inbound/sms")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> inbound(@Valid @RequestBody MessageModel model, BindingResult bindingResult)
			throws Exception {
		log.debug("Inside   Inbound SMS API");

		ResponseModel resp = null;
		try {

			String localizedErrorMessage = messageSource.getMessage("sms.unkown.failuer", null, currentLocale);
			resp = new ResponseModel("", localizedErrorMessage);

			inboundValidator.validate(model, bindingResult);

			if (bindingResult.hasErrors()) {
				for (FieldError fieldError : bindingResult.getFieldErrors()) {
					argumentsToReplace = fieldError.getArguments();
					localizedErrorMessage = messageSource.getMessage(fieldError.getCode(), argumentsToReplace,
							currentLocale);

					resp = new ResponseModel("", localizedErrorMessage);

					break;// as per requirement need to send only one error so just handling in this way.
							// we can handle diffrent way to show multiple errors.
				}

				return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
			} else {
				resp = smsService.procesInboundMessage(model);
			}

		} catch (Exception e) {
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
		}

		log.debug("Exist from Inbound SMS API");

		return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
	}

	@PostMapping("/outbound/sms")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> outbound(@Valid @RequestBody MessageModel model, BindingResult bindingResult)
			throws Exception {
		log.info("Inside   Outbound SMS API" + model.getText());
		ResponseModel resp = null;
		try {

			String localizedErrorMessage = messageSource.getMessage("sms.unkown.failuer", null, currentLocale);
			resp = new ResponseModel("", localizedErrorMessage);

			outboundValidator.validate(model, bindingResult);

			if (bindingResult.hasErrors()) {
				for (FieldError fieldError : bindingResult.getFieldErrors()) {
					argumentsToReplace = fieldError.getArguments();
					localizedErrorMessage = messageSource.getMessage(fieldError.getCode(), argumentsToReplace,
							currentLocale);

					resp = new ResponseModel("", localizedErrorMessage);
				}
				return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
			} else {
				resp = smsService.procesOutboundMessage(model);
			}
		} catch (Exception e) {
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
		}

		log.debug("Exist from Outbound SMS API");

		return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
	}

}
