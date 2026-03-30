package bt.edu.gcit.usermicroservice.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;

import java.util.List;
import java.util.stream.Collectors;

import bt.edu.gcit.usermicroservice.security.ShopmeuserDetails;
import bt.edu.gcit.usermicroservice.dao.CustomerDAO; // import the new DAO for Customer entities
import bt.edu.gcit.usermicroservice.entity.Customer; // import the new Customer entity
import java.util.Collections; // import the Collections class

@Service
public class ShopeMeUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;
    private final CustomerDAO customerDAO; // new DAO for Customer entities

    @Autowired
    public ShopeMeUserDetailsService(UserDAO userDAO, CustomerDAO customerDAO) {
        this.userDAO = userDAO;
        this.customerDAO = customerDAO; // initialize the new DAO
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userDAO.findByEmail(email);

        if (user != null) {
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );
        }

        // Try to load a Customer if no User was found
        Customer customer = customerDAO.findByEMail(email);

        if (customer != null) {
            // Customer has no roles → return empty authorities
            return new org.springframework.security.core.userdetails.User(
                    customer.getEmail(),
                    customer.getPassword(),
                    Collections.emptyList()
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);

        // ===== OLD / COMMENTED CODE =====

        // User user = userDAO.findByEmail(email);
        // if (user == null) {
        //     throw new UsernameNotFoundException("User not found with email: " + email);
        // }

        // return new ShopmeuserDetails(user);

        // System.out.println("Email: " + email);

        // User user = userDAO.findByEmail(email);
        // if (user == null) {
        //     throw new UsernameNotFoundException("User not found with email: " + email);
        // }

        // List<GrantedAuthority> authorities = user.getRoles().stream()
        //         .map(role -> {
        //             System.out.println("Role: " + role.getName());
        //             return new SimpleGrantedAuthority(role.getName());
        //         })
        //         .collect(Collectors.toList());

        // System.out.println("Authorities: " + authorities);
        // System.out.println("User in loadbyUserna: " + user.getPassword());

        // return new org.springframework.security.core.userdetails.User(
        //         user.getEmail(),
        //         user.getPassword(),
        //         authorities
        // );
    }
}