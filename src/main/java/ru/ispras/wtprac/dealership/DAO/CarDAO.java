package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.Car;

public class CarDAO extends CommonDAO<Car, Long> {
    public CarDAO(Class<Car> entityClass) {
        super(entityClass);
    }
}
