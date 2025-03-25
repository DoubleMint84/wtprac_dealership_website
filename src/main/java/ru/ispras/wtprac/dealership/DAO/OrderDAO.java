package ru.ispras.wtprac.dealership.DAO;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Order;
import ru.ispras.wtprac.dealership.model.Client;

import java.util.Collection;

@Repository
public class OrderDAO extends CommonDAO<Order, Long> {
    public OrderDAO() { super(Order.class); }

    public Collection<Order> getAllOrdersOfClient(Client client) {
        Collection<Order> result;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("from Order where client = :client", Order.class)
                    .setParameter("client", client)
                    .list().stream().toList();
        }
        return result;
    }

    public Collection<Order> getAllOrdersWithUnannouncedTestDrive() {
        Collection<Order> result;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("from Order where needsPreTestDrive = true and testDrive = null",
                            Order.class).list().stream().toList();
        }
        return result;
    }
}