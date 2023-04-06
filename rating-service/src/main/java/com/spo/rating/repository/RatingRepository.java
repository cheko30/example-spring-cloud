package com.spo.rating.repository;

import com.spo.rating.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, Long> {
    List<Rating> findByUserId(String userId);
    List<Rating> findByHotelId(String hotelId);
}
