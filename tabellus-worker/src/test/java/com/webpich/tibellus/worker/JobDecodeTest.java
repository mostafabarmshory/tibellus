package com.webpich.tibellus.worker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class JobDecodeTest {

	@Test
	public void loadEchoTest() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new YAMLMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Job echo = objectMapper.readValue(getClass().getResourceAsStream("echo.yaml"), Job.class);

		Assert.assertEquals(echo.getVariables().size(), 2);
		Assert.assertEquals(echo.getScripts().size(), 5);
		Assert.assertNotNull(echo);
	}

	@Test
	public void stringFormat() {
		Assert.assertEquals("001", String.format("%03d", 1));
	}

	@Test
	public void stringFormatArray() {
		Assert.assertEquals("001", String.format("%03d", new Object[] { 1 }));
	}
	
	@Test
	public void dateFromat() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T00:00:00'XXX");
		String nowAsString = df.format(new Date());
		System.out.println(nowAsString);
	}
}
