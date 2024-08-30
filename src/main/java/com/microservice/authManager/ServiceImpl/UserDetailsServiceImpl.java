package com.microservice.authManager.ServiceImpl;

import com.microservice.authManager.Entity.User;
import com.microservice.authManager.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Chargement des détails pour l'utilisateur : {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null){
            new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        }

        logger.info("Utilisateur trouvé : {}", user);
        return UserDetailsImpl.build(user);
    }
}
