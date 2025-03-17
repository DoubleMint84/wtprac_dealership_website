package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.Model;

public class ModelDAO extends CommonDAO<Model, Long> {
    public ModelDAO(Class<Model> entityClass) {
        super(entityClass);
    }
}
