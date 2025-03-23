package ru.ispras.wtprac.dealership.DAO;

import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.TestDriveSchedule;

@Repository
public class TestDriveScheduleDAO extends CommonDAO<TestDriveSchedule, Long> {
    public TestDriveScheduleDAO() { super(TestDriveSchedule.class); }
}
