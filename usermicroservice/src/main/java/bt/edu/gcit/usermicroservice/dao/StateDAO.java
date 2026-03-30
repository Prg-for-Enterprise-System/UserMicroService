package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Country;
import bt.edu.gcit.usermicroservice.entity.State;
import java.util.List;

public interface StateDAO {
    List<State> findByCountryOrderByNameAsc(Country country);

    List<State> findAll();

    State findById(int theId);

    void save(State theState, Long country_id);

    void deleteById(int theId);

    List<State> listStatesByCountry(Long countryId);
}