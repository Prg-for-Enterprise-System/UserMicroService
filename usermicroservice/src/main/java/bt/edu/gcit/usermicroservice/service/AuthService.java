package bt.edu.gcit.usermicroservice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    UserDetails login(String email, String password);
}