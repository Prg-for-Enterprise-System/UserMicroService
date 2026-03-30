package bt.edu.gcit.usermicroservice.dao;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import bt.edu.gcit.usermicroservice.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.TypedQuery;

@Repository
public class CountryDAOImpl implements CountryDAO {
    private EntityManager entityManager;

    @Autowired
    public CountryDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Country country) {
        entityManager.persist(country);
    }

    @Override
    public Country findById(int id) {
        return entityManager.find(Country.class, id);
    }

    @Override
    public List<Country> findAll() {
        return entityManager.createQuery("SELECT c FROM Country c", Country.class)
                .getResultList();
    }

@Override 
public List<Country> findAllByOrderByNameAsc() { 
return entityManager.createQuery("SELECT c FROM Country c ORDER BY c.name ASC", Country.class) 
.getResultList(); 
}

    @Override
    public void update(Country country) {
        entityManager.merge(country);
    }

    @Override
    public void delete(Country country) {
        entityManager.remove(country);
    }

    @Override
    public Country findByCode(String code) {
        TypedQuery<Country> query = entityManager.createQuery(
                "SELECT c FROM Country c WHERE c.code = :code", Country.class);
        query.setParameter("code", code);
        return query.getSingleResult();
    }
}