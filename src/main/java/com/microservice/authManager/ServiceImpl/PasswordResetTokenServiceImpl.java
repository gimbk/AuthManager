package com.microservice.authManager.ServiceImpl;

import com.google.gson.Gson;
import com.microservice.authManager.Dto.request.NewPasswordReq;
import com.microservice.authManager.Dto.request.ResetPasswordReq;
import com.microservice.authManager.Dto.request.UserRequestDto;
import com.microservice.authManager.Dto.response.HttpResponse;
import com.microservice.authManager.Entity.PasswordResetToken;
import com.microservice.authManager.Entity.User;
import com.microservice.authManager.Message.CustomMessage;
import com.microservice.authManager.Repository.PasswordResetTokenRepository;
import com.microservice.authManager.Repository.UserRepository;
import com.microservice.authManager.Service.PasswordResetTokenService;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
        //fonction qui genere un lien et l'envois a l'utilisateur par mail pour l'inviter a renitialiser son mot de passe
        Map<String, Object> map = new HashMap<>();
        User user = userRepository.findByUsername(useremail.replaceAll("\"", ""));
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


            //sent email to user
            CloseableHttpClient client = HttpClients.createDefault();
            // Créer l'objet Gson pour la sérialisation
            Gson gson = new Gson();

            // Construire l'objet à envoyer
            String keyapp = request.getHeader("keyapp");
            ResetPasswordReq requestObject = new ResetPasswordReq(user.getEmail(),url);

            // Convertir l'objet en JSON
            String json = gson.toJson(requestObject);

            // Configurer la requête POST
            HttpPost request2 = new HttpPost("http://localhost:9003/api/users/get-user");
            request2.setHeader("Content-Type", "application/json");
            request2.setHeader("keyapp", keyapp);
            request2.setEntity(new StringEntity(json));

            // Exécuter la requête et obtenir la réponse
            org.apache.hc.core5.http.HttpResponse httpResponse = client.execute(request2);
            // fin de l'envois des mail


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
    public HttpResponse<?> handlePasswordReset(HttpServletRequest request, NewPasswordReq newPasswordReq) throws IOException {
// fonction pour modifier le mot de passe et envoyer un mail pour confirmer le changement du mot de passe
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(request.getHeader("token"));
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
            user.setPassword(encoder.encode(newPasswordReq.getNewpassword()));
            userRepository.save(user);
            passwordResetTokenRepository.delete(passwordResetToken);
            //sent email to user
            CloseableHttpClient client = HttpClients.createDefault();
            // Créer l'objet Gson pour la sérialisation
            Gson gson = new Gson();

            // Construire l'objet à envoyer
            String keyapp = request.getHeader("keyapp");
            ResetPasswordReq requestObject = new ResetPasswordReq(url,user.getName(),user.getEmail());

            // Convertir l'objet en JSON
            String json = gson.toJson(requestObject);

            // Configurer la requête POST
            HttpPost request2 = new HttpPost("http://localhost:9003/api/users/get-user");
            request2.setHeader("Content-Type", "application/json");
            request2.setHeader("keyapp", keyapp);
            request2.setEntity(new StringEntity(json));

            // Exécuter la requête et obtenir la réponse
            org.apache.hc.core5.http.HttpResponse httpResponse = client.execute(request2);
            // fin de l'envois des mail

            //emailSenderService.ConfirmResetPassword(user.getEmail(), url,user.getName());
            return HttpResponse.<PasswordResetToken>builder()
                    // .data(Collections.singleton(appKeyRepository.save(appKeyGen1)))
                    .message(CustomMessage.SUCCESS_RESET)
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }





}
