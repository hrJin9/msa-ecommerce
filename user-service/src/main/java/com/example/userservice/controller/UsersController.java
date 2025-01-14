package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;
    private final Environment env;
    private final ModelMapper mapper;

    @GetMapping("/health_check")
    public String health_check() {
        return String.format("It's working in user service" +
                        ", port(local.server.port)=" + env.getProperty("local.server.port"),
                        ", port(server.port)=" + env.getProperty("server.port"),
                        ", token secret=" + env.getProperty("token.secret"),
                        ", token expiration time=" + env.getProperty("token.expiration_time")
        );
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody @Valid RequestUser user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto responseUserDto = userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(responseUserDto, ResponseUser.class);
        return ResponseEntity.created(null)
                .body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        List<UserDto> userList = userService.getUserByAll();

        List<ResponseUser> result = userList.stream()
                .map(o -> mapper.map(o, ResponseUser.class))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {
        UserDto user = userService.getUserByUserId(userId);

        ResponseUser returnValue = mapper.map(user, ResponseUser.class);

        return ResponseEntity.ok(returnValue);
    }
}
