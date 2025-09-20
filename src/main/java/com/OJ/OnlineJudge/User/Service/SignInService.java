package com.OJ.OnlineJudge.User.Service;

import com.OJ.OnlineJudge.Auth.JwtUtils;
import com.OJ.OnlineJudge.User.Entity.User;
import com.OJ.OnlineJudge.User.Model.UserLogin;
import com.OJ.OnlineJudge.User.Repo.UserRepo;
import com.OJ.OnlineJudge.User.dto.AuthResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class SignInService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo repo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthResponse authResponse;

    public void signInService(User user) {
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        repo.save(user);
        log.info("sign in successful");
    }
    public AuthResponse loginService(UserLogin user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        String JWT_TOKEN=jwtUtils.generateToken(user.getUsername());
        log.info("login successful"+JWT_TOKEN);
        User candidate = repo.findByUsername(user.getUsername());
        authResponse.setToken(JWT_TOKEN);
        authResponse.setUser_id(candidate.getId());
        log.info(authResponse.toString());
        return authResponse;
    }

    public List<User> gatAllUsers() {
        return repo.findAll();
    }

    public User findUser(Long id) {
        return repo.findById(id).orElse(null);
    }
}
