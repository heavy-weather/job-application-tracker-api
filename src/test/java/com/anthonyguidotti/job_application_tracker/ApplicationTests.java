package com.anthonyguidotti.job_application_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

//@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void test() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
		ZonedDateTime nowUtc = now.atZone(ZoneOffset.UTC);
		System.out.println(nowUtc.getHour() + ":" + nowUtc.getMinute() + ":" + nowUtc.getSecond());
		nowUtc = ZonedDateTime.of(now, ZoneOffset.UTC);
		System.out.println(nowUtc.getHour() + ":" + nowUtc.getMinute() + ":" + nowUtc.getSecond());
		nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
		System.out.println(nowUtc.getHour() + ":" + nowUtc.getMinute() + ":" + nowUtc.getSecond());


		System.out.println(nowUtc.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
		System.out.println(nowUtc.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
	}

}
