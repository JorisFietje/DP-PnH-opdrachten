package p4OneToManyTests;

import domain.OvChipkaart;
import domain.Reiziger;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class p4OvChipkaartTest {

    @Test
    void testKaartNummer() {
        OvChipkaart ovChipkaart = new OvChipkaart();
        ovChipkaart.setKaartNummer(4);

        assertEquals(ovChipkaart.getKaartNummer(), 4);
    }

    @Test
    void testGeldigTot() {
        OvChipkaart ovChipkaart = new OvChipkaart();
        ovChipkaart.setGeldigTot(Date.valueOf("2022-01-28"));

        assertEquals(Date.valueOf("2022-01-28"), ovChipkaart.getGeldigTot());
    }

    @Test
    void testKlasse() {
        OvChipkaart ovChipkaart = new OvChipkaart();
        ovChipkaart.setKlasse(2);

        assertEquals(2, ovChipkaart.getKlasse());
    }

    @Test
    void testSaldo() {
        OvChipkaart ovChipkaart = new OvChipkaart();
        ovChipkaart.setSaldo(5.0);

        assertEquals(5.0, ovChipkaart.getSaldo());
    }

    @Test
    void testReiziger() {
        OvChipkaart ovChipkaart = new OvChipkaart();
        Reiziger reiziger = new Reiziger();
        ovChipkaart.setReiziger(reiziger);

        assertEquals(reiziger, ovChipkaart.getReiziger());
    }
}
