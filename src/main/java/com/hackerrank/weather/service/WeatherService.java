package com.hackerrank.weather.service;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    static {
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private final WeatherRepository repository;

    public List<Weather> getSortedAndFilteredWeather(
            @Nullable String sort,
            @Nullable String date,
            @Nullable String city
    ) throws ParseException {
        List<String> cities = parseCities(city);
        if (StringUtils.isEmpty(sort)) {
            if (!cities.isEmpty()) {
                return repository.findWeathersByCityIgnoreCaseIn(cities);
            } else if (!StringUtils.isEmpty(date)) {
                return repository.findWeathersByDate(format.parse(date));
            } else {
                return repository.findAll();
            }
        } else if (sort.startsWith("-")) {
            return repository.findWeathersByOrderByDateDesc();
        } else {
            return repository.findWeathersByOrderByDateAsc();
        }
    }

    public Optional<Weather> getWeatherById(@NonNull Integer id) {
        return repository.findById(id);
    }

    private List<String> parseCities(@Nullable String cities) {
        List<String> citiesList= new ArrayList<>();
        if (!StringUtils.isEmpty(cities)) {
            citiesList = Arrays.stream(cities.split(",")).collect(Collectors.toList());
        }
        return citiesList;
    }

    public Weather addNewWeatherRecord(Weather weather) {
        return repository.save(weather);
    }
}
