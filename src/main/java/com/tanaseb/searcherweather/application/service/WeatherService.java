package com.tanaseb.searcherweather.application.service;

import static com.tanaseb.searcherweather.application.mapper.WeatherMapper.toWeatherCommand;
import static com.tanaseb.searcherweather.application.mapper.WeatherMapper.toWeatherDto;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanaseb.searcherweather.api.model.WeatherDto;
import com.tanaseb.searcherweather.application.mapper.WeatherMapper;
import com.tanaseb.searcherweather.application.util.DateGenerator;
import com.tanaseb.searcherweather.domain.model.WeatherEntity;
import com.tanaseb.searcherweather.domain.repository.WeatherRepository;
import com.tanaseb.searcherweather.infrastructure.openweather.model.OpenWeather;
import com.tanaseb.searcherweather.infrastructure.openweather.service.OpenWeatherService;
import com.tanaseb.searcherweather.infrastructure.sqs.model.WeatherCommand;
import com.tanaseb.searcherweather.infrastructure.sqs.service.SqsSenderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WeatherService {

	public static final String CREATED = "created";
	public static final String UPDATED = "updated";

	@Value("${open.weather.ttl}")
	private int weatherTtlMinutes;

	private final WeatherRepository weatherRepository;
	private final OpenWeatherService openWeatherService;
	private final SqsSenderService sqsSenderService;
	private final DateGenerator dateGenerator;
	private final ObjectMapper mapper;

	public WeatherService(WeatherRepository weatherRepository, OpenWeatherService openWeatherService,
	                      SqsSenderService sqsSenderService, DateGenerator dateGenerator, ObjectMapper mapper) {
		this.weatherRepository = weatherRepository;
		this.openWeatherService = openWeatherService;
		this.sqsSenderService = sqsSenderService;
		this.dateGenerator = dateGenerator;
		this.mapper = mapper;
	}

	@Transactional
	public WeatherDto get(String city) {
		Optional<WeatherEntity> entity = findOrUpdate(city);
		return entity
				.map(WeatherMapper::toWeatherDto)
				.orElseGet(() -> {
					WeatherEntity newEntity = create(city);
					String commandSerialized = createCommand(newEntity, CREATED);
					sqsSenderService.send(commandSerialized);
					return toWeatherDto(newEntity);
				});
	}

	private Optional<WeatherEntity> findOrUpdate(String city) {
		Optional<WeatherEntity> entity = weatherRepository.findByCity(city);

		if (entity.isPresent()) {
			long entityAge = System.currentTimeMillis() - entity.get().getTimestamp().getTime();
			if (dataIsOld(entityAge, weatherTtlMinutes)) {
				WeatherEntity updatedEntity = update(entity.get(), openWeatherService.get(city));

				String commandSerialized = createCommand(updatedEntity, UPDATED);
				sqsSenderService.send(commandSerialized);
				return Optional.of(updatedEntity);
			}
			else {
				log.info("Loading city: {} from db", city);
				return entity;
			}
		}

		return Optional.empty();
	}

	private boolean dataIsOld(long entityAge, int noOfMinutes) {
		return entityAge > ((long) noOfMinutes * 60 * 1000);
	}

	private WeatherEntity update(WeatherEntity entity, OpenWeather openWeather) {
		entity.setTemperature(openWeather.getMain().getTemp());
		entity.setTimestamp(dateGenerator.now());

		log.info("Updating city: {} in db", openWeather.getName());
		return weatherRepository.save(entity);
	}

	private WeatherEntity create(String city) {
		OpenWeather openWeather = openWeatherService.get(city);

		WeatherEntity entity = new WeatherEntity();
		entity.setCity(openWeather.getName());
		entity.setCountry(openWeather.getSys().getCountry());
		entity.setTemperature(openWeather.getMain().getTemp());
		entity.setTimestamp(dateGenerator.now());

		log.info("Creating city: {} in db", openWeather.getName());
		return weatherRepository.save(entity);
	}

	private String createCommand(WeatherEntity newEntity, String type) {
		WeatherCommand command = toWeatherCommand(newEntity, type);
		String commandSerialized;
		try {
			commandSerialized = mapper.writeValueAsString(command);
		} catch (JsonProcessingException e) {
			log.error("Command could not be serialized");
			throw new RuntimeException();
		}
		return commandSerialized;
	}
}
