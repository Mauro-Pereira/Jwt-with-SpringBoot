package com.example.jwt.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.config.JwtUtil;
import com.example.jwt.dto.AuthenticationRequest;
import com.example.jwt.dto.AuthenticationResponse;
import com.example.jwt.dto.UserMapper;
import com.example.jwt.dto.UserResponse;
import com.example.jwt.dto.UserResquest;
import com.example.jwt.entity.User;
import com.example.jwt.service.CustomUserDetailsService;
import com.example.jwt.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    
    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserResquest userRequest) {
        return new ResponseEntity<>(userService.saveUser(UserMapper.userRequestToUser(userRequest)), HttpStatus.OK); 
    }

    @GetMapping("/me")
    public Optional<User> getUserDetails(Authentication authentication) {
        return userService.findUserByEmail(authentication.getName());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/me/updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserResquest userResquest) {
        User updateUser = this.userService.updateUser(id, UserMapper.userRequestToUser(userResquest));
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/me/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return new ResponseEntity<>("Deleted with successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/listAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> userResponseList = this.userService
                                            .findAllUsers()
                                            .stream()
                                            .map(UserMapper::userToUserResponse)
                                            .collect(Collectors.toList()
                                            );

        return new ResponseEntity<>(userResponseList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/user/setAdminUser/{id}")
    public ResponseEntity<String> setAdminUser(@PathVariable Long id){
        return new ResponseEntity<>(this.userService.setAdminUser(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/user/removeAdminUser/{id}")
    public ResponseEntity<String> removeAdminUser(@PathVariable Long id){
        return new ResponseEntity<>(this.userService.removeAdminUser(id), HttpStatus.OK);
    }
}