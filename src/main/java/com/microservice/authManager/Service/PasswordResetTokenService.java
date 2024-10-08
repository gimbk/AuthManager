package com.microservice.authManager.Service;

import com.microservice.authManager.Dto.request.NewPasswordReq;
import com.microservice.authManager.Dto.response.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface PasswordResetTokenService {
    String getPasswordResetPage(String token, Model model);
    HttpResponse<?> sentEmailReset(String useremail, HttpServletRequest request);
    HttpResponse<?> handlePasswordReset(HttpServletRequest request, NewPasswordReq newPasswordReq) throws IOException;
}
