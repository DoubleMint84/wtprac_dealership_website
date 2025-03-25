package ru.ispras.wtprac.dealership.DAO;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Model;

import java.util.Collection;
import java.util.List;

@Repository
public class ModelDAO extends CommonDAO<Model, Long> {
    public ModelDAO() { super(Model.class); }

    public Collection<Model> getAllModelsByBrand(final String brandName) {
        List<Model> result;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from Model where brand.name = :brandName", Model.class)
                            .setParameter("brandName", brandName).getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    public Collection<Model> getAllModelsByYear(int year) {
        List<Model> result;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from Model where year = :year", Model.class)
                    .setParameter("year", year).getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    public Collection<Model> getAllModelsByYear(int minYear, int maxYear) {
        List<Model> result;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from Model where year >= :minYear and year <= :maxYear", Model.class)
                    .setParameter("minYear", minYear).setParameter("maxYear", maxYear).getResultList();
            session.getTransaction().commit();
        }
        return result;
    }
}
