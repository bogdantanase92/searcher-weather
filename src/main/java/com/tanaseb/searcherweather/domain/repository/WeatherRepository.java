package com.tanaseb.searcherweather.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tanaseb.searcherweather.domain.model.WeatherEntity;

@Repository
public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {

	Optional<WeatherEntity> findByCity(String city);
}
