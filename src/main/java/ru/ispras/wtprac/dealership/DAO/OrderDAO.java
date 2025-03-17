package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.Order;

public class OrderDAO extends CommonDAO<Order, Long> {
    public OrderDAO(Class<Order> entityClass) {
        super(entityClass);
    }
}
