package ru.ispras.wtprac.dealership.DAO;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Brand;
import ru.ispras.wtprac.dealership.model.Model;

@Repository
public class BrandDAO extends CommonDAO<Brand, Long> {
    public BrandDAO() { super(Brand.class); }

    public Brand getBrandByName(final String brandName) {
        Brand result;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from Brand where name = :brandName", Brand.class)
                    .setParameter("brandName", brandName).uniqueResult();
            session.getTransaction().commit();
        }
        return result;
    }
}
