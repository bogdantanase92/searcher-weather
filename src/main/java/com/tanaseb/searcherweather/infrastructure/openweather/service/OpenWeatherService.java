package com.tanaseb.searcherweather.infrastructure.openweather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tanaseb.searcherweather.infrastructure.openweather.model.OpenWeather;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpenWeatherService {

	public static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&APPID={appid}";

	private final RestTemplate restTemplate;

	public OpenWeatherService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Value("${open.weather.key}")
	private String openWeatherKey;

	public OpenWeather get(String city) {
		String url = WEATHER_API_URL.replace("{city}", city).replace("{appid}", openWeatherKey);
		return restTemplate.getForEntity(url, OpenWeather.class).getBody();
	}
}
