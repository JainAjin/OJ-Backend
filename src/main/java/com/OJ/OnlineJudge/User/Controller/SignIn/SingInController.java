package com.OJ.OnlineJudge.User.Controller.SignIn;

import com.OJ.OnlineJudge.User.Entity.User;
import com.OJ.OnlineJudge.User.Model.UserLogin;
import com.OJ.OnlineJudge.User.Service.SignInService;
import com.OJ.OnlineJudge.User.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class SingInController {

    @Autowired
    private SignInService service;

    @PostMapping("/signIn")
    public void userSignIn(@RequestBody User user){
        service.signInService(user);
    }

    @PostMapping("/login")
    public AuthResponse userLogin(@RequestBody UserLogin user){
        return service.loginService(user);
    }

    @GetMapping("/users")
    public List<User> getalluser(){
        return service.gatAllUsers();
    }

    @GetMapping("/user/{id}")
    public User finduser(@PathVariable Long id) {
        return service.findUser(id);
    }

}
