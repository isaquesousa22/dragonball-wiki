package com.dbwiki.services;

import com.dbwiki.models.User;
import com.dbwiki.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Cadastrar novo usuário
    public User registerUser(String username, String password, String role) throws Exception {
        if (userRepository.existsByUsername(username)) {
            throw new Exception("Usuário já existe!");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, hashedPassword, role);
        return userRepository.save(user);
    }

    // Validar login
    public boolean authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    // Buscar usuário por username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
