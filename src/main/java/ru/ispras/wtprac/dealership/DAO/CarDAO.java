package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Car;

@Repository
public class CarDAO extends CommonDAO<Car, Long> {
    public CarDAO() { super(Car.class); }
}
