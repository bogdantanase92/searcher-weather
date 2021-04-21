package com.tanaseb.searcherweather.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
	private String city;
	private String country;
	private Double temperature;
}
