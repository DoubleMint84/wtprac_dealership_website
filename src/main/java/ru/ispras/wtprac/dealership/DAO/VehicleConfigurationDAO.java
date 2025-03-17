package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.VehicleConfiguration;

public class VehicleConfigurationDAO extends CommonDAO<VehicleConfiguration, Long> {
    public VehicleConfigurationDAO(Class<VehicleConfiguration> entityClass) {
        super(entityClass);
    }
}
