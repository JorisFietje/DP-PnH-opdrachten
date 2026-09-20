package infra.hibernate;

import infra.dao.IReizigerDao;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.sql.Date;
import java.util.List;

public class ReizigerDaoHibernate implements IReizigerDao {

    private final EntityManager entityManager;

    public ReizigerDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        entityManager.persist(reiziger);
        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        entityManager.merge(reiziger);
        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        entityManager.remove(reiziger);
        return true;
    }

    @Override
    public Reiziger findById(int id) {
        return entityManager.find(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) {
        return entityManager
                .createQuery("SELECT r FROM Reiziger r WHERE r.geboortedatum = :datum", Reiziger.class)
                .setParameter("datum", date)
                .getResultList();
    }

    @Override
    public List<Reiziger> findAll() {
        return entityManager
                .createQuery("SELECT r FROM Reiziger r", Reiziger.class)
                .getResultList();
    }
}
