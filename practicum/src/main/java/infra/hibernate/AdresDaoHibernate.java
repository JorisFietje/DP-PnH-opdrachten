package infra.hibernate;

import infra.dao.IAdresDao;
import domain.Adres;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class AdresDaoHibernate implements IAdresDao {

    public AdresDaoHibernate(EntityManager entityManager) {

    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public Adres findById(int id) throws SQLException {
        return null;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        return null;
    }
}
