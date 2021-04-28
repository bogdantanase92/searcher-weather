package com.tanaseb.searcherweather.application.util;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateGenerator {

	public Date now() {
		return new Date();
	}
}
