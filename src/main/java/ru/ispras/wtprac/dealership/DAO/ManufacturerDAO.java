package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Manufacturer;

@Repository
public class ManufacturerDAO extends CommonDAO<Manufacturer, Long> {
    public ManufacturerDAO() { super(Manufacturer.class); }
}
