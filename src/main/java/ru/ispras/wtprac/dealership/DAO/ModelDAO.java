package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.Model;

@Repository
public class ModelDAO extends CommonDAO<Model, Long> {
    public ModelDAO() { super(Model.class); }
}
