package com.github.appreciated.flow_cms.controller;

import com.github.appreciated.flow_cms.entity.User;
    import com.github.appreciated.flow_cms.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/api/users")
    public class UserController {
        @Autowired
        private UserService userService;

        @GetMapping
        public List<User> getAllUsers() {
            return userService.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<User> getUserById(@PathVariable Long id) {
            Optional<User> user = userService.findById(id);
            return user.map(ResponseEntity::ok).orElseGet(() ->
  ResponseEntity.notFound().build());
        }

        @PostMapping
        public User createUser(@RequestBody User user) {
            return userService.save(user);
        }

        @PutMapping("/{id}")
        public ResponseEntity<User> updateUser(@PathVariable Long id,
  @RequestBody User userDetails) {
            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                userDetails.setId(id);
                return ResponseEntity.ok(userService.save(userDetails));
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
