package com.sms.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.demo.model.MessageModel;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

@SpringBootTest(classes = SmsdemoApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class SmsdemoApplicationTests {

	@LocalServerPort
	private int port;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	@Order(1)
	public void testInBoundMessage() {

		MessageModel model = new MessageModel();
		model.setTo("4924195509195");
		model.setFrom("4924195509198");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/inbound/sms");

		r.then().assertThat().statusCode(200);

	}

	@Test
	@Order(2)
	public void testOutBoundMessage() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setFrom("4924195509195");
		model.setTo("4924195509198");
		model.setText("Hellow How are U");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(200);
		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("sms from 4924195509195 to 4924195509198 blocked by STOP request",
				actualObj.get("error").textValue());
		System.out.println(actualObj);

	}

	@Test
	@Order(3)
	public void testInBoundMessageWithToMissing() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("");
		model.setFrom("4924195509198");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/inbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("to is missing", actualObj.get("error").textValue());

	}

	@Test
	@Order(4)
	public void testInBoundMessageWithFromMising() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509198");
		model.setFrom("");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/inbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("from is missing", actualObj.get("error").textValue());

	}

	@Test
	@Order(5)
	public void testInBoundMessageWithTextMising() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509198");
		model.setFrom("4924195509195");
		model.setText("");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/inbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("text is missing", actualObj.get("error").textValue());

	}

	@Test
	@Order(6)
	public void testInBoundMessageWithToInvalid() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924");
		model.setFrom("4924195509195");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/inbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("to is invalid", actualObj.get("error").textValue());

	}

	@Test
	@Order(7)
	public void testInBoundMessageWithFromInvalid() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setFrom("4924");
		model.setTo("4924195509195");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/inbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("from is invalid", actualObj.get("error").textValue());

	}

	@Test
	@Order(8)
	public void testInBoundMessageWithToNotFound() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setFrom("4924195509195");
		model.setTo("49241955091951");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/inbound/sms");

		r.then().assertThat().statusCode(200);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("to parameter not found", actualObj.get("error").textValue());
	}

	@Test
	@Order(9)
	public void testOutBoundMessageWithToMissing() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("");
		model.setFrom("4924195509198");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("to is missing", actualObj.get("error").textValue());

	}

	@Test
	@Order(10)
	public void testOutBoundMessageWithFromMising() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509198");
		model.setFrom("");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("from is missing", actualObj.get("error").textValue());

	}

	@Test
	@Order(11)
	public void testOutBoundMessageWithTextMising() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509198");
		model.setFrom("4924195509195");
		model.setText("");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("text is missing", actualObj.get("error").textValue());

	}

	@Test
	@Order(12)
	public void testOutBoundMessageWithToInvalid() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924");
		model.setFrom("4924195509195");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("to is invalid", actualObj.get("error").textValue());

	}

	@Test
	@Order(13)
	public void testOutBoundMessageWithFromInvalid() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setFrom("4924");
		model.setTo("4924195509195");
		model.setText("STOP");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(400);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("from is invalid", actualObj.get("error").textValue());

	}

	@Test
	@Order(14)
	public void testOutBoundMessageWithToNotFound() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509195");
		model.setFrom("49241955091951");
		model.setText("Hello How are u doing");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(200);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("from parameter not found", actualObj.get("error").textValue());
	}

	@Order(14)
	@Test
	public void testOutBoundMessageWithSuccess() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509195");
		model.setFrom("4924195509049");
		model.setText("Hello How are u doing");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(200);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("outbound sms ok", actualObj.get("message").textValue());
	}

	@Order(15)
	@Test
	@RepeatedTest(49)
	public void testOutBoundMessageWithLimitMessage() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509195");
		model.setFrom("4924195509049");
		model.setText("Hello How are u doing");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(200);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("outbound sms ok", actualObj.get("message").textValue());
	}

	@Test
	@Order(16)
	public void testOutBoundMessageWithLimitMessageSuccss() throws JsonMappingException, JsonProcessingException {

		MessageModel model = new MessageModel();
		model.setTo("4924195509195");
		model.setFrom("4924195509049");
		model.setText("Hello How are u doing");

		String adminuserCredentials = "azr1:20S0KPNOIM";
		String encodedCredentials = new String(Base64.encodeBase64(adminuserCredentials.getBytes()));

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.addHeader("Authorization", "Basic " + encodedCredentials).build();
		Response r = RestAssured.given().contentType("application/json").body(model).when()
				.post("http://localhost:" + port + "/outbound/sms");

		r.then().assertThat().statusCode(200);

		String responseModelObject = r.body().asString();

		JsonNode actualObj = mapper.readTree(responseModelObject.toString());
		assertEquals("limit reached for from " + model.getFrom(), actualObj.get("error").textValue());
	}

}
