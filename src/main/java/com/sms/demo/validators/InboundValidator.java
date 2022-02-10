package com.sms.demo.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class InboundValidator  extends MessageValidator implements Validator {

}
