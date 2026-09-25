package domain;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "product_nummer")
    private int productNummer;
    @Column(name = "naam")
    private String naam;
    @Column(name = "beschrijving")
    private String beschrijving;
    @Column(name = "prijs")
    private double prijs;

    @ManyToMany(mappedBy = "producten",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<OvChipkaart> ovChipKaarten = new ArrayList<>();

    public Product() {
    }

    public Product(int productNummer, String naam, String beschrijving, double prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OvChipkaart> getOvChipKaarten() {
        return ovChipKaarten;
    }

    public void setOvChipKaarten(List<OvChipkaart> ovChipKaarten) {
        this.ovChipKaarten = ovChipKaarten;
    }

    /**
     * Koppelt een OV-chipkaart aan dit product en zet het product ook op de kaart,
     * zodat beide kanten van de veel-op-veel-relatie kloppen.
     *
     * @return false als de kaart leeg is of al gekoppeld was
     */
    public boolean voegToeOVChipkaart(OvChipkaart ovChipkaart) {
        if (ovChipkaart == null || ovChipKaarten.contains(ovChipkaart)) {
            return false;
        }
        ovChipKaarten.add(ovChipkaart);
        if (!ovChipkaart.getProducten().contains(this)) {
            ovChipkaart.getProducten().add(this);
        }
        return true;
    }

    /**
     * Maakt een OV-chipkaart los van dit product, aan beide kanten.
     *
     * @return false als de kaart niet aan dit product hing
     */
    public boolean verwijderOVChipkaart(OvChipkaart ovChipkaart) {
        if (ovChipkaart == null || !ovChipKaarten.remove(ovChipkaart)) {
            return false;
        }
        ovChipkaart.getProducten().remove(this);
        return true;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Product {#%d %s, %.2f, op %d kaart(en)}",
                productNummer, naam, prijs, ovChipKaarten.size());
    }
}
