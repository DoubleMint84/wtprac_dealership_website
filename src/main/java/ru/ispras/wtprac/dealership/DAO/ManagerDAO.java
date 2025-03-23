package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Manager;

@Repository
public class ManagerDAO extends CommonDAO<Manager, Long> {
    public ManagerDAO() { super(Manager.class); }
}