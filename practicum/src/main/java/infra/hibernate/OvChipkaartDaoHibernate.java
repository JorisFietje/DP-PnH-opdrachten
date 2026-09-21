package infra.hibernate;

import infra.dao.IOvChipkaartDao;
import domain.OvChipkaart;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OvChipkaartDaoHibernate implements IOvChipkaartDao {

    private final EntityManager entityManager;

    public OvChipkaartDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public boolean save(OvChipkaart ovChipkaart) {
        entityManager.persist(ovChipkaart);
        return true;
    }

    @Override
    public boolean update(OvChipkaart ovChipkaart) {
        entityManager.merge(ovChipkaart);
        return true;
    }

    @Override
    public boolean delete(OvChipkaart ovChipkaart) {
        entityManager.remove(ovChipkaart);
        return true;
    }

    @Override
    public OvChipkaart findById(int id) {
        return entityManager.find(OvChipkaart.class, id);
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) {
        return entityManager
                .createQuery("SELECT o FROM OvChipkaart o WHERE o.reiziger = :reiziger", OvChipkaart.class)
                .setParameter("reiziger", reiziger)
                .getResultList();
    }

    @Override
    public List<OvChipkaart> findAll() {
        return entityManager
                .createQuery("SELECT o FROM OvChipkaart o", OvChipkaart.class)
                .getResultList();
    }
}