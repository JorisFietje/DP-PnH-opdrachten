import domain.Adres;
import domain.OvChipkaart;
import domain.Product;
import domain.Reiziger;
import globals.Hibernate;
import infra.dao.IAdresDao;
import infra.dao.IOvChipkaartDao;
import infra.dao.IProductDao;
import infra.dao.IReizigerDao;
import infra.hibernate.AdresDaoHibernate;
import infra.hibernate.OvChipkaartDaoHibernate;
import infra.hibernate.ProductDaoHibernate;
import infra.hibernate.ReizigerDaoHibernate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * P2H, P3H en P4H - Persistentie met Hibernate.
 * Test de CRUD-operaties van ReizigerDAOHibernate, AdresDAOHibernate en
 * OVChipkaartDAOHibernate.
 */
public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager entityManager = null;
        try {
            emf = Persistence.createEntityManagerFactory(Hibernate.persistanceUnitName);
            entityManager = emf.createEntityManager();

            IReizigerDao rdao = new ReizigerDaoHibernate(entityManager);
            IAdresDao adao = new AdresDaoHibernate(entityManager);
            IOvChipkaartDao odao = new OvChipkaartDaoHibernate(entityManager);
            IProductDao pdao = new ProductDaoHibernate(entityManager);

            testReizigerDAOHibernate(rdao, entityManager);
            testAdresDAO(adao, rdao, entityManager);
            testOVChipkaartDAO(odao, rdao, entityManager);
            testProductDAO(pdao, odao, entityManager);
        } catch (PersistenceException e) {
            System.err.println("Hibernate kon de bewerking niet uitvoeren: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Benaderen van de database is mislukt: " + e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }

    /** Een schrijfactie op de DAO, die zowel een SQLException als een Hibernate-fout kan opleveren. */
    @FunctionalInterface
    private interface DaoActie {
        boolean uitvoeren() throws SQLException;
    }

    private static boolean inTransactie(EntityManager em, DaoActie actie) throws SQLException {
        EntityTransaction transactie = em.getTransaction();
        transactie.begin();
        try {
            boolean gelukt = actie.uitvoeren();
            transactie.commit();
            return gelukt;
        } catch (SQLException | RuntimeException e) {
            if (transactie.isActive()) {
                transactie.rollback();
            }
            throw e;
        }
    }

    /**
     * Test de methoden van ReizigerDAO in de Hibernate-implementatie.
     */
    private static void testReizigerDAOHibernate(IReizigerDao rdao, EntityManager em) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO (Hibernate) -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Persisteer een nieuwe reiziger
        Reiziger sietske = new Reiziger(100, "S", "", "Boers", Date.valueOf("1981-03-14"));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        boolean opgeslagen = inTransactie(em, () -> rdao.save(sietske));
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers (save gaf " + opgeslagen + " terug)\n");

        // Haal de zojuist opgeslagen reiziger op via zijn id
        System.out.println("[Test] ReizigerDAO.findById(100) geeft: " + rdao.findById(100) + "\n");

        // Wijzig de achternaam en persisteer de wijziging
        sietske.setAchternaam("Boersma");
        boolean gewijzigd = inTransactie(em, () -> rdao.update(sietske));
        System.out.println("[Test] Na ReizigerDAO.update() (gaf " + gewijzigd + " terug) geeft findById(100): "
                + rdao.findById(100) + "\n");

        // Zoek alle reizigers met een bepaalde geboortedatum
        Date gbdatum = Date.valueOf("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbdatum(" + gbdatum + ") geeft de volgende reizigers:");
        for (Reiziger r : rdao.findByGbdatum(gbdatum)) {
            System.out.println(r);
        }
        System.out.println();

        // Verwijder de reiziger weer uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        boolean verwijderd = inTransactie(em, () -> rdao.delete(sietske));
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers (delete gaf " + verwijderd + " terug)\n");
    }

    /**
     * P3H - Test de CRUD-operaties van AdresDAO.
     */
    private static void testAdresDAO(IAdresDao adao, IReizigerDao rdao, EntityManager em) throws SQLException {
        System.out.println("\n---------- Test AdresDAO (Hibernate) -------------");

        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Persisteer een nieuw adres.
        Reiziger albrechts = rdao.findById(6);
        Adres nieuwAdres = new Adres(101, "1234AB", "12", "Teststraat", "Utrecht");
        nieuwAdres.setReiziger(albrechts);
        albrechts.setAdres(nieuwAdres);

        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        boolean opgeslagen = inTransactie(em, () -> adao.save(nieuwAdres));
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen (save gaf " + opgeslagen + " terug)\n");

        // Haal het zojuist opgeslagen adres op via zijn id
        System.out.println("[Test] AdresDAO.findById(101) geeft: " + adao.findById(101) + "\n");

        // Wijzig het adres en persisteer de wijziging
        nieuwAdres.setStraat("Arnhemseweg");
        nieuwAdres.setWoonplaats("Amersfoort");
        nieuwAdres.setPostcode("3817CH");
        boolean gewijzigd = inTransactie(em, () -> adao.update(nieuwAdres));
        System.out.println("[Test] AdresDAO.update() (gaf " + gewijzigd + " terug) geeft findById(101): "
                + adao.findById(101) + "\n");

        // Zoek het adres op via de reiziger die eraan hangt
        System.out.println("[Test] AdresDAO.findByReiziger(" + albrechts.getNaam() + ") geeft: "
                + adao.findByReiziger(albrechts) + "\n");

        // Verwijder het adres weer.
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        boolean adresVerwijderd = inTransactie(em, () -> {
            albrechts.setAdres(null);
            return adao.delete(nieuwAdres);
        });
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen (delete gaf " + adresVerwijderd + " terug)\n");
    }

    /**
     * P4H - Test de CRUD-operaties van OVChipkaartDAO, inclusief findByReiziger().
     */
    private static void testOVChipkaartDAO(IOvChipkaartDao odao, IReizigerDao rdao, EntityManager em)
            throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO (Hibernate) -------------");

        List<OvChipkaart> kaarten = odao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft " + kaarten.size() + " kaarten, waarvan de eerste vijf:");
        kaarten.stream().limit(5).forEach(System.out::println);
        System.out.println();

        // Reiziger 6 heeft nog geen kaart.
        Reiziger albrechts = rdao.findById(6);
        OvChipkaart nieuweKaart = new OvChipkaart(
                99001, Date.valueOf("2027-01-31"), 2, 15.00);
        System.out.println("[Test] Reiziger.voegToeOVChipkaart() gaf " + albrechts.voegToeOVChipkaart(nieuweKaart) + " terug");

        System.out.print("[Test] Eerst " + kaarten.size() + " kaarten, na OVChipkaartDAO.save() ");
        boolean opgeslagen = inTransactie(em, () -> odao.save(nieuweKaart));
        kaarten = odao.findAll();
        System.out.println(kaarten.size() + " kaarten (save gaf " + opgeslagen + " terug)\n");

        System.out.println("[Test] OVChipkaartDAO.findById(99001) geeft: " + odao.findById(99001) + "\n");

        nieuweKaart.setSaldo(42.50);
        boolean gewijzigd = inTransactie(em, () -> odao.update(nieuweKaart));
        System.out.println("[Test] Na OVChipkaartDAO.update() (gaf " + gewijzigd + " terug) geeft findById(99001): "
                + odao.findById(99001) + "\n");

        System.out.println("[Test] OVChipkaartDAO.findByReiziger(" + albrechts.getNaam() + ") geeft:");
        for (OvChipkaart k : odao.findByReiziger(albrechts)) {
            System.out.println(k);
        }
        System.out.println();

        System.out.println("[Test] Reiziger.verwijderOVChipkaart() gaf " + albrechts.verwijderOVChipkaart(nieuweKaart) + " terug");
        System.out.print("[Test] Eerst " + kaarten.size() + " kaarten, na OVChipkaartDAO.delete() ");
        boolean verwijderd = inTransactie(em, () -> {
                        return odao.delete(nieuweKaart);
        });
        kaarten = odao.findAll();
        System.out.println(kaarten.size() + " kaarten (delete gaf " + verwijderd + " terug)\n");
    }

    /**
     * P5H - Test de CRUD-operaties van ProductDAO, inclusief findByOvChipkaart().
     */
    private static void testProductDAO(IProductDao pdao, IOvChipkaartDao odao, EntityManager em)
            throws SQLException {
        System.out.println("\n---------- Test ProductDAO (Hibernate) -------------");

        List<Product> producten = pdao.findAll();
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");
        producten.forEach(System.out::println);
        System.out.println();

        // Een nieuw product, gekoppeld aan een bestaande kaart.
        OvChipkaart kaart = odao.findById(35283);
        Product nieuwProduct = new Product(101, "Weekendretour", "Onbeperkt reizen in het weekend", 29.95);

        System.out.println("[Test] OVChipkaart.voegToeProduct() gaf " + kaart.voegToeProduct(nieuwProduct) + " terug");
        System.out.print("[Test] Eerst " + producten.size() + " producten, na ProductDAO.save() ");
        boolean opgeslagen = inTransactie(em, () -> pdao.save(nieuwProduct));
        producten = pdao.findAll();
        System.out.println(producten.size() + " producten (save gaf " + opgeslagen + " terug)\n");

        System.out.println("[Test] ProductDAO.findById(101) geeft: " + pdao.findById(101) + "\n");

        nieuwProduct.setPrijs(34.95);
        boolean gewijzigd = inTransactie(em, () -> pdao.update(nieuwProduct));
        System.out.println("[Test] ProductDAO.update() (gaf " + gewijzigd + " terug) geeft findById(101): "
                + pdao.findById(101) + "\n");

        System.out.println("[Test] ProductDAO.findByOvChipkaart(#35283) geeft:");
        pdao.findByOvChipkaart(kaart).forEach(System.out::println);
        System.out.println();

        System.out.println("[Test] OVChipkaart.verwijderProduct() gaf " + kaart.verwijderProduct(nieuwProduct) + " terug");
        System.out.print("[Test] Eerst " + producten.size() + " producten, na ProductDAO.delete() ");
        boolean verwijderd = inTransactie(em, () -> {
                        return pdao.delete(nieuwProduct);
        });
        producten = pdao.findAll();
        System.out.println(producten.size() + " producten (delete gaf " + verwijderd + " terug)\n");
    }
}
