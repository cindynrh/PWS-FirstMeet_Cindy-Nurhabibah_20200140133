/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercise1.Exercise1.C;

import Exercise1.Exercise1.C.exceptions.NonexistentEntityException;
import Exercise1.Exercise1.C.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author user
 */
public class ProdukJpaController implements Serializable {

    public ProdukJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Exercise1_Exercise1.C_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ProdukJpaController() {
    }
    
    

    public void create(Produk produk) throws PreexistingEntityException, Exception {
        if (produk.getProdukPK() == null) {
            produk.setProdukPK(new ProdukPK());
        }
        produk.getProdukPK().setIdTransaksi(produk.getIdTransaksi1().getIdTransaksi());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IdTransaksi idTransaksi1 = produk.getIdTransaksi1();
            if (idTransaksi1 != null) {
                idTransaksi1 = em.getReference(idTransaksi1.getClass(), idTransaksi1.getIdTransaksi());
                produk.setIdTransaksi1(idTransaksi1);
            }
            em.persist(produk);
            if (idTransaksi1 != null) {
                idTransaksi1.getProdukCollection().add(produk);
                idTransaksi1 = em.merge(idTransaksi1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProduk(produk.getProdukPK()) != null) {
                throw new PreexistingEntityException("Produk " + produk + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produk produk) throws NonexistentEntityException, Exception {
        produk.getProdukPK().setIdTransaksi(produk.getIdTransaksi1().getIdTransaksi());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produk persistentProduk = em.find(Produk.class, produk.getProdukPK());
            IdTransaksi idTransaksi1Old = persistentProduk.getIdTransaksi1();
            IdTransaksi idTransaksi1New = produk.getIdTransaksi1();
            if (idTransaksi1New != null) {
                idTransaksi1New = em.getReference(idTransaksi1New.getClass(), idTransaksi1New.getIdTransaksi());
                produk.setIdTransaksi1(idTransaksi1New);
            }
            produk = em.merge(produk);
            if (idTransaksi1Old != null && !idTransaksi1Old.equals(idTransaksi1New)) {
                idTransaksi1Old.getProdukCollection().remove(produk);
                idTransaksi1Old = em.merge(idTransaksi1Old);
            }
            if (idTransaksi1New != null && !idTransaksi1New.equals(idTransaksi1Old)) {
                idTransaksi1New.getProdukCollection().add(produk);
                idTransaksi1New = em.merge(idTransaksi1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProdukPK id = produk.getProdukPK();
                if (findProduk(id) == null) {
                    throw new NonexistentEntityException("The produk with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProdukPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produk produk;
            try {
                produk = em.getReference(Produk.class, id);
                produk.getProdukPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produk with id " + id + " no longer exists.", enfe);
            }
            IdTransaksi idTransaksi1 = produk.getIdTransaksi1();
            if (idTransaksi1 != null) {
                idTransaksi1.getProdukCollection().remove(produk);
                idTransaksi1 = em.merge(idTransaksi1);
            }
            em.remove(produk);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produk> findProdukEntities() {
        return findProdukEntities(true, -1, -1);
    }

    public List<Produk> findProdukEntities(int maxResults, int firstResult) {
        return findProdukEntities(false, maxResults, firstResult);
    }

    private List<Produk> findProdukEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produk.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Produk findProduk(ProdukPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produk.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdukCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produk> rt = cq.from(Produk.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
