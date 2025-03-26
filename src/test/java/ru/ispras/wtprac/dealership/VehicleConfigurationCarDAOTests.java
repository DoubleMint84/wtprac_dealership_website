package ru.ispras.wtprac.dealership;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ispras.wtprac.dealership.DAO.*;
import ru.ispras.wtprac.dealership.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class VehicleConfigurationCarDAOTests {

    @Autowired
    private VehicleConfigurationDAO vehicleConfigurationDAO;

    @Autowired
    private CarDAO carDAO;

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private BrandDAO brandDAO;

    @Test
    void testFilterByPowerRange() {
        VehicleConfigurationDAO.Filter filter = new VehicleConfigurationDAO.Filter();
        filter.setEnginePowerMin(190);
        filter.setEnginePowerMax(245);

        Collection<VehicleConfiguration> result = vehicleConfigurationDAO.getConfigurationsByFilter(filter);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(c ->
                c.getEnginePower().compareTo(190) >= 0 &&
                        c.getEnginePower().compareTo(245) <= 0
        ));

        filter.setName("Sport");
        filter.setHasCruiseControl(true);
        filter.setIsOnSale(true);
        result = vehicleConfigurationDAO.getConfigurationsByFilter(filter);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c ->
                c.getEnginePower().compareTo(190) >= 0 &&
                        c.getEnginePower().compareTo(245) <= 0 &&
                        c.getName().equals("Sport") &&
                        c.getIsOnSale() &&
                        c.getHasCruiseControl()
        ));
    }

    @Test
    void testFilterByTransmissionList() {
        VehicleConfigurationDAO.Filter filter = new VehicleConfigurationDAO.Filter();
        filter.setTransmissions(List.of("Automatic", "Robot"));

        Collection<VehicleConfiguration> result = vehicleConfigurationDAO.getConfigurationsByFilter(filter);
        assertEquals(4, result.size());
        assertTrue(result.stream().allMatch(c ->
                c.getTransmission().equals("Automatic") ||
                        c.getTransmission().equals("Robot")
        ));

        filter.setModel(modelDAO.getById(1L));
        filter.setEnginePowerExact(250);
        result = vehicleConfigurationDAO.getConfigurationsByFilter(filter);
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(c ->
                (c.getTransmission().equals("Automatic") ||
                        c.getTransmission().equals("Robot"))
                && c.getModel().getName().equals("911")
                && c.getEnginePower() == 250
        ));
    }

    @Test
    void testFilterByPriceRangeAndColor() {
        CarDAO.Filter filter = new CarDAO.Filter();
        filter.setPriceMin(new BigDecimal("50000"));
        filter.setPriceMax(new BigDecimal("100000"));
        filter.setColor("Red");

        Collection<Car> result = carDAO.getAllCarsByFilter(filter);
        assertEquals(1, result.size());
        Car car = result.iterator().next();
        assertTrue(car.getPrice().compareTo(new BigDecimal("50000")) >= 0);
        assertTrue(car.getPrice().compareTo(new BigDecimal("100000")) <= 0);
        assertEquals("Red", car.getColor());
    }

    @Test
    void testFilterByMileageRangeAndSeat() {
        CarDAO.Filter filter = new CarDAO.Filter();
        filter.setMileageMin(300);
        filter.setMileageMax(800);
        filter.setSeat("Leather");
        filter.setName("Sport");

        Collection<Car> result = carDAO.getAllCarsByFilter(filter);
        assertEquals(2, result.size());
        Iterator<Car> iterator = result.iterator();
        Car car = iterator.next();
        assertTrue(car.getMileage().compareTo(300) >= 0);
        assertTrue(car.getMileage().compareTo(800) <= 0);
        assertEquals("Leather", car.getSeat());
        assertEquals("Toyota Camry Sport", car.getName());
        car = iterator.next();
        assertTrue(car.getMileage().compareTo(300) >= 0);
        assertTrue(car.getMileage().compareTo(800) <= 0);
        assertEquals("Leather", car.getSeat());
        assertEquals("Chevrolet Camaro Sport", car.getName());
    }

    @Test
    void testFilterByConfigAndDateLastTO() {
        CarDAO.Filter filter = new CarDAO.Filter();
        filter.setConfig(vehicleConfigurationDAO.getById(2L));
        filter.setLastTOStart(LocalDate.parse("2023-01-01"));
        filter.setLastTOEnd(LocalDate.parse("2023-10-12"));
        filter.setPriceExact(BigDecimal.valueOf(75000));

        Collection<Car> result = carDAO.getAllCarsByFilter(filter);
        assertEquals(1, result.size());
        Car car = result.iterator().next();
        assertFalse(car.getDateLastTO().isBefore(LocalDate.parse("2023-01-01")));
        assertFalse(car.getDateLastTO().isAfter(LocalDate.parse("2023-10-12")));
        assertEquals(new BigDecimal("75000.00"), car.getPrice());
        assertEquals(2L, car.getVehicleConfiguration().getId());

    }

    @Test
    void testFilterByConfig() {
        CarDAO.Filter filter = new CarDAO.Filter();
        filter.setCreationDateStart(LocalDate.parse("2023-03-01"));
        filter.setCreationDateEnd(LocalDate.parse("2023-04-01"));
        filter.setStatuses(List.of(CarStatus.InProcessing));

        Collection<Car> result = carDAO.getAllCarsByFilter(filter);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c ->
                !c.getDateOfCreation().isBefore(LocalDate.parse("2023-03-01")) &&
                        !c.getDateOfCreation().isAfter(LocalDate.parse("2023-04-01")) &&
                        c.getCarStatus() == CarStatus.InProcessing
        ));
    }

    @Test
    void testGetCarByVin() {
        CarDAO.Filter filter = new CarDAO.Filter();
        filter.setVin("KMHDN46D24U136245");

        Collection<Car> result = carDAO.getAllCarsByFilter(filter);
        assertEquals(1, result.size());
        assertEquals("KMHDN46D24U136245", result.iterator().next().getVin());
        assertEquals("Toyota Camry Sport", result.iterator().next().getName());
    }

    @BeforeEach
    void setUp() {
        List<Brand> brandList = new ArrayList<>();
        brandList.add(new Brand("Porsche"));
        brandList.add(new Brand("Toyota"));
        brandList.add(new Brand("Chevrolet"));
        brandDAO.saveCollection(brandList);

        List<Model> modelList = new ArrayList<>();
        modelList.add(new Model(1L, "911", brandDAO.getBrandByName("Porsche"), 2021,
                Map.of("transmission", "PDK")));
        modelList.add(new Model(2L, "Camry", brandDAO.getBrandByName("Toyota"), 2022,
                new HashMap<String, String>()));
        modelList.add(new Model(3L, "Camaro", brandDAO.getBrandByName("Chevrolet"), 2019,
                new HashMap<String, String>()));
        modelDAO.saveCollection(modelList);

        List<VehicleConfiguration> configurations = List.of(
                createConfig("Basic", modelDAO.getById(1L), "1.6", 180, "Manual"),
                createConfig("Comfort", modelDAO.getById(1L), "2.0", 250, "Automatic"),
                createConfig("Luxury", modelDAO.getById(2L), "3.0", 200, "Robot"),
                createConfig("Sport", modelDAO.getById(2L), "2.5", 240, "Automatic"),
                createConfig("Sport", modelDAO.getById(3L), "3.5", 230, "Automatic")
        );
        vehicleConfigurationDAO.saveCollection(configurations);

        List<Car> cars = List.of(
                createCar("Porsche 911 Basic", "JTDBE32K603218947",
                        new BigDecimal("25000"), LocalDate.parse("2023-01-10"), "", 5000,
                        vehicleConfigurationDAO.getById(1L)),
                createCar("Porsche 911 Comfort", "WBANV93549C139876", new BigDecimal("75000"),
                        LocalDate.parse("2023-03-15"), "Black", 2000,
                        vehicleConfigurationDAO.getById(2L)),
                createCar("Toyota Camry Luxury", "1FAFP45X5YF206781", new BigDecimal("65000"),
                        LocalDate.parse("2023-03-20"), "Red", 1500,
                        vehicleConfigurationDAO.getById(3L)),
                createCar("Toyota Camry Sport", "KMHDN46D24U136245", new BigDecimal("95000"),
                        LocalDate.parse("2023-04-05"), "Silver", 500,
                        vehicleConfigurationDAO.getById(4L)),
                createCar("Chevrolet Camaro Sport", "5YJSA1E26MF168239", new BigDecimal(110000),
                        LocalDate.parse("2023-05-12"), "Yellow", 700,
                        vehicleConfigurationDAO.getById(5L)));
        carDAO.saveCollection(cars);
    }

    private VehicleConfiguration createConfig(String name, Model model, String engine,
                                              int power, String transmission) {
        VehicleConfiguration config = new VehicleConfiguration();
        config.setName(name);
        config.setModel(model);
        config.setEngineVolume(new BigDecimal(engine));
        config.setEnginePower(power);
        config.setTransmission(transmission);
        config.setHasCruiseControl(true);
        config.setIsOnSale(true);
        return config;
    }

    private Car createCar(String name, String vin, BigDecimal price,
                          LocalDate date, String color, int mileage, VehicleConfiguration configuration) {
        Car car = new Car();
        car.setName(name);
        car.setVin(vin);
        car.setPrice(price);
        car.setDateOfCreation(date);
        car.setColor(color);
        car.setSeat("Leather");
        car.setMileage(mileage);
        car.setDateLastTO(date);
        car.setCarStatus(CarStatus.InProcessing);
        car.setVehicleConfiguration(configuration);
        return car;
    }

    @BeforeAll
    @AfterEach
    void endUp() {
        carDAO.deleteAllEntries();
        vehicleConfigurationDAO.deleteAllEntries();
        modelDAO.deleteAllEntries();
        brandDAO.deleteAllEntries();
    }
}