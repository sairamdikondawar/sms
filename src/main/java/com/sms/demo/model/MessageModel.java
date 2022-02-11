package com.sms.demo.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3601660114926850953L;

//	@NotNull(message = "from is missing"  )
//	@NotBlank(message = "from is missing")
//	@Size(min = 6, max = 16, message = "from is invalid")
	private String from;

//	@NotNull(message = "to is missing")
//	@NotBlank(message = "to is missing")
//	@Size(min = 6, max = 16, message = "to is invalid")
	private String to;

//	@NotNull(message = "text is missing")
//	@NotBlank(message = "text is missing")
//	@Size(min = 1, message = "text is invalid")
	private String text;

}
