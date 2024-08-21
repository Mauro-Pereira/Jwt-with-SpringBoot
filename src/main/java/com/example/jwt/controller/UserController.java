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
import com.example.jwt.dto.UserRequest;
import com.example.jwt.entity.User;
import com.example.jwt.service.CustomUserDetailsService;
import com.example.jwt.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "JWT", description = "JWT Spring Boot API")
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

    
    @Operation(
        summary = "Autentica Usuário",
        description = "Ao entrar com um email e senha, o usuário será autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation")
    })
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

    @Operation(
            summary = "Registra um Usuário",
            description = "Ao entrar com o Nome, Email e senha o usuário será devidamente cadastrado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.saveUser(UserMapper.userRequestToUser(userRequest)), HttpStatus.OK); 
    }


    @Operation(
            summary = "Mostra detalhes de um usuário logado",
            description = "Desde que o usuário esteja logado, ele poderá ver detalhes de seu cadastro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/me")
    public Optional<User> getUserDetails(Authentication authentication) {
        return userService.findUserByEmail(authentication.getName());
    }

    @Operation(
            summary = "Atualiza Usuário",
            description = "Desde que o usuário esteja logado, ele poderá realizar uma atualização de seu cadastro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/me/updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest userResquest) {
        User updateUser = this.userService.updateUser(id, UserMapper.userRequestToUser(userResquest));
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @Operation(
            summary = "Deleta Usuário",
            description = "Por meio deste endpoint, o usuário poderá feitar o apagameto de seu cadastro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/me/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return new ResponseEntity<>("Deleted with successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Retorna Usuários",
            description = "Desde que o usuário logado seja um admin, ele poderá ver todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
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

    @Operation(
            summary = "Atribui a um usuário comum o título de admin",
            description = "Desde que o usuário esteja logado e seja um admin, ele pode fazer de outro usuário admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/user/setAdminUser/{id}")
    public ResponseEntity<String> setAdminUser(@PathVariable Long id){
        return new ResponseEntity<>(this.userService.setAdminUser(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Remove título de admin",
            description = "Desde que o usuário esteja logado e seja um admin, ele pode remover o título de admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/user/removeAdminUser/{id}")
    public ResponseEntity<String> removeAdminUser(@PathVariable Long id){
        return new ResponseEntity<>(this.userService.removeAdminUser(id), HttpStatus.OK);
    }
}