package ru.ispras.wtprac.dealership.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.ispras.wtprac.dealership.model.IEntity;

import javax.persistence.Table;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Collection;

@Repository
public abstract class CommonDAO<T extends IEntity<Id>, Id> {
    protected Class<T> entityClass;
    protected SessionFactory sessionFactory;

    public CommonDAO(Class<T> entityClass) { this.entityClass = entityClass; }

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean sessionFactory) {
        this.sessionFactory = sessionFactory.getObject();
    }

    public T getById(Id id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityClass, (Serializable) id);
        }
    }

    public Collection<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(entityClass);
            criteria.from(entityClass);
            return session.createQuery(criteria).getResultList();
        }
    }

    public void saveOne(T entity) {
        try (Session session = sessionFactory.openSession()) {
            if (entity.getId() == null) {
                entity.setId(null);
            }
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    public void saveCollection(Collection<T> entities) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (T entity : entities) {
                if (entity.getId() == null) {
                    entity.setId(null);
                }
                session.save(entity);
            }
            session.getTransaction().commit();
        }
    }

    public void updateOne(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    public void deleteOne(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    public void deleteById(Id id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T entity = getById(id);
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    public void deleteAllEntries() {
        String tableName = entityClass.getAnnotation(Table.class).name();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(String.format("truncate %s restart identity cascade;", tableName))
                    .executeUpdate();
            session.createSQLQuery(String.format("alter sequence %s_id_seq restart with 1;", tableName))
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}
