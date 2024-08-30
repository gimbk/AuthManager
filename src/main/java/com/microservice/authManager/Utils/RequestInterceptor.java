package com.microservice.authManager.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.authManager.Entity.AppKeyGen;
import com.microservice.authManager.Message.CustomMessage;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // Wrapping the original request to cache the request body
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }

        // Récupérer le keyapp depuis les headers
        String keyapp = request.getHeader("keyapp");

        if (keyapp == null || keyapp.isEmpty()) {
            sendErrorResponse(response, "Missing keyapp header", HttpStatus.BAD_REQUEST.value());
            return false;
        }

        // Simuler une requête externe pour vérifier l'AppKey
        ObjectMapper mapper = new ObjectMapper();
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request2 = new HttpGet("http://localhost:9000/api/key/generator/check/" + keyapp);

            AppKeyGen response2 = client.execute(request2, httpResponse ->
                    mapper.readValue(httpResponse.getEntity().getContent(), AppKeyGen.class));

            if (response2.statusCode == 404) {
                sendErrorResponse(response, CustomMessage.APP_NOT_FOUND, HttpStatus.SERVICE_UNAVAILABLE.value());
                return false;
            }
            return true;
        } catch (IOException e) {
            sendErrorResponse(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }
    }

    public void sendErrorResponse(HttpServletResponse response, String message,int code) throws IOException {
        response.setStatus(code);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);

        String jsonResponse = new ObjectMapper().writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }
}
