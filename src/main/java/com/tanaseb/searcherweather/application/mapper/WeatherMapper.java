package com.tanaseb.searcherweather.application.mapper;

import com.tanaseb.searcherweather.api.model.WeatherDto;
import com.tanaseb.searcherweather.domain.model.WeatherEntity;
import com.tanaseb.searcherweather.infrastructure.sqs.model.WeatherCommand;

public class WeatherMapper {

	public static WeatherDto toWeatherDto(WeatherEntity entity) {
		return new WeatherDto(entity.getCity(), entity.getCountry(), entity.getTemperature());
	}

	public static WeatherCommand toWeatherCommand(WeatherEntity entity, String type) {
		return new WeatherCommand(entity.getCity(), entity.getCountry(), entity.getTemperature(), entity.getTimestamp(), type);
	}
}
