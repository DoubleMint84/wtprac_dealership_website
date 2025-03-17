package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.Client;

public class ClientDAO extends CommonDAO<Client, Long> {
    public ClientDAO(Class<Client> entityClass) {
        super(entityClass);
    }
}
