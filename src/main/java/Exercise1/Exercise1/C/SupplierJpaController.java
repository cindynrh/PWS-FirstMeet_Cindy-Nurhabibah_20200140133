/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercise1.Exercise1.C;

import Exercise1.Exercise1.C.exceptions.IllegalOrphanException;
import Exercise1.Exercise1.C.exceptions.NonexistentEntityException;
import Exercise1.Exercise1.C.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author user
 */
public class SupplierJpaController implements Serializable {

    public SupplierJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Exercise1_Exercise1.C_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public SupplierJpaController() {
    }
    
    

    public void create(Supplier supplier) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IdTransaksi idTransaksi = supplier.getIdTransaksi();
            if (idTransaksi != null) {
                idTransaksi = em.getReference(idTransaksi.getClass(), idTransaksi.getIdTransaksi());
                supplier.setIdTransaksi(idTransaksi);
            }
            em.persist(supplier);
            if (idTransaksi != null) {
                Supplier oldIdOwnerOfIdTransaksi = idTransaksi.getIdOwner();
                if (oldIdOwnerOfIdTransaksi != null) {
                    oldIdOwnerOfIdTransaksi.setIdTransaksi(null);
                    oldIdOwnerOfIdTransaksi = em.merge(oldIdOwnerOfIdTransaksi);
                }
                idTransaksi.setIdOwner(supplier);
                idTransaksi = em.merge(idTransaksi);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSupplier(supplier.getIdSupplier()) != null) {
                throw new PreexistingEntityException("Supplier " + supplier + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Supplier supplier) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier persistentSupplier = em.find(Supplier.class, supplier.getIdSupplier());
            IdTransaksi idTransaksiOld = persistentSupplier.getIdTransaksi();
            IdTransaksi idTransaksiNew = supplier.getIdTransaksi();
            List<String> illegalOrphanMessages = null;
            if (idTransaksiOld != null && !idTransaksiOld.equals(idTransaksiNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain IdTransaksi " + idTransaksiOld + " since its idOwner field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTransaksiNew != null) {
                idTransaksiNew = em.getReference(idTransaksiNew.getClass(), idTransaksiNew.getIdTransaksi());
                supplier.setIdTransaksi(idTransaksiNew);
            }
            supplier = em.merge(supplier);
            if (idTransaksiNew != null && !idTransaksiNew.equals(idTransaksiOld)) {
                Supplier oldIdOwnerOfIdTransaksi = idTransaksiNew.getIdOwner();
                if (oldIdOwnerOfIdTransaksi != null) {
                    oldIdOwnerOfIdTransaksi.setIdTransaksi(null);
                    oldIdOwnerOfIdTransaksi = em.merge(oldIdOwnerOfIdTransaksi);
                }
                idTransaksiNew.setIdOwner(supplier);
                idTransaksiNew = em.merge(idTransaksiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = supplier.getIdSupplier();
                if (findSupplier(id) == null) {
                    throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier supplier;
            try {
                supplier = em.getReference(Supplier.class, id);
                supplier.getIdSupplier();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            IdTransaksi idTransaksiOrphanCheck = supplier.getIdTransaksi();
            if (idTransaksiOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Supplier (" + supplier + ") cannot be destroyed since the IdTransaksi " + idTransaksiOrphanCheck + " in its idTransaksi field has a non-nullable idOwner field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(supplier);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Supplier> findSupplierEntities() {
        return findSupplierEntities(true, -1, -1);
    }

    public List<Supplier> findSupplierEntities(int maxResults, int firstResult) {
        return findSupplierEntities(false, maxResults, firstResult);
    }

    private List<Supplier> findSupplierEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Supplier.class));
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

    public Supplier findSupplier(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Supplier.class, id);
        } finally {
            em.close();
        }
    }

    public int getSupplierCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Supplier> rt = cq.from(Supplier.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
