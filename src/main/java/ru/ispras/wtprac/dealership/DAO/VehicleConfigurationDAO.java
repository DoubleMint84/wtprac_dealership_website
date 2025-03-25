package ru.ispras.wtprac.dealership.DAO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Model;
import ru.ispras.wtprac.dealership.model.VehicleConfiguration;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VehicleConfigurationDAO extends CommonDAO<VehicleConfiguration, Long> {
    public VehicleConfigurationDAO() {
        super(VehicleConfiguration.class);
    }

    public Collection<VehicleConfiguration> getConfigurationsByFilter(Filter filter) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM VehicleConfiguration v WHERE 1=1");
            Map<String, Object> params = new HashMap<>();

            processStringFilters(filter, hql, params);
            processNumericFilters(filter, hql, params);
            processBooleanFilters(filter, hql, params);
            processModelFilter(filter, hql, params);

            Query<VehicleConfiguration> query = session.createQuery(hql.toString(), VehicleConfiguration.class);
            params.forEach(query::setParameter);

            return query.getResultList();
        }
    }

    private void processStringFilters(Filter filter, StringBuilder hql, Map<String, Object> params) {
        if (filter.getName() != null) {
            hql.append(" AND v.name LIKE :name");
            params.put("name", "%" + filter.getName() + "%");
        }

        if (filter.getTransmissions() != null && !filter.getTransmissions().isEmpty()) {
            hql.append(" AND v.transmission IN :transmissions");
            params.put("transmissions", filter.getTransmissions());
        }
    }

    private void processNumericFilters(Filter filter, StringBuilder hql, Map<String, Object> params) {
        processIntegerFilter("enginePower",
                filter.getEnginePowerExact(),
                filter.getEnginePowerMin(),
                filter.getEnginePowerMax(),
                hql, params);

        processIntegerFilter("doorsCount",
                filter.getDoorsCountExact(),
                filter.getDoorsCountMin(),
                filter.getDoorsCountMax(),
                hql, params);

    }

    private void processIntegerFilter(String fieldName, Integer exact, Integer min, Integer max,
                                      StringBuilder hql, Map<String, Object> params) {
        if (exact != null) {
            hql.append(" AND v.").append(fieldName).append(" = :").append(fieldName);
            params.put(fieldName, exact);
        } else {
            if (min != null) {
                hql.append(" AND v.").append(fieldName).append(" >= :").append(fieldName).append("Min");
                params.put(fieldName + "Min", min);
            }
            if (max != null) {
                hql.append(" AND v.").append(fieldName).append(" <= :").append(fieldName).append("Max");
                params.put(fieldName + "Max", max);
            }
        }
    }

    private void processBooleanFilters(Filter filter, StringBuilder hql, Map<String, Object> params) {
        if (filter.getHasCruiseControl() != null) {
            hql.append(" AND v.hasCruiseControl = :hasCruiseControl");
            params.put("hasCruiseControl", filter.getHasCruiseControl());
        }

        if (filter.getIsOnSale() != null) {
            hql.append(" AND v.isOnSale = :isOnSale");
            params.put("isOnSale", filter.getIsOnSale());
        }
    }

    private void processModelFilter(Filter filter, StringBuilder hql, Map<String, Object> params) {
        if (filter.getModel() != null) {
            hql.append(" AND v.model = :model");
            params.put("model", filter.getModel());
        }
    }

    @Getter
    @Setter
    public static class Filter {
        private String name;
        private List<String> transmissions;

        private Integer enginePowerExact;
        private Integer enginePowerMin;
        private Integer enginePowerMax;

        private Integer doorsCountExact;
        private Integer doorsCountMin;
        private Integer doorsCountMax;

        private Boolean hasCruiseControl;
        private Boolean isOnSale;

        private Model model;
    }
}
