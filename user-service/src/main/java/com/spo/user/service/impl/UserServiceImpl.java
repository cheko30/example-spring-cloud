package com.spo.user.service.impl;

import com.spo.user.entities.Hotel;
import com.spo.user.entities.Rating;
import com.spo.user.entities.User;
import com.spo.user.exceptions.ResourceNotFoundException;
import com.spo.user.external.services.HotelService;
import com.spo.user.repository.UserRepository;
import com.spo.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Override
    public User saveUser(User user) {
        String randomUSer = UUID.randomUUID().toString();
        user.setUserId(randomUSer);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        String urlUser = "http://RATING-SERVICE/ratings/users/".concat(user.getUserId());

        Rating[] ratingsUser = restTemplate.getForObject(urlUser, Rating[].class);
        List<Rating> ratings = Arrays.stream(ratingsUser).collect(Collectors.toList());
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/".concat(rating.getHotelId()), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            //logger.info("Response status: {}", forEntity.getStatusCode());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }
}
