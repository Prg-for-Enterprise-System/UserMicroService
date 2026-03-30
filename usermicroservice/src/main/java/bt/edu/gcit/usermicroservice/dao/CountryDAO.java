package bt.edu.gcit.usermicroservice.dao;

import java.util.List;
import bt.edu.gcit.usermicroservice.entity.Country;

public interface CountryDAO {
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