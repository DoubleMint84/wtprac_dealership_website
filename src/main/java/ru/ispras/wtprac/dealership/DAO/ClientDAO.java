package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Client;

@Repository
public class ClientDAO extends CommonDAO<Client, Long> {
    public ClientDAO() { super(Client.class); }
}