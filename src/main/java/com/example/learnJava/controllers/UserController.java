package com.example.learnJava.controllers;

import com.example.learnJava.domain.User;
import com.example.learnJava.domain.response.ResPageDTO;
import com.example.learnJava.domain.response.User.ResCreateUserDTO;
import com.example.learnJava.service.UserService;
import com.example.learnJava.utils.annotation.ApiMessage;
import com.example.learnJava.utils.error.IdInvalidException;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

   private final UserService userService;
   private final PasswordEncoder passwordEncoder;

   public UserController(UserService userService, PasswordEncoder passWordEncoder) {
      this.userService = userService;
      this.passwordEncoder = passWordEncoder;
   }

   // @GetMapping("user/create")
   @PostMapping(value = "users/create", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ApiMessage("create user success")
   public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User data) throws IdInvalidException {
      Boolean check = this.userService.handleExistsByEmail(data.getEmail());
      if(check){
         throw new IdInvalidException("Email "+ data.getEmail()+ " da ton tai");
      }

      String HashPassword = this.passwordEncoder.encode(data.getPassword());

      data.setPassword(HashPassword);
      User newUser = this.userService.handleCreateUser(data);
      ResCreateUserDTO DTOUser = this.userService.handlCreateUserDTO(newUser);
      return ResponseEntity.status(HttpStatus.CREATED).body(DTOUser);
   }

   @DeleteMapping("users/{id}")
   public ResponseEntity<String> deleteUser(@PathVariable Long id)
         throws IdInvalidException {
      if (id > 1500) {
         throw new IdInvalidException("Id k vuoi qua  1500");
      }
      this.userService.handleDeleteUser(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted successfully");
   }

   @GetMapping("users/{id}")
   public ResponseEntity<ResCreateUserDTO> getUserById(@PathVariable long id) throws IdInvalidException {
      if (id > 1500) {
         throw new IdInvalidException("Id k vuoi qua  1500");
      }
      User user = this.userService.handleGetUserById(id);
      
      ResCreateUserDTO newUser = this.userService.handlCreateUserDTO(user);
      return ResponseEntity.status(HttpStatus.OK).body(newUser);
   }

   @GetMapping("users")
   @ApiMessage("get all user success")
   public ResponseEntity<ResPageDTO> getAllUsers(
         @RequestParam("current") Optional<String> currentOptional,
         @RequestParam("pageSize") Optional<String> pageSizeOptional) {
      
      String current = currentOptional.isPresent() ? currentOptional.get() : "";
      String pageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
      
      int currentInt = current.isEmpty() ? 0 : Integer.parseInt(current);
      int pageSizeInt = pageSize.isEmpty() ? 10 : Integer.parseInt(pageSize);
      Pageable pageable = PageRequest.of(currentInt - 1, pageSizeInt);
      return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUser(pageable));
   }

   @PutMapping("/users")
   public ResponseEntity<ResCreateUserDTO> updateUser(@RequestBody User data) {
      User User = this.userService.handleUpdateUser(data);
      ResCreateUserDTO  newUser = this.userService.handleUpdateUserDTO(User);
      return ResponseEntity.status(HttpStatus.OK).body(newUser);
   }

}
