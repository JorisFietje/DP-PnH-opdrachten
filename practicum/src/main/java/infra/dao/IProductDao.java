package infra.dao;

import domain.OvChipkaart;
import domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDao {
    boolean save(Product product) throws SQLException;

    boolean update(Product product) throws SQLException;

    boolean delete(Product product) throws SQLException;

    Product findById(int id) throws SQLException;

    List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException;

    List<Product> findAll() throws SQLException;
}
