package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.VehicleConfiguration;

@Repository
public class VehicleConfigurationDAO extends CommonDAO<VehicleConfiguration, Long> {
    public VehicleConfigurationDAO() {
        super(VehicleConfiguration.class);
    }
}
