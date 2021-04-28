package com.tanaseb.searcherweather.application.service;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tanaseb.searcherweather.SearcherWeatherApplication;
import com.tanaseb.searcherweather.api.model.WeatherDto;
import com.tanaseb.searcherweather.application.util.DateGenerator;
import com.tanaseb.searcherweather.domain.model.WeatherEntity;
import com.tanaseb.searcherweather.domain.repository.WeatherRepository;
import com.tanaseb.searcherweather.infrastructure.openweather.model.Main;
import com.tanaseb.searcherweather.infrastructure.openweather.model.OpenWeather;
import com.tanaseb.searcherweather.infrastructure.openweather.model.Sys;
import com.tanaseb.searcherweather.infrastructure.openweather.service.OpenWeatherService;
import com.tanaseb.searcherweather.infrastructure.sqs.service.SqsSenderService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SearcherWeatherApplication.class})
class WeatherServiceTest {

	public static final String CITY = "London";
	public static final String COUNTRY = "UK";
	public static final double TEMPERATURE_1 = 70d;
	public static final double TEMPERATURE_2 = 80d;

	@Autowired
	private WeatherService weatherService;
	@Autowired
	private WeatherRepository repository;
	@MockBean
	private OpenWeatherService openWeatherService;
	@MockBean
	private DateGenerator dateGenerator;
	@MockBean
	private SqsSenderService sqsSenderService;

	@BeforeEach
	public void setUp() {
		repository.deleteAll();
	}

	@Test
	void findCity() {
		repository.save(new WeatherEntity(1, CITY, COUNTRY, TEMPERATURE_1, new Date()));
		WeatherDto actual = weatherService.get(CITY);

		Assertions.assertEquals(CITY, actual.getCity());
		Assertions.assertEquals(COUNTRY, actual.getCountry());
		Assertions.assertEquals(TEMPERATURE_1, actual.getTemperature());
	}

	@Test
	void updateCity() {
		int threeMinutes = 180;
		Instant instant = new Date().toInstant().minusSeconds(threeMinutes);
		Date timestamp = new Date(instant.toEpochMilli());
		repository.save(new WeatherEntity(1, CITY, COUNTRY, TEMPERATURE_1, timestamp));

		OpenWeather openWeather = buildOpenWeather(TEMPERATURE_2);
		Mockito.when(openWeatherService.get(CITY)).thenReturn(openWeather);
		Mockito.when(dateGenerator.now()).thenReturn(new Date());
		WeatherDto actual = weatherService.get(CITY);

		Assertions.assertEquals(CITY, actual.getCity());
		Assertions.assertEquals(COUNTRY, actual.getCountry());
		Assertions.assertEquals(TEMPERATURE_2, actual.getTemperature());
	}

	@Test
	void createCity() {
		OpenWeather openWeather = buildOpenWeather(TEMPERATURE_1);
		Mockito.when(openWeatherService.get(CITY)).thenReturn(openWeather);
		Mockito.when(dateGenerator.now()).thenReturn(new Date());
		WeatherDto actual = weatherService.get(CITY);

		Assertions.assertEquals(CITY, actual.getCity());
		Assertions.assertEquals(COUNTRY, actual.getCountry());
		Assertions.assertEquals(TEMPERATURE_1, actual.getTemperature());
	}

	private OpenWeather buildOpenWeather(Double temperature) {
		OpenWeather openWeather = new OpenWeather();
		Sys sys = new Sys();
		sys.setCountry(COUNTRY);
		Main main = new Main();
		main.setTemp(temperature);

		openWeather.setName(CITY);
		openWeather.setSys(sys);
		openWeather.setMain(main);

		return openWeather;
	}
}