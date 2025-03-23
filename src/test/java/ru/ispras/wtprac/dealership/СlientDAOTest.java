package ru.ispras.wtprac.dealership;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ispras.wtprac.dealership.DAO.ClientDAO;
import ru.ispras.wtprac.dealership.model.Client;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class СlientDAOTest {

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGetAllClients() {
        List<Client> clientList = clientDAO.getAll().stream().toList();
        assertEquals(1, clientList.size());

        clientDAO.deleteAllEntries();
        clientList = clientDAO.getAll().stream().toList();
        assertEquals(0, clientList.size());
    }

    @BeforeEach
    void setUp() {
        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client(1L, "Иван Иванов", "ivan@gmail.com",
                "ул. Пушкина, д. Колотушкина", "88005553535", "45 08 434277",
                null, "passwordHash1"));

        clientDAO.saveCollection(clientList);
    }

    @BeforeAll
    @AfterEach
    void endUp() {
        clientDAO.deleteAllEntries();
    }
}
