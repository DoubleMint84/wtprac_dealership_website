package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.Brand;

public class BrandDAO extends CommonDAO<Brand, Long> {
    public BrandDAO(Class<Brand> entityClass) {
        super(entityClass);
    }
}
