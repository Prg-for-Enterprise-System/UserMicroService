package bt.edu.gcit.usermicroservice.service;

import java.util.List;
import bt.edu.gcit.usermicroservice.entity.Country;

public interface CountryService {
    Country findByCode(String code);

    // Create
    void save(Country country);

    // Read
    Country findById(int id);

    List<Country> findAll();

    List<Country> findAllByOrderByNameAsc();

    // Update
    void update(Country country);

    // Delete
    void delete(Country country);
}