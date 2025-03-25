package ru.ispras.wtprac.dealership.DAO;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Client;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class ClientDAO extends CommonDAO<Client, Long> {
    public ClientDAO() { super(Client.class); }

    public Collection<Client> getAllClientsByName(final String name) {
        Collection<Client> result;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("from Client where name like :name", Client.class)
                    .setParameter("name", '%' + name + '%').list();
        }
        return result == null ? new ArrayList<>() : result;
    }
}