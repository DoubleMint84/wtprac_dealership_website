package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Order;

@Repository
public class OrderDAO extends CommonDAO<Order, Long> {
    public OrderDAO() { super(Order.class); }
}