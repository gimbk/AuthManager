package com.microservice.authManager.Service;

import com.microservice.authManager.Dto.request.LoginDTO;
import com.microservice.authManager.Dto.request.UserDTO;
import com.microservice.authManager.Dto.response.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthService {
    HttpResponse<?> logout(HttpServletRequest request);
    HttpResponse<?> saveUserAndRole(String keyapp, LoginDTO loginDTO) throws IOException;
    HttpResponse<?> authenticateUser(LoginDTO loginDTO);
    UserDTO getConcernedUser(String keyapp, LoginDTO loginDTO) throws IOException;
    String getRoleById(Long role, String keyapp) throws IOException;
}
