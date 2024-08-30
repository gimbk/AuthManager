package com.microservice.authManager.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.microservice.authManager.Dto.request.LoginDTO;
import com.microservice.authManager.Dto.request.RoleRequestDto;
import com.microservice.authManager.Dto.request.UserDTO;
import com.microservice.authManager.Dto.request.UserRequestDto;
import com.microservice.authManager.Dto.response.HttpResponse;
import com.microservice.authManager.Dto.response.JwtResponse;
import com.microservice.authManager.Dto.response.RoleResponse;
import com.microservice.authManager.Entity.Role;
import com.microservice.authManager.Entity.User;
import com.microservice.authManager.Exception.EntitiesNotFoundException;
import com.microservice.authManager.Message.CustomMessage;
import com.microservice.authManager.Repository.RoleRepository;
import com.microservice.authManager.Repository.UserRepository;
import com.microservice.authManager.Security.JwtBlacklistConfig;
import com.microservice.authManager.Security.jwt.JwtUtils;
import com.microservice.authManager.Service.AuthService;
import com.microservice.authManager.Utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AuthServiceimpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtBlacklistConfig jwtBlacklistConfig;

    @Autowired
    JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceimpl.class);

    @Override
    public HttpResponse<?> logout(HttpServletRequest request) {
        try{
            String token = Utils.extractTokenFromHeader(request);
            if (token != null) {
                if (!jwtBlacklistConfig.isTokenBlacklisted(token)) {
                    jwtBlacklistConfig.blacklistToken(token);
                    return HttpResponse.<User>builder()
                            .message(CustomMessage.LOGOUT)
                            //.status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.OK.value())
                            .build();
                }
            }
            return HttpResponse.<User>builder()
                    .message(CustomMessage.TOKEN_INTROUVABLE)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }catch (Exception exception){
            return HttpResponse.<User>builder()
                    .message(exception.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }

    @Override
    public HttpResponse<?> saveUserAndRole(String keyapp, LoginDTO loginDTO) throws IOException {
        UserDTO userDTO = getConcernedUser(keyapp,loginDTO);
        Map<String,Object> map = new HashMap<>();
        if (userDTO == null){
            map.put("statuscode",HttpStatus.NOT_FOUND.value());
            //map.put("message",userDTO);
            map.put("message",CustomMessage.USER_NOT_EXIST);
            return HttpResponse.<User>builder()
                    .message(CustomMessage.USER_NOT_EXIST)
                    //.status(HttpStatus.NOT_FOUND)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        String roleResponse = getRoleById(userDTO.getRoleID(),keyapp);
        if (roleResponse == null){
            map.put("statuscode",HttpStatus.NOT_FOUND.value());
            map.put("message",CustomMessage.ROLE_NOT_EXIST);
            return HttpResponse.<User>builder()
                    .message(CustomMessage.ROLE_NOT_EXIST)
                    //.status(HttpStatus.NOT_FOUND)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        User user = new User();
        user.setUuid(userDTO.getUuid());
        user.setActived(userDTO.isActived());
        user.setAreaCode(userDTO.getAreaCode());
        user.setCityOfBirth(userDTO.getCityOfBirth());
        user.setCityResidence(userDTO.getCityResidence());
        user.setCivility(userDTO.getCivility());
        user.setCountryOfBirth(userDTO.getCountryOfBirth());
        user.setEmail(userDTO.getEmail());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setFirstname(userDTO.getFirstname());
        user.setName(userDTO.getName());
        user.setKeyapp(userDTO.getKeyapp());
        user.setPassword(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setRole(roleResponse);
        userRepository.save(user);
        Role role = new Role();
        role.setRole(roleResponse);
        role.setKeyapp(keyapp);
        roleRepository.save(role);
        return authenticateUser(loginDTO);

    }

    @Override
    public HttpResponse<?> authenticateUser(LoginDTO loginDTO) {
        logger.info("Démarrage de l'authentification pour l'utilisateur : {}", loginDTO.getUsername());
        Map<String, Object> map = new HashMap<>();
        try {
            // Authentification de l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            logger.info("Authentification réussie pour l'utilisateur : {}", loginDTO.getUsername());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);

            logger.info("Rôle attribué : {}", role);

            String username = userDetails.getUsername();
            String name = userDetails.getName();
            String firstname = userDetails.getFirstname();

            // Nettoyage des dépôts
            roleRepository.deleteAll();
            userRepository.deleteAll();

            // Réponse avec JWT
            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUuid(),username, name, firstname, role);
            logger.info("Réponse JWT générée avec succès pour l'utilisateur : {}", username);

            return HttpResponse.<User>builder()
                    .message(CustomMessage.ROLE_NOT_EXIST)
                    .data(jwtResponse)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
        } catch (Exception exception) {
            logger.error("Erreur lors de l'authentification de l'utilisateur : {}", loginDTO.getUsername(), exception);
            return HttpResponse.<User>builder()
                    .message(exception.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            throw new EntitiesNotFoundException(exception.getMessage());
        }
    }

    @Override
    public UserDTO getConcernedUser(String keyapp, LoginDTO loginDTO) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        // Créer l'objet Gson pour la sérialisation
        Gson gson = new Gson();

        // Construire l'objet à envoyer
        //String keyapp = request.getHeader("keyapp");
        UserRequestDto requestObject = new UserRequestDto(loginDTO.getUsername());

        // Convertir l'objet en JSON
        String json = gson.toJson(requestObject);

        // Configurer la requête POST
        HttpPost request2 = new HttpPost("http://localhost:9003/api/users/get-user");
        request2.setHeader("Content-Type", "application/json");
        request2.setHeader("keyapp", keyapp);
        request2.setEntity(new StringEntity(json));

        // Exécuter la requête et obtenir la réponse
        org.apache.hc.core5.http.HttpResponse httpResponse = client.execute(request2);

        // Lire et désérialiser la réponse
        UserDTO response2 = gson.fromJson(new InputStreamReader(((CloseableHttpResponse) httpResponse).getEntity().getContent()), UserDTO.class);

        if (response2.getUuid() == null){
            return null;
        }


        return response2;

    }

    @Override
    public String getRoleById(Long role, String keyapp) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        // Créer l'objet Gson pour la sérialisation
        Gson gson = new Gson();

        // Construire l'objet à envoyer
        //String keyapp = request.getHeader("keyapp");
        RoleRequestDto requestObject = new RoleRequestDto(keyapp,role);

        // Convertir l'objet en JSON
        String json = gson.toJson(requestObject);

        // Configurer la requête POST
        HttpPost request2 = new HttpPost("http://localhost:9001/api/role/get-role-name");
        request2.setHeader("Content-Type", "application/json");
        request2.setHeader("keyapp", keyapp);
        request2.setEntity(new StringEntity(json));

        // Exécuter la requête et obtenir la réponse
        org.apache.hc.core5.http.HttpResponse httpResponse = client.execute(request2);

        // Lire et désérialiser la réponse
        String response2 = gson.fromJson(new InputStreamReader(((CloseableHttpResponse) httpResponse).getEntity().getContent()), String.class);

        if (response2 == null){
            return null;
        }

        return response2;

    }


}
