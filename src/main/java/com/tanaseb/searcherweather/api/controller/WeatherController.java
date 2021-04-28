package com.tanaseb.searcherweather.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tanaseb.searcherweather.api.model.WeatherDto;
import com.tanaseb.searcherweather.application.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

	private final WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@Operation(summary = "Find weather in a city")
	@GetMapping
	public ResponseEntity<WeatherDto> get(@RequestParam String city) {
		return ResponseEntity.ok(weatherService.get(city));
	}
}
