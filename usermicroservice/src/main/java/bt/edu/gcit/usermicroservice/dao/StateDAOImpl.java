package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Country;
import bt.edu.gcit.usermicroservice.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import jakarta.persistence.TypedQuery;

@Repository
public class StateDAOImpl implements StateDAO {
    private EntityManager entityManager;

    @Autowired
    public StateDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<State> findByCountryOrderByNameAsc(Country country) {
        return entityManager
                .createQuery("select s from State s where s.country = :country order by s.name asc", State.class)
                .setParameter("country", country).getResultList();
    }

    @Override
    public List<State> findAll() {
        return entityManager.createQuery("select s from State s",
                State.class).getResultList();
    }

    @Override
    public State findById(int theId) {
        return entityManager.find(State.class, theId);
    }

    @Override
    public void save(State theState, Long country_id) {
        entityManager.persist(theState);
    }

    @Override
    public void deleteById(int theId) {
        State state = entityManager.find(State.class, theId);
        entityManager.remove(state);
    }

    @Override
    public List<State> listStatesByCountry(Long countryId) {
        TypedQuery<State> query = entityManager.createQuery(
                "SELECT s FROM State s WHERE s.country.id = :countryId", State.class);
        query.setParameter("countryId", countryId);
        return query.getResultList();
    }
}