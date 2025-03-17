package ru.ispras.wtprac.dealership.DAO;

import ru.ispras.wtprac.dealership.model.TestDriveSchedule;

public class TestDriveScheduleDAO extends CommonDAO<TestDriveSchedule, Long> {
    public TestDriveScheduleDAO(Class<TestDriveSchedule> entityClass) {
        super(entityClass);
    }
}
