package ru.ispras.wtprac.dealership.DAO;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Client;
import ru.ispras.wtprac.dealership.model.Manager;

import java.util.Collection;

@Repository
public class ManagerDAO extends CommonDAO<Manager, Long> {
    public ManagerDAO() { super(Manager.class); }

    public Manager findByEmail(final String email) {
        Collection<Manager> result;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("from Manager where email like :email", Manager.class)
                    .setParameter("email", '%' + email + '%').getResultList();
        }
        return result.isEmpty() ? null : result.iterator().next();
    }
}