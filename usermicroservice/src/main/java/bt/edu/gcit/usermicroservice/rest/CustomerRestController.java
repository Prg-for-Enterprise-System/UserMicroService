package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Customer;
import bt.edu.gcit.usermicroservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import bt.edu.gcit.usermicroservice.entity.AuthenticationType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

    @PostMapping("/sendVerificationEmail")
    public void sendVerificationEmail(@RequestBody String email) {
        // implementation depends on your email service
    }
    // @PostMapping("/verifyAccount")
    // public boolean verifyAccount(@RequestBody String code) {
    //
    // return customerService.verify(code);

    // }
    @GetMapping("/code/{code}")
    public Customer findByVerificationCode(@PathVariable String code) {
        return customerService.findByVerificationCode(code);
    }

    @PostMapping("/enable/{id}")
    public void enable(@PathVariable long id) {
        customerService.enable(id);
    }

    @PostMapping("/isEmailUnique")
    public boolean isEmailUnique(@RequestBody String email) {
        return customerService.isEmailUnique(email);
    }
    // @PostMapping("/verify")
    // public boolean verify(@RequestBody String code) {
    //
    // return customerService.verify(code);

    // }
    // CRUD methods
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PutMapping("/update")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("/updateAuthenticationType/{customerId}")
    public void updateAuthenticationType(@PathVariable Long customerId, @RequestBody String type) {
        // Convert the String to an AuthenticationType
        AuthenticationType authenticationType;
        try {
            authenticationType = AuthenticationType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authentication type");
        }
        // Pass the AuthenticationType to the updateAuthenticationType method
        customerService.updateAuthenticationType(customerId, authenticationType);
    }
}