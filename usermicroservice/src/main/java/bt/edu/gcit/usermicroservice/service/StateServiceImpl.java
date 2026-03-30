package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.StateDAO;
import bt.edu.gcit.usermicroservice.entity.Country;
import bt.edu.gcit.usermicroservice.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bt.edu.gcit.usermicroservice.dao.CountryDAO;
import java.util.List;

@Service
public class StateServiceImpl implements StateService {
    private StateDAO stateDAO;
    private CountryDAO countryDAO;

    @Autowired
    public StateServiceImpl(StateDAO theStateDAO, CountryDAO theCountryDAO) {
        stateDAO = theStateDAO;
        countryDAO = theCountryDAO;
    }

    @Override
    @Transactional
    public List<State> findByCountryOrderByNameAsc(Country country) {
        return stateDAO.findByCountryOrderByNameAsc(country);
    }

    @Override
    @Transactional
    public List<State> findAll() {
        return stateDAO.findAll();
    }

    @Override
    @Transactional
    public State findById(int theId) {
        return stateDAO.findById(theId);
    }

    @Override
    @Transactional
    public void save(State theState, Long country_id) {
        Country country = countryDAO.findById(country_id.intValue());
        System.out.println("Country: " + country.getName());
        if (country == null) {
            throw new IllegalArgumentException("Country with id " + country_id + "does not exist");
        }
        State state = new State();
        state.setName(theState.getName());
        state.setCountry(country);
        System.out.println("State: " + state.getCountry());
        stateDAO.save(state, country_id);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        stateDAO.deleteById(theId);
    }

    @Override
    @Transactional
    public List<State> listStatesByCountry(Long countryId) {
        return stateDAO.listStatesByCountry(countryId);
    }
}