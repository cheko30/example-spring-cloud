package com.spo.user.controllers;

import com.spo.user.entities.User;
import com.spo.user.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> saveUSer(@RequestBody User userRequest) {
        User user = userService.saveUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    int quantityRetrying = 1;

    @GetMapping("/{userId}")
    //@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        log.info("List only user: UserController");
        log.info("Quantity retrying {}", quantityRetrying);
        quantityRetrying++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    public ResponseEntity<User> ratingHotelFallback(String userId, Exception exception) {
        log.info("Tha backup is run because the service is upo", exception.getMessage());
        User user = User.builder()
                .email("root@gmail.com")
                .name("root")
                .information("This service is run when a service fail")
                .userId("1234")
                .build();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
