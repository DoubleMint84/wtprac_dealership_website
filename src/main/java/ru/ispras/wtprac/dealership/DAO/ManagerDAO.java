package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.Manager;

public class ManagerDAO extends CommonDAO<Manager, Long> {
    public ManagerDAO(Class<Manager> entityClass) {
        super(entityClass);
    }
}