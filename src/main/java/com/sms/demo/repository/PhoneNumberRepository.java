package com.sms.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sms.demo.entity.PhoneNumber;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {
	
	
	@Query("select phone from PhoneNumber phone where phone.number like :number and phone.account.id =:accountId ")
	public Optional<PhoneNumber> phoneNumberByAccountIdAndPhoneNumber(String number, Integer accountId);

}
