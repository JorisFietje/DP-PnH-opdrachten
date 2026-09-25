package p4OneToManyTests;

import domain.*;
import infra.dao.*;
import globals.Hibernate;
import infra.hibernate.OvChipkaartDaoHibernate;
import infra.hibernate.ReizigerDaoHibernate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import util.SetupDatabase;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class p4hTestOvChipkaartDao {
    private static SetupDatabase setupDatabase;
    private IOvChipkaartDao ovChipkaartDao;
    private IReizigerDao reizigerDao;

    private EntityManager entityManager;


    @BeforeAll
    public static void beforeAll() throws SQLException, FileNotFoundException {
        setupDatabase = new SetupDatabase(Hibernate.dbName);

        setupDatabase.initializeDb();
    }

    @BeforeEach
    public void beforeEach() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(Hibernate.persistanceUnitName);
        entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        ovChipkaartDao = new OvChipkaartDaoHibernate(entityManager);
        reizigerDao = new ReizigerDaoHibernate(entityManager);
    }

    @AfterEach
    public void afterEach() throws SQLException {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().commit();
        }
    }

    @AfterAll
    public static void after() throws SQLException {
        setupDatabase.getConnection().endRequest();
        setupDatabase.getConnection().close();
        setupDatabase.clean();
    }

    @Test
    public void testOvChipkaartDaoInitialState() throws SQLException {
        List<OvChipkaart> ovChipkaarten = ovChipkaartDao.findAll();
        OvChipkaart kaart = ovChipkaarten.stream().filter(x -> x.getKaartNummer() == 46392).findAny().get();

        assertAll(
                () -> assertTrue(ovChipkaarten.size() > 1),
                () -> assertEquals(46392, kaart.getKaartNummer()),
                () -> assertEquals(2, kaart.getKlasse()),
                () -> assertEquals(5.50, kaart.getSaldo()),
                () -> assertEquals(Date.valueOf("2017-05-31"), kaart.getGeldigTot()),
                () -> assertEquals(2, kaart.getReiziger().getReizigerId())
        );
    }

    @Test
    public void testOvChipkaartDaoFind() throws SQLException {
        OvChipkaart ovChipkaart = ovChipkaartDao.findById(68514);

        assertAll(
                () -> assertEquals(68514, ovChipkaart.getKaartNummer()),
                () -> assertEquals(1, ovChipkaart.getKlasse()),
                () -> assertEquals(2.50, ovChipkaart.getSaldo()),
                () -> assertEquals(Date.valueOf("2020-03-31"), ovChipkaart.getGeldigTot()),
                () -> assertEquals(3, ovChipkaart.getReiziger().getReizigerId())
        );
    }

    @Test
    public void testOvChipkaartDaoFindByReiziger() throws SQLException {
        Reiziger reiziger = reizigerDao.findById(2);
        List<OvChipkaart> ovChipkaarten = ovChipkaartDao.findByReiziger(reiziger);
        OvChipkaart ovChipkaart = ovChipkaarten.stream().filter(x -> x.getKaartNummer() == 35283).findAny().get();

        assertAll(
                () -> assertEquals(3, ovChipkaarten.size()),
                () -> assertEquals(35283, ovChipkaart.getKaartNummer()),
                () -> assertEquals(2, ovChipkaart.getKlasse()),
                () -> assertEquals(25.50, ovChipkaart.getSaldo()),
                () -> assertEquals(Date.valueOf("2018-05-31"), ovChipkaart.getGeldigTot()),
                () -> assertEquals(2, ovChipkaart.getReiziger().getReizigerId())
        );
    }

    @Test
    public void testOvChipkaartDoesntExist() throws SQLException {
        OvChipkaart ovChipkaart = ovChipkaartDao.findById(10);
        assertNull(ovChipkaart);
    }

    @Test
    public void testUpdateAttachedOvChipkaart() throws SQLException {
        OvChipkaart ovChipkaart = ovChipkaartDao.findById(79625);

        assertAll(
                () -> assertEquals(79625, ovChipkaart.getKaartNummer()),
                () -> assertEquals(1, ovChipkaart.getKlasse()),
                () -> assertEquals(25.50, ovChipkaart.getSaldo()),
                () -> assertEquals(Date.valueOf("2020-01-31"), ovChipkaart.getGeldigTot()),
                () -> assertEquals(4, ovChipkaart.getReiziger().getReizigerId())
        );

        ovChipkaart.setKlasse(2);
        ovChipkaart.setSaldo(12.80);
        ovChipkaart.setGeldigTot(Date.valueOf("2010-03-03"));

        ovChipkaartDao.update(ovChipkaart);

        OvChipkaart updatedOvChipkaart = ovChipkaartDao.findById(79625);

        assertAll(
                () -> assertEquals(79625, updatedOvChipkaart.getKaartNummer()),
                () -> assertEquals(2, updatedOvChipkaart.getKlasse()),
                () -> assertEquals(12.80, updatedOvChipkaart.getSaldo()),
                () -> assertEquals(Date.valueOf("2010-03-03"), updatedOvChipkaart.getGeldigTot())
        );
    }

    @Test
    public void testDeleteOvChipkaart() throws SQLException {
        OvChipkaart ovChipkaart = ovChipkaartDao.findById(65427);

        assertAll(
                () -> assertEquals(65427, ovChipkaart.getKaartNummer()),
                () -> assertEquals(2, ovChipkaart.getKlasse()),
                () -> assertEquals(0.00, ovChipkaart.getSaldo()),
                () -> assertEquals(Date.valueOf("2017-12-31"), ovChipkaart.getGeldigTot()),
                () -> assertEquals(77, ovChipkaart.getReiziger().getReizigerId())
        );

        ovChipkaartDao.delete(ovChipkaart);

        OvChipkaart deletedOvChipkaart = ovChipkaartDao.findById(65427);

        assertAll(
                () -> assertNull(deletedOvChipkaart)
        );
    }
}
