package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.Manufacturer;

public class ManufacturerDAO extends CommonDAO<Manufacturer, Long> {
    public ManufacturerDAO(Class<Manufacturer> entityClass) {
        super(entityClass);
    }
}
