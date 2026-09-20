package infra.hibernate;

import domain.Adres;
import domain.Reiziger;
import infra.dao.IAdresDao;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AdresDaoHibernate implements IAdresDao {

    private final EntityManager entityManager;

    public AdresDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean save(Adres adres) {
        entityManager.persist(adres);
        return true;
    }

    @Override
    public boolean update(Adres adres) {
        entityManager.merge(adres);
        return true;
    }

    @Override
    public boolean delete(Adres adres) {
        entityManager.remove(adres);
        return true;
    }

    @Override
    public Adres findById(int id) {
        return entityManager.find(Adres.class, id);
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        List<Adres> adressen = entityManager
                .createQuery("SELECT a FROM Adres a WHERE a.reiziger = :reiziger", Adres.class)
                .setParameter("reiziger", reiziger)
                .getResultList();

        return adressen.isEmpty() ? null : adressen.get(0);
    }

    @Override
    public List<Adres> findAll() {
        return entityManager
                .createQuery("SELECT a FROM Adres a", Adres.class)
                .getResultList();
    }
}
