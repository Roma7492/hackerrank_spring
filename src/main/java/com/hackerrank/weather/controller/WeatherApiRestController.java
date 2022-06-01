package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import com.hackerrank.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherApiRestController {

    private final WeatherService service;

    @PostMapping
    public ResponseEntity<Weather> createNewWeatherRecord(@RequestBody Weather weather) {
        final Weather createdWeather = service.addNewWeatherRecord(weather);
        return new ResponseEntity<>(createdWeather, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Weather>> getWeatherList(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "city", required = false) String city
    ) throws ParseException {
        return new ResponseEntity<>(service.getSortedAndFilteredWeather(sort, date, city), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Weather> getWeather(@PathVariable String id) {
        final int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<Weather> optionalWeather = service.getWeatherById(idInt);
        return optionalWeather
                .map(weather -> new ResponseEntity<>(weather, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
