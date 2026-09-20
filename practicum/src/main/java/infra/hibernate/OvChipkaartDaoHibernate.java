package infra.hibernate;

import infra.dao.IOvChipkaartDao;
import domain.OvChipkaart;
import domain.Reiziger;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class OvChipkaartDaoHibernate implements IOvChipkaartDao {

    public OvChipkaartDaoHibernate(EntityManager entityManager) {

    }


    @Override
    public boolean save(OvChipkaart ovChipkaart) throws SQLException {
        return false;
    }

    @Override
    public boolean update(OvChipkaart ovChipkaart) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(OvChipkaart ovChipkaart) throws SQLException {
        return false;
    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        return null;
    }

    @Override
    public List<OvChipkaart> findAll() throws SQLException {
        return null;
    }
}
