package com.sms.demo.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
	private PhoneNumberRepository phoneNumberRepository;

	@Autowired
	private IMap<String, String> stopMap;

	@Autowired
	@Qualifier("dailyLimitMap")
	private IMap<String, Integer> dailyLimitMap;

	@Autowired
	@Qualifier("expirationTimeMap")
	private IMap<String, Long> expirationTimeMap;

	@Value("${sms.stop.expire.time}")
	private Integer stopCacheTimingInHourse;

	@Value("${sms.clear.dialy.limit}")
	private Integer maxLimitClearTime;

	@Value("${sms.clear.dialy.limit.count}")
	private Integer maxLimitClearTimeCount;

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
					stopMap.put(messageModel.getFrom() + messageModel.getTo(), messageModel.getTo(),
							stopCacheTimingInHourse, TimeUnit.HOURS);
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

				else if (dailyLimitMap.get(messageModel.getFrom()) != null
						&& dailyLimitMap.get(messageModel.getFrom()) > maxLimitClearTimeCount) {
					res = new ResponseModel("", "limit reached for from " + messageModel.getFrom());
				} else {

					if (dailyLimitMap.get(messageModel.getFrom()) == null) {
						expirationTimeMap.put(messageModel.getFrom(), Util.expirationTimeInMillies(maxLimitClearTime));
						long timeToExpire = expirationTimeMap.get(messageModel.getFrom()) - System.currentTimeMillis();
						dailyLimitMap.put(messageModel.getFrom(), 1, timeToExpire, TimeUnit.MILLISECONDS);
					} else {
						long timeToExpire = expirationTimeMap.get(messageModel.getFrom()) - System.currentTimeMillis();
						dailyLimitMap.put(messageModel.getFrom(), (dailyLimitMap.get(messageModel.getFrom()) + 1),
								timeToExpire, TimeUnit.MILLISECONDS);
					}

					res = new ResponseModel("outbound sms ok", "");
				}
			}

		} catch (Exception e) {
			res = new ResponseModel("", "unknown failure");
		}

		return res;

	}

}
