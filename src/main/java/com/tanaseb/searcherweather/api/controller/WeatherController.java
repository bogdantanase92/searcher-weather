package com.tanaseb.searcherweather.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tanaseb.searcherweather.api.model.WeatherDto;
import com.tanaseb.searcherweather.application.service.WeatherService;

@RestController
public class WeatherController {

	private final WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@GetMapping("/weather")
	public ResponseEntity<WeatherDto> get(@RequestParam String city) {
		return ResponseEntity.ok(weatherService.get(city));
	}
}
