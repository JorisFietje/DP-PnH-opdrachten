import domain.Reiziger;
import globals.Hibernate;
import infra.dao.IReizigerDao;
import infra.hibernate.ReizigerHibernate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * P2H - Persistentie van een klasse met Hibernate.
 * Test elke CRUD-operatie van ReizigerHibernate op de tabel reiziger.
 */
public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager entityManager = null;
        try {
            emf = Persistence.createEntityManagerFactory(Hibernate.persistanceUnitName);
            entityManager = emf.createEntityManager();

            testReizigerDAOHibernate(new ReizigerHibernate(entityManager), entityManager);
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
        Reiziger sietske = new Reiziger(100, "S", "", "Boers", Date.valueOf("2003-03-14"));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        em.getTransaction().begin();
        rdao.save(sietske);
        em.getTransaction().commit();
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal de zojuist opgeslagen reiziger op via zijn id
        System.out.println("[Test] ReizigerDAO.findById(100) geeft: " + rdao.findById(100) + "\n");

        // Wijzig de achternaam en persisteer de wijziging
        sietske.setAchternaam("Boersma");
        em.getTransaction().begin();
        rdao.update(sietske);
        em.getTransaction().commit();
        System.out.println("[Test] Na ReizigerDAO.update() geeft findById(100): " + rdao.findById(100) + "\n");

        // Zoek alle reizigers met een bepaalde geboortedatum
        Date gbdatum = Date.valueOf("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbdatum(" + gbdatum + ") geeft de volgende reizigers:");
        for (Reiziger r : rdao.findByGbdatum(gbdatum)) {
            System.out.println(r);
        }
        System.out.println();

        // Verwijder de reiziger weer uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        em.getTransaction().begin();
        rdao.delete(sietske);
        em.getTransaction().commit();
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
}
