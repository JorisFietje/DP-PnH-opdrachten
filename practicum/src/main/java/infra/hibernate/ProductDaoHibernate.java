package infra.hibernate;

import infra.dao.IProductDao;
import domain.OvChipkaart;
import domain.Product;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class ProductDaoHibernate implements IProductDao {

    private EntityManager entityManager;

    public ProductDaoHibernate(EntityManager entityManager) {

    }


    @Override
    public boolean save(Product product) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        return false;
    }

    @Override
    public Product findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException {
        return null;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return null;
    }
}
