package com.microservice.authManager.Controller;

import com.microservice.authManager.Dto.request.LoginDTO;
import com.microservice.authManager.Dto.request.NewPasswordReq;
import com.microservice.authManager.Dto.response.JwtResponse;
import com.microservice.authManager.Exception.EntitiesNotFoundException;
import com.microservice.authManager.Security.JwtBlacklistConfig;
import com.microservice.authManager.Security.jwt.JwtUtils;
import com.microservice.authManager.Service.PasswordResetTokenService;
import com.microservice.authManager.ServiceImpl.AuthServiceimpl;
import com.microservice.authManager.ServiceImpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private AuthServiceimpl authServiceimpl;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private JwtBlacklistConfig jwtBlacklistConfig;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) throws IOException {
        String keyapp = request.getHeader("keyapp");
        return ResponseEntity.ok().body(authServiceimpl.saveUserAndRole(keyapp,loginDTO));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws IOException {
        return ResponseEntity.ok().body(authServiceimpl.logout(request));
    }


    @PostMapping("/password-reset-request")
    public ResponseEntity<?> sentEmailResetPassword(@RequestBody String useremail, HttpServletRequest request) throws IOException {
        return ResponseEntity.ok().body(passwordResetTokenService.sentEmailReset(useremail,request));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> setnewPassword(HttpServletRequest request, @RequestBody NewPasswordReq newPasswordReq) throws IOException {
        return ResponseEntity.ok().body(passwordResetTokenService.handlePasswordReset(request,newPasswordReq));
    }

}
