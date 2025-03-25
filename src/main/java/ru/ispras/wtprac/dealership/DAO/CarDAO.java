package ru.ispras.wtprac.dealership.DAO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Car;
import ru.ispras.wtprac.dealership.model.CarStatus;
import ru.ispras.wtprac.dealership.model.VehicleConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CarDAO extends CommonDAO<Car, Long> {
    public CarDAO() { super(Car.class); }

    public Collection<Car> getAllCarsByFilter(Filter filter) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Car c WHERE 1=1");
            Map<String, Object> params = new HashMap<>();

            processStringFilters(filter, hql, params);
            processNumericFilters(filter, hql, params);
            processDateFilters(filter, hql, params);
            processStatusFilter(filter, hql, params);
            processConfigFilter(filter, hql, params);

            Query<Car> query = session.createQuery(hql.toString(), Car.class);
            params.forEach(query::setParameter);

            return query.getResultList();
        }
    }

    private void processStringFilters(Filter filter, StringBuilder hql, Map<String, Object> params) {
        if (filter.getName() != null) {
            hql.append(" AND c.name LIKE :name");
            params.put("name", "%" + filter.getName() + "%");
        }

        if (filter.getVin() != null) {
            hql.append(" AND c.vin = :vin");
            params.put("vin", filter.getVin());
        }

        if (filter.getColor() != null) {
            hql.append(" AND c.color = :color");
            params.put("color", filter.getColor());
        }

        if (filter.getSeat() != null) {
            hql.append(" AND c.seat = :seat");
            params.put("seat", filter.getSeat());
        }
    }

    private void processNumericFilters(Filter filter, StringBuilder hql, Map<String, Object> params) {
        processBigDecimalFilter("price",
                filter.getPriceExact(),
                filter.getPriceMin(),
                filter.getPriceMax(),
                hql, params);

        processIntegerFilter("mileage",
                filter.getMileageExact(),
                filter.getMileageMin(),
                filter.getMileageMax(),
                hql, params);
    }


    private void processDateFilters(Filter filter, StringBuilder hql, Map<String, Object> params) {
        if (filter.getCreationDateStart() != null) {
            hql.append(" AND c.dateOfCreation >= :creationStart");
            params.put("creationStart", filter.getCreationDateStart());
        }

        if (filter.getCreationDateEnd() != null) {
            hql.append(" AND c.dateOfCreation <= :creationEnd");
            params.put("creationEnd", filter.getCreationDateEnd());
        }

        if (filter.getLastTOStart() != null) {
            hql.append(" AND c.dateLastTO >= :toStart");
            params.put("toStart", filter.getLastTOStart());
        }

        if (filter.getLastTOEnd() != null) {
            hql.append(" AND c.dateLastTO <= :toEnd");
            params.put("toEnd", filter.getLastTOEnd());
        }
    }

    private void processStatusFilter(Filter filter, StringBuilder hql, Map<String, Object> params) {
        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            hql.append(" AND c.carStatus IN :statuses");
            params.put("statuses", filter.getStatuses());
        }
    }

    private void processConfigFilter(Filter filter, StringBuilder hql, Map<String, Object> params) {
        if (filter.getConfig() != null) {
            hql.append(" AND c.vehicleConfiguration = :config");
            params.put("config", filter.getConfig());
        }
    }

    private void processIntegerFilter(String field, Integer exact, Integer min, Integer max,
                                      StringBuilder hql, Map<String, Object> params) {
        if (exact != null) {
            hql.append(" AND c.").append(field).append(" = :").append(field);
            params.put(field, exact);
        } else {
            if (min != null) {
                hql.append(" AND c.").append(field).append(" >= :").append(field).append("Min");
                params.put(field + "Min", min);
            }
            if (max != null) {
                hql.append(" AND c.").append(field).append(" <= :").append(field).append("Max");
                params.put(field + "Max", max);
            }
        }
    }

    private void processBigDecimalFilter(String field, BigDecimal exact, BigDecimal min, BigDecimal max,
                                         StringBuilder hql, Map<String, Object> params) {
        if (exact != null) {
            hql.append(" AND c.").append(field).append(" = :").append(field);
            params.put(field, exact);
        } else {
            if (min != null) {
                hql.append(" AND c.").append(field).append(" >= :").append(field).append("Min");
                params.put(field + "Min", min);
            }
            if (max != null) {
                hql.append(" AND c.").append(field).append(" <= :").append(field).append("Max");
                params.put(field + "Max", max);
            }
        }
    }

    @Getter
    @Setter
    public static class Filter {
        private String name;
        private String vin;
        private String color;
        private String seat;

        private BigDecimal priceExact;
        private BigDecimal priceMin;
        private BigDecimal priceMax;
        private Integer mileageExact;
        private Integer mileageMin;
        private Integer mileageMax;

        private LocalDate creationDateStart;
        private LocalDate creationDateEnd;
        private LocalDate lastTOStart;
        private LocalDate lastTOEnd;

        private List<CarStatus> statuses;

        private VehicleConfiguration config;

    }
}
