package com.microservice.authManager.ServiceImpl;

import com.microservice.authManager.Dto.response.HttpResponse;
import com.microservice.authManager.Entity.PasswordResetToken;
import com.microservice.authManager.Entity.User;
import com.microservice.authManager.Message.CustomMessage;
import com.microservice.authManager.Repository.PasswordResetTokenRepository;
import com.microservice.authManager.Repository.UserRepository;
import com.microservice.authManager.Service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;


    @Override
    public String getPasswordResetPage(String token, Model model) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null)
        {
            model.addAttribute("error","Could not find reset token");
        }
        else if(passwordResetToken.getExpiryDate() == LocalDateTime.now())
        {
            model.addAttribute("error","Reset Token is expired");
        }
        else
        {
            model.addAttribute("token",passwordResetToken.getToken());
        }
        return "resetpassword";
    }

    @Override
    public HttpResponse<?> sentEmailReset(String useremail, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = userRepository.findByEmail(useremail.replaceAll("\"", ""));
        if (user == null){
            return HttpResponse.<PasswordResetToken>builder()
                    // .data(Collections.singleton(appKeyRepository.save(appKeyGen1)))
                    .message(CustomMessage.EMAIL_INTROUVABLE)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
        }else{
            PasswordResetToken token = new PasswordResetToken();
            token.setToken(UUID.randomUUID().toString());
            token.setUser(user);
            token.setExpiryDate(LocalDateTime.now().plusHours(2));
            passwordResetTokenRepository.save(token);
            String url =
                    request.getScheme() + "://" + request.getServerName() + ":3000"+ "/resetpassword?token=" + token.getToken();
            //emailSenderService.ConfirmEmail(useremail,url);
            map.put("message", CustomMessage.EMAIL_RESET_SENT);
            map.put("code", HttpStatus.OK);
            return HttpResponse.<PasswordResetToken>builder()
                    // .data(Collections.singleton(appKeyRepository.save(appKeyGen1)))
                    .message(CustomMessage.EMAIL_RESET_SENT)
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }

    @Override
    public HttpResponse<?> handlePasswordReset(HttpServletRequest request, Model model, String updatedPassword, String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null){
            return HttpResponse.<PasswordResetToken>builder()
                    // .data(Collections.singleton(appKeyRepository.save(appKeyGen1)))
                    .message(CustomMessage.TOKEN_INTROUVABLE)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
        }else{
            User user = passwordResetToken.getUser();
            String url =
                    request.getScheme() + "://" + request.getServerName() + ":3000"+ "/login";
            user.setPassword(encoder.encode(updatedPassword));
            userRepository.save(user);
            passwordResetTokenRepository.delete(passwordResetToken);
            //emailSenderService.ConfirmResetPassword(user.getEmail(), url,user.getName());
            return HttpResponse.<PasswordResetToken>builder()
                    // .data(Collections.singleton(appKeyRepository.save(appKeyGen1)))
                    .message(CustomMessage.SUCCESS_RESET)
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
