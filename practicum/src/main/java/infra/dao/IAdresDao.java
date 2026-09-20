package infra.dao;

import domain.Adres;
import domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface IAdresDao {
    boolean save(Adres adres) throws SQLException;

    boolean update(Adres adres) throws SQLException;

    boolean delete(Adres adres) throws SQLException;

    Adres findById(int id) throws SQLException;

    Adres findByReiziger(Reiziger reiziger) throws SQLException;

    List<Adres> findAll() throws SQLException;
}
