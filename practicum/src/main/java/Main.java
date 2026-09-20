import domain.Reiziger;
import globals.Hibernate;
import infra.dao.IReizigerDao;
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
 * P2H - Persistentie van een klasse met Hibernate.
 * Test elke CRUD-operatie van ReizigerDAOHibernate op de tabel reiziger.
 */
public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager entityManager = null;
        try {
            emf = Persistence.createEntityManagerFactory(Hibernate.persistanceUnitName);
            entityManager = emf.createEntityManager();

            testReizigerDAOHibernate(new ReizigerDaoHibernate(entityManager), entityManager);
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
}
