package bt.edu.gcit.usermicroservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bt.edu.gcit.usermicroservice.dao.CustomerDAO;
import bt.edu.gcit.usermicroservice.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import bt.edu.gcit.usermicroservice.entity.Country;
import bt.edu.gcit.usermicroservice.entity.AuthenticationType;
import bt.edu.gcit.usermicroservice.dao.CountryDAO;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerDAO customerDAO;
    private final CountryDAO countryDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, CountryDAO countryDAO,
            PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.countryDAO = countryDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Customer findByEMail(String email) {
        return customerDAO.findByEMail(email);
    }

    @Override
    @Transactional
    public Customer findByVerificationCode(String code) {
        return customerDAO.findByVerificationCode(code);
    }

    @Override
    @Transactional
    public void enable(long id) {
        customerDAO.enable(id);
    }

    @Override
    @Transactional
    public Customer registerCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerDAO.registerCustomer(customer);
    }

    @Override
    @Transactional
    public boolean isEmailUnique(String email) {
        return customerDAO.isEmailUnique(email);
    }

    // @Override
    // @Transactional
    // public boolean verify(String code) {
    // return customerDAO.verify(code);
    // }
    @Override
    @Transactional
    public Customer getCustomerById(long id) {
        return customerDAO.getCustomerById(id);
    }

    @Override
    @Transactional
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    @Override
    @Transactional
    public Customer updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(long id) {
        customerDAO.deleteCustomer(id);
    }

    @Override
    @Transactional
    public void updateAuthenticationType(Long customerId, AuthenticationType type) {
        customerDAO.updateAuthenticationType(customerId, type);
    }

    @Override
    @Transactional
    public void addNewCustomerUponOAuthLogin(String name, String email, String countrycode,
            AuthenticationType authenticationType) {
        Customer customer = new Customer();
        customer.setEmail(email);
        // customer.setFirstName(name);
        setName(name, customer);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(authenticationType);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        Country country = countryDAO.findByCode(countrycode);
        if (country == null) {
            log.error("Country not found for code: {}", countrycode);
            // Option 1: Skip save or redirect to profile completion
            // return; // or throw custom exception
            // Option 2: Fallback to default (e.g., Bhutan)
            country = countryDAO.findByCode("BT");
        }
        customer.setCountry(country);
        customerDAO.registerCustomer(customer);
    }

    private void setName(String name, Customer customer) {
        String[] names = name.split(" ");
        if (names.length < 2) {
            customer.setFirstName(names[0]);
            customer.setLastName("");
        } else {
            String firstName = names[0];
            customer.setFirstName(firstName);
            String lastName = name.replaceFirst(firstName, "").trim();
            customer.setLastName(lastName);
        }
    }
}