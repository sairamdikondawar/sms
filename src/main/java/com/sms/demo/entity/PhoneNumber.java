package com.sms.demo.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the phone_number database table.
 * 
 */
@Entity
@Table(name="phone_number")
@NamedQuery(name="PhoneNumber.findAll", query="SELECT p FROM PhoneNumber p")
public class PhoneNumber implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String number;
	private Account account;

	public PhoneNumber() {
	}


	@Id
	@SequenceGenerator(name="PHONE_NUMBER_ID_GENERATOR", sequenceName="PHONE_NUMBER_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PHONE_NUMBER_ID_GENERATOR")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}


	//bi-directional many-to-one association to Account
	@ManyToOne
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}