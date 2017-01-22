package com.epiaggregator.services.users.web;

import com.epiaggregator.services.users.model.User;
import com.epiaggregator.services.users.model.UserRepository;
import com.epiaggregator.services.users.util.PBKDF2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class UsersController {
    @Autowired
    private UserRepository userRepository;
    private PBKDF2 hasher = new PBKDF2();

    @RequestMapping(method = RequestMethod.POST, path = "/users")
    public ResponseEntity<CreateUserResponse> registerUser(@Validated @RequestBody CreateUserRequest req) {
        userRepository.save(new User(req.getEmail(), hasher.hash(req.getPassword().toCharArray())));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public ResponseEntity<GetUserResponse> getUserByEmail(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email);
        return new ResponseEntity<>(new GetUserResponse(user.getId(), user.getEmail(), user.getPassword()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable(value = "id") String userId) {
        User user = userRepository.findOne(userId);
        return new ResponseEntity<>(new GetUserResponse(user.getId(), user.getEmail(), null), HttpStatus.OK);
    }
}
