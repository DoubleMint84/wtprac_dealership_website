package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Brand;

@Repository
public class BrandDAO extends CommonDAO<Brand, Long> {
    public BrandDAO() { super(Brand.class); }
}
