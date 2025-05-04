package ru.ispras.wtprac.dealership.DAO;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public class TestDriveScheduleDAO extends CommonDAO<TestDriveSchedule, Long> {
    public TestDriveScheduleDAO() {
        super(TestDriveSchedule.class);
    }

    public Collection<Car> getCarsOnTestDriveByDateTime(LocalDateTime dateTime) {
        return getCarsOnTestDriveByDateTime(dateTime, false);
    }

    public Collection<Car> getCarsOnTestDriveByDateTime(LocalDateTime dateTime, boolean showCancelled) {
        Collection<Car> result;
        try (Session session = sessionFactory.openSession()) {
            Query<Car> query = session.createQuery(
                    "select t.car from TestDriveSchedule t where t.dateTimeStart <= :dateTime" +
                            " and t.dateTimeEnd >= :dateTime" +
                            (showCancelled ? "" : " and t.testDriveStatus != :testDriveStatus"),
                    Car.class).setParameter("dateTime", dateTime);
            if (!showCancelled) {
                query.setParameter("testDriveStatus", TestDriveStatus.Canceled);
            }
            result = query.getResultList();
        }
        return result;
    }

    public Collection<TestDriveSchedule> getAllTestDrivesOfClient(Client client) {
        Collection<TestDriveSchedule> result;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("from TestDriveSchedule where client = :client", TestDriveSchedule.class)
                    .setParameter("client", client)
                    .list().stream().toList();
        }
        return result;
    }
}
