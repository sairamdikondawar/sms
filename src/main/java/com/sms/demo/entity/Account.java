package com.sms.demo.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String authId;
	private String username;
	private List<PhoneNumber> phoneNumbers;

	public Account() {
	}


	@Id
	@SequenceGenerator(name="ACCOUNT_ID_GENERATOR", sequenceName="ACCOUNT_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCOUNT_ID_GENERATOR" )
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name="auth_id")
	public String getAuthId() {
		return this.authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}


	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	//bi-directional many-to-one association to PhoneNumber
	@OneToMany(mappedBy="account")
	public List<PhoneNumber> getPhoneNumbers() {
		return this.phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public PhoneNumber addPhoneNumber(PhoneNumber phoneNumber) {
		getPhoneNumbers().add(phoneNumber);
		phoneNumber.setAccount(this);

		return phoneNumber;
	}

	public PhoneNumber removePhoneNumber(PhoneNumber phoneNumber) {
		getPhoneNumbers().remove(phoneNumber);
		phoneNumber.setAccount(null);

		return phoneNumber;
	}

}