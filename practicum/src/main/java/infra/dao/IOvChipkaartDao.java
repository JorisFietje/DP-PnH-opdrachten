package infra.dao;

import domain.OvChipkaart;
import domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface IOvChipkaartDao {
    boolean save(OvChipkaart ovChipkaart) throws SQLException;
    boolean update(OvChipkaart ovChipkaart) throws SQLException;
    boolean delete(OvChipkaart ovChipkaart) throws SQLException;
    OvChipkaart findById(int id) throws SQLException;

    List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;

    List<OvChipkaart> findAll() throws SQLException;
}
