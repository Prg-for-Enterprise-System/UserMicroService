package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Customer;
import java.util.List;
import bt.edu.gcit.usermicroservice.entity.AuthenticationType;

public interface CustomerService {
    Customer findByEMail(String email);

    Customer findByVerificationCode(String code);

    void enable(long id);

    Customer registerCustomer(Customer customer);

    boolean isEmailUnique(String email);

    // boolean verify(String code);
    Customer getCustomerById(long id);

    List<Customer> getAllCustomers();

    Customer updateCustomer(Customer customer);

    void deleteCustomer(long id);

    void updateAuthenticationType(Long customerId, AuthenticationType type);

    void addNewCustomerUponOAuthLogin(String name, String email, String countrycode,
            AuthenticationType authenticationType);
}