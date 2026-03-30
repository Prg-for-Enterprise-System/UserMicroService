package bt.edu.gcit.usermicroservice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import bt.edu.gcit.usermicroservice.entity.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import bt.edu.gcit.usermicroservice.entity.Country;
import bt.edu.gcit.usermicroservice.entity.AuthenticationType;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    private EntityManager entityManager;

    @Autowired
    public CustomerDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Customer findByEMail(String email) {
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email",
                Customer.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    @Override
    public Customer findByVerificationCode(String code) {
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.verificationCode = :code",
                Customer.class);
        query.setParameter("code", code);
        return query.getSingleResult();
    }

    @Override
    public void enable(long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            customer.setEnabled(true);
            entityManager.merge(customer);
        }
    }

    @Override
    public Customer registerCustomer(Customer customer) {

        Country country = customer.getCountry();
        System.out.println("Country: " + country.getName());

        if (country != null) {

            // Fetch the country from the database to ensure it's fully initialized
            country = entityManager.find(Country.class, country.getId());

            if (country == null) {
                // If the country is not found in the database, throw an exception
                throw new RuntimeException("Country not found");
            }

            customer.setCountry(country);
            System.out.println("Country: " + country.getName());

        } else {
            // If the country is null, throw an exception
            throw new RuntimeException("Country is null");
        }

        customer.setAuthenticationType(AuthenticationType.DATABASE);
        entityManager.persist(customer);
        return customer;

        // entityManager.merge(customer);
        // return customer;
    }

    @Override
    public boolean isEmailUnique(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email",
                Long.class);
        query.setParameter("email", email);
        long count = query.getSingleResult();
        return count == 0;
    }

    // @Override
    // public boolean verify(String code) {

    // TypedQuery<Customer> query = entityManager.createQuery(
    // "SELECT c FROM Customer c WHERE c.verificationCode = :code",
    // Customer.class
    // );

    // }

    // query.setParameter("code", code);

    // Customer customer = query.getSingleResult();

    // if (customer != null) {
    // customer.setVerificationCode(true);
    // entityManager.merge(customer);
    // return true;
    // }

    // return false;

    @Override
    public Customer getCustomerById(long id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c",
                Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return entityManager.merge(customer);
    }

    @Override
    public void deleteCustomer(long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }

    @Override
    public void updateAuthenticationType(Long customerId, AuthenticationType type) {
        Customer customer = entityManager.find(Customer.class, customerId);
        if (customer != null) {
            customer.setAuthenticationType(type);
            entityManager.merge(customer);
        }
    }
}