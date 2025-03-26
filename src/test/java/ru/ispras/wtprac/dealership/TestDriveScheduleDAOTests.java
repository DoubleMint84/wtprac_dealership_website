package ru.ispras.wtprac.dealership;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ispras.wtprac.dealership.DAO.*;
import ru.ispras.wtprac.dealership.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class TestDriveScheduleDAOTests {

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private TestDriveScheduleDAO testDriveScheduleDAO;

    @Autowired
    private CarDAO carDAO;

    @Autowired
    private ManagerDAO managerDAO;

    @Test
    void testGetCarsByTime1() {
        List<Car> res = testDriveScheduleDAO.getCarsOnTestDriveByDateTime(
                        LocalDateTime.parse("2025-04-02T10:15:30"))
                .stream().toList();

        assertEquals(1, res.size());
        assertEquals("BMW X5 Black", res.get(0).getName());
    }

    @Test
    void testGetCarsByTime2() {
        List<Car> res = testDriveScheduleDAO.getCarsOnTestDriveByDateTime(
                        LocalDateTime.parse("2025-04-05T10:00:30"), true)
                .stream().toList();
        assertEquals(2, res.size());
        assertEquals("Ford Mustang Red", res.get(0).getName());
        assertEquals("BMW X5 Black", res.get(1).getName());
    }

    @Test
    void testGetCarsByTime3() {
        List<Car> res = testDriveScheduleDAO.getCarsOnTestDriveByDateTime(
                        LocalDateTime.parse("2025-04-05T10:00:30"))
                .stream().toList();
        assertEquals(1, res.size());
        assertEquals("BMW X5 Black", res.get(0).getName());
    }

    @BeforeEach
    void setUp() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Toyota Corolla White",
                LocalDate.parse("2023-01-10"), "JTDBE32K603218947", new BigDecimal("24000.0")));
        cars.add(new Car("BMW X5 Black",
                LocalDate.parse("2023-02-15"), "WBANV93549C139876", new BigDecimal("68000")));
        cars.add(new Car("Ford Mustang Red",
                LocalDate.parse("2023-03-20"), "1FAFP45X5YF206781", new BigDecimal(53000)));
        cars.add(new Car("Hyundai Elantra Blue",
                LocalDate.parse("2023-04-05"), "KMHDN46D24U136245", new BigDecimal(26000)));
        cars.add(new Car("Tesla Model S White",
                LocalDate.parse("2023-05-12"), "5YJSA1E26MF168239", new BigDecimal(110000)));
        carDAO.saveCollection(cars);

        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client(1L, "Иван Иванов", "ivan@gmail.com",
                "ул. Пушкина, д. Колотушкина", "88005553535", "45 08 434277",
                null, "passwordHash1"));
        clientList.add(new Client(2L, "Петр Иванов", "petr.petrov@example.com",
                "Санкт-Петербург, Невский пр., 20", "+79165554433", "0987654321",
                "78 45 987654", "passwordHash2"));
        clientList.add(new Client(3L, "Николай Ивашин", "nickoliv@gmail.com",
                "ул. Горбунова, 44", "+79885369096", "45 12 409287",
                null, "passwordHash3"));
        clientDAO.saveCollection(clientList);

        List<Manager> managerList = new ArrayList<>();
        managerList.add(new Manager("Алексей Соколов", "alexey.sokolov@example.com",
                "password1"));
        managerList.add(new Manager("Дмитрий Орлов", "dmitry.orlov@example.com",
                "password2"));
        managerList.add(new Manager("Мария Белова", "maria.belova@example.com",
                "password3"));
        managerList.add(new Manager("Виктор Иванченко", "victor.ivanchenco@example.com",
                "password4"));
        managerList.add(new Manager("Ольга Карпова", "olga.karpova@example.com",
                "password5"));
        managerDAO.saveCollection(managerList);

        List<TestDriveSchedule> scheduleList = new ArrayList<>();
        scheduleList.add(new TestDriveSchedule(carDAO.getById(3L), LocalDateTime.parse("2025-04-03T10:15:30"),
                LocalDateTime.parse("2025-04-07T10:15:30"), managerDAO.getById(2L), clientDAO.getById(3L)));
        scheduleList.get(0).setTestDriveStatus(TestDriveStatus.Canceled);
        scheduleList.add(new TestDriveSchedule(carDAO.getById(1L), LocalDateTime.parse("2025-03-29T10:15:30"),
                LocalDateTime.parse("2025-04-01T10:15:30"), managerDAO.getById(1L), clientDAO.getById(2L)));
        scheduleList.add(new TestDriveSchedule(carDAO.getById(2L), LocalDateTime.parse("2007-03-30T10:15:30"),
                LocalDateTime.parse("2025-04-05T10:15:30"), managerDAO.getById(4L), clientDAO.getById(1L)));
        testDriveScheduleDAO.saveCollection(scheduleList);
    }

    @BeforeAll
    @AfterEach
    void endUp() {
        testDriveScheduleDAO.deleteAllEntries();
        managerDAO.deleteAllEntries();
        clientDAO.deleteAllEntries();
        carDAO.deleteAllEntries();
    }
}
