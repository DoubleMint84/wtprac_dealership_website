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
class СlientDAOTests {

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGetAllClients() {
        List<Client> clientList = clientDAO.getAll().stream().toList();
        assertEquals(3, clientList.size());

        clientDAO.deleteAllEntries();
        clientList = clientDAO.getAll().stream().toList();
        assertEquals(0, clientList.size());
    }

    @Test
    void testGetClientByName() {
        List<Client> clientList = clientDAO.getAllClientsByName("Иван").stream().toList();
        assertEquals(2, clientList.size());
    }

    @BeforeEach
    void setUp() {
        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client(1L, "Иван Иванов", "ivan@gmail.com",
                "ул. Пушкина, д. Колотушкина", "88005553535", "45 08 434277",
                null, "passwordHash1"));
        clientList.add(new Client(2L, "Петр Иванов", "petr.petrov@example.com",
                "Санкт-Петербург, Невский пр., 20", "+79165554433", "0987654321",
                "78 45 987654", "passwordHash2"));
        clientList.add(new Client(1L, "Николай Ивашин", "nickoliv@gmail.com",
                "ул. Горбунова, 44", "+79885369096", "45 12 409287",
                null, "passwordHash3"));

        clientDAO.saveCollection(clientList);
    }

    @BeforeAll
    @AfterEach
    void endUp() {
        clientDAO.deleteAllEntries();
    }
}
