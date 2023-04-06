package com.spo.rating.service;

import com.spo.rating.entity.Rating;

import java.util.List;

public interface RatingService {
    Rating create(Rating rating);
    List<Rating> getAllRatings();
    List<Rating> getRatingByUser(String userId);
    List<Rating> getRatingByHotel(String hotelId);
}
