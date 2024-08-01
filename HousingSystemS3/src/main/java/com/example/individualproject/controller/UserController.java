package com.example.individualproject.controller;

import com.example.individualproject.business.UserUseCases;
import com.example.individualproject.domain.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
private  final UserUseCases userUseCase;
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request){
        CreateUserResponse response = userUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsers() {

        return ResponseEntity.ok(userUseCase.getUsers());
    }
    @GetMapping("id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id){
         Optional<User> user = userUseCase.getUserById(id);
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable(value = "email") String email){
        Optional<User> user = userUseCase.getUserByEmail(email);
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable(value = "id") long id,
                                           @RequestBody @Valid UpdateUserRequest request){
        request.setId(id);
        userUseCase.updateUser(request);
    return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deleteUser(@PathVariable(value = "id") long id){
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
