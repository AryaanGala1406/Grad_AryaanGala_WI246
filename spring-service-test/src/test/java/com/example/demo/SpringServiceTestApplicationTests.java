package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class SpringRestJpaApplicationTests {

	@Autowired
	MockMvc mockmvc;

	@Test
	void testEmpExists() throws Exception {
		int empid = 1;
		MvcResult result = mockmvc.perform(get("/employee/" + empid)).andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(200, status, "Product not found — test failed");
	}

	@Test
	void testEmpNotExists() throws Exception {
		int empid = 11;
		MvcResult result = mockmvc.perform(get("/employee/" + empid)).andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(204, status, "Product not found — test failed");
	}

}