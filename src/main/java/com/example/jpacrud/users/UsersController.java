package com.example.jpacrud.users;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {
    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public User createUser(@RequestBody NewUser user) {
        return userRepository.save(new User(0, user.firstName(), user.lastName()));
    }

    @PutMapping("{id}")
    public User updateUser(@PathVariable int id,
                           @RequestBody UpdatedUser updatedUser) {
        return userRepository.findById(id)
                .map(existing -> userRepository.save(new User(id, updatedUser.firstName(), updatedUser.lastName())))
                .orElseThrow();
    }

    @DeleteMapping("{id}")
    public User deleteUser(@PathVariable int id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);
        return user;
    }
}
