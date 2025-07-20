package com.example.authservice.contoller;

import com.example.authservice.model.LoginDto;
import com.example.authservice.model.LoginResponse;
import com.example.authservice.model.User;
import com.example.authservice.model.UserDto;
import com.example.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required=false)MultipartFile profilePhoto
    ) throws IOException {
        UserDto registerUserDto=new UserDto(name,email,password,profilePhoto);
        User registeredUser=userService.register(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/signIn")
    public ResponseEntity<LoginResponse> signin(@RequestBody LoginDto loginDto){
        var loginResponse=userService.authenticate(loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/test")
    public String test(){
        return "Hello from auth-service";
    }

}
