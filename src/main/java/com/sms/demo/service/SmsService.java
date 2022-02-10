package com.sms.demo.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.map.IMap;
import com.sms.demo.entity.PhoneNumber;
import com.sms.demo.model.MessageModel;
import com.sms.demo.model.ResponseModel;
import com.sms.demo.repository.AccountRepository;
import com.sms.demo.repository.PhoneNumberRepository;
import com.sms.demo.util.Util;

@Service
public class SmsService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PhoneNumberRepository phoneNumberRepository;

	@Autowired
	private IMap<String, String> stopMap;

	@Autowired
	private IMap<String, Integer> dailyLimitMap;

	public ResponseModel procesInboundMessage(MessageModel messageModel) {
		ResponseModel res = null;
		try {

			Integer userName = Util.getLoggedInUserId();

			Optional<PhoneNumber> phoneNumber = phoneNumberRepository
					.phoneNumberByAccountIdAndPhoneNumber(messageModel.getTo(), userName);

			if (!phoneNumber.isPresent()) {
				res = new ResponseModel("", "to parameter not found");
			} else {

				if (messageModel.getText().trim().equals("STOP")) {
					stopMap.put(messageModel.getFrom() + messageModel.getTo(), messageModel.getTo(), 1,
							TimeUnit.MINUTES);
				}

				res = new ResponseModel("", "inbund sms ok");
			}

		} catch (Exception e) {
			res = new ResponseModel("", "unknown failure");
		}
		return res;
	}

	public ResponseModel procesOutboundMessage(MessageModel messageModel) {
		ResponseModel res = null;
		try {
			Integer userName = Util.getLoggedInUserId();

			Optional<PhoneNumber> phoneNumber = phoneNumberRepository
					.phoneNumberByAccountIdAndPhoneNumber(messageModel.getFrom(), userName);

			if (!phoneNumber.isPresent()) {
				res = new ResponseModel("", "from parameter not found");
			} else {

				if (stopMap.get(messageModel.getTo() + messageModel.getFrom()) != null) {
					res = new ResponseModel("", "sms from " + messageModel.getFrom() + " to " + messageModel.getTo()
							+ " blocked by STOP request");
				}

				else if (dailyLimitMap.get(messageModel.getFrom())!=null && dailyLimitMap.get(messageModel.getFrom()) >= 3) {
					res = new ResponseModel("", "limit reached for from " + messageModel.getFrom());
				} else {

					dailyLimitMap.put(messageModel.getFrom(), (dailyLimitMap.get(messageModel.getFrom()) == null ? 1
							: (dailyLimitMap.get(messageModel.getFrom()) + 1)), 1, TimeUnit.MINUTES);
					res = new ResponseModel("outbound sms ok","");
				}
			}

		} catch (Exception e) {
			res = new ResponseModel("", "unknown failure");
		}

		return res;

	}

}
