package domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "ov_chipkaart")
public class OvChipkaart {

    @Id
    @Column(name = "kaart_nummer")
    private int kaartNummer;

    @Column(name = "geldig_tot")
    private Date geldigTot;

    @Column(name = "klasse")
    private int klasse;

    @Column(name = "saldo")
    private double saldo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reiziger_id", nullable = false)
    private Reiziger reiziger;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "kaart_nummer"),
            inverseJoinColumns = @JoinColumn(name = "product_nummer")
    )
    private List<Product> producten = new ArrayList<>();

    public OvChipkaart() {
    }

    public OvChipkaart(int kaartNummer, Date geldigTot, int klasse, double saldo) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }


    /**
     * Koppelt een product aan deze kaart, aan beide kanten van de relatie.
     *
     * @return false als het product leeg is of al gekoppeld was
     */
    public boolean voegToeProduct(Product product) {
        return product != null && product.voegToeOVChipkaart(this);
    }

    /**
     * Maakt een product los van deze kaart, aan beide kanten.
     *
     * @return false als het product niet aan deze kaart hing
     */
    public boolean verwijderProduct(Product product) {
        return product != null && product.verwijderOVChipkaart(this);
    }

    /** Korte weergave van de producten op deze kaart, voor toString(). */
    private String productenAlsTekst() {
        if (producten == null || producten.isEmpty()) {
            return "geen producten";
        }
        StringBuilder sb = new StringBuilder(producten.size() + " producten: ");
        for (int i = 0; i < producten.size(); i++) {
            sb.append(i > 0 ? ", " : "").append(producten.get(i).getNaam());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String reizigerInfo = (reiziger == null)
                ? "geen reiziger"
                : "reiziger #" + reiziger.getReizigerId();
        return String.format(Locale.ROOT, "OvChipkaart {#%d, klasse %d, saldo %.2f, geldig tot %s, %s, %s}",
                kaartNummer, klasse, saldo, geldigTot, reizigerInfo, productenAlsTekst());
    }
}