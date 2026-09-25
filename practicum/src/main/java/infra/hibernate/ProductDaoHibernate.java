package infra.hibernate;

import infra.dao.IProductDao;
import domain.OvChipkaart;
import domain.Product;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductDaoHibernate implements IProductDao {

    private final EntityManager entityManager;

    public ProductDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public boolean save(Product product) {
        for (OvChipkaart ovChipkaart : product.getOvChipKaarten()) {
            if (!ovChipkaart.getProducten().contains(product)) {
                ovChipkaart.getProducten().add(product);
            }
        }
        entityManager.persist(product);
        return true;
    }

    @Override
    public boolean update(Product product) {
        for (OvChipkaart ovChipkaart : product.getOvChipKaarten()) {
            if (!ovChipkaart.getProducten().contains(product)) {
                ovChipkaart.getProducten().add(product);
            }
        }
        entityManager.merge(product);
        return true;
    }

    @Override
    public boolean delete(Product product) {
        for (OvChipkaart ovChipkaart : product.getOvChipKaarten()) {
            ovChipkaart.getProducten().remove(product);
        }
        product.getOvChipKaarten().clear();

        entityManager.remove(product);
        return true;
    }

    @Override
    public Product findById(int id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) {
        return entityManager
                .createQuery("SELECT p FROM Product p JOIN p.ovChipKaarten o WHERE o = :ovChipkaart",
                        Product.class)
                .setParameter("ovChipkaart", ovChipkaart)
                .getResultList();
    }

    @Override
    public List<Product> findAll() {
        return entityManager
                .createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }
}