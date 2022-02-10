package com.sms.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.demo.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	
	
	
	public Optional<Account> findByUsernameIgnoreCase(String userName);

}
