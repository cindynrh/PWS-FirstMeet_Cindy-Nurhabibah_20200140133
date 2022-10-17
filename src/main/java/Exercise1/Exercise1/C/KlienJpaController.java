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

/**
 *
 * @author user
 */
public class KlienJpaController implements Serializable {

    public KlienJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Klien klien) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Owner idOwnerOrphanCheck = klien.getIdOwner();
        if (idOwnerOrphanCheck != null) {
            Klien oldKlienOfIdOwner = idOwnerOrphanCheck.getKlien();
            if (oldKlienOfIdOwner != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Owner " + idOwnerOrphanCheck + " already has an item of type Klien whose idOwner column cannot be null. Please make another selection for the idOwner field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Owner idOwner = klien.getIdOwner();
            if (idOwner != null) {
                idOwner = em.getReference(idOwner.getClass(), idOwner.getIdOwner());
                klien.setIdOwner(idOwner);
            }
            IdTransaksi idTransaksi = klien.getIdTransaksi();
            if (idTransaksi != null) {
                idTransaksi = em.getReference(idTransaksi.getClass(), idTransaksi.getIdTransaksi());
                klien.setIdTransaksi(idTransaksi);
            }
            em.persist(klien);
            if (idOwner != null) {
                idOwner.setKlien(klien);
                idOwner = em.merge(idOwner);
            }
            if (idTransaksi != null) {
                Klien oldIdKlienOfIdTransaksi = idTransaksi.getIdKlien();
                if (oldIdKlienOfIdTransaksi != null) {
                    oldIdKlienOfIdTransaksi.setIdTransaksi(null);
                    oldIdKlienOfIdTransaksi = em.merge(oldIdKlienOfIdTransaksi);
                }
                idTransaksi.setIdKlien(klien);
                idTransaksi = em.merge(idTransaksi);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKlien(klien.getIdKlien()) != null) {
                throw new PreexistingEntityException("Klien " + klien + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Klien klien) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Klien persistentKlien = em.find(Klien.class, klien.getIdKlien());
            Owner idOwnerOld = persistentKlien.getIdOwner();
            Owner idOwnerNew = klien.getIdOwner();
            IdTransaksi idTransaksiOld = persistentKlien.getIdTransaksi();
            IdTransaksi idTransaksiNew = klien.getIdTransaksi();
            List<String> illegalOrphanMessages = null;
            if (idOwnerNew != null && !idOwnerNew.equals(idOwnerOld)) {
                Klien oldKlienOfIdOwner = idOwnerNew.getKlien();
                if (oldKlienOfIdOwner != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Owner " + idOwnerNew + " already has an item of type Klien whose idOwner column cannot be null. Please make another selection for the idOwner field.");
                }
            }
            if (idTransaksiOld != null && !idTransaksiOld.equals(idTransaksiNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain IdTransaksi " + idTransaksiOld + " since its idKlien field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idOwnerNew != null) {
                idOwnerNew = em.getReference(idOwnerNew.getClass(), idOwnerNew.getIdOwner());
                klien.setIdOwner(idOwnerNew);
            }
            if (idTransaksiNew != null) {
                idTransaksiNew = em.getReference(idTransaksiNew.getClass(), idTransaksiNew.getIdTransaksi());
                klien.setIdTransaksi(idTransaksiNew);
            }
            klien = em.merge(klien);
            if (idOwnerOld != null && !idOwnerOld.equals(idOwnerNew)) {
                idOwnerOld.setKlien(null);
                idOwnerOld = em.merge(idOwnerOld);
            }
            if (idOwnerNew != null && !idOwnerNew.equals(idOwnerOld)) {
                idOwnerNew.setKlien(klien);
                idOwnerNew = em.merge(idOwnerNew);
            }
            if (idTransaksiNew != null && !idTransaksiNew.equals(idTransaksiOld)) {
                Klien oldIdKlienOfIdTransaksi = idTransaksiNew.getIdKlien();
                if (oldIdKlienOfIdTransaksi != null) {
                    oldIdKlienOfIdTransaksi.setIdTransaksi(null);
                    oldIdKlienOfIdTransaksi = em.merge(oldIdKlienOfIdTransaksi);
                }
                idTransaksiNew.setIdKlien(klien);
                idTransaksiNew = em.merge(idTransaksiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = klien.getIdKlien();
                if (findKlien(id) == null) {
                    throw new NonexistentEntityException("The klien with id " + id + " no longer exists.");
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
            Klien klien;
            try {
                klien = em.getReference(Klien.class, id);
                klien.getIdKlien();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The klien with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            IdTransaksi idTransaksiOrphanCheck = klien.getIdTransaksi();
            if (idTransaksiOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Klien (" + klien + ") cannot be destroyed since the IdTransaksi " + idTransaksiOrphanCheck + " in its idTransaksi field has a non-nullable idKlien field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Owner idOwner = klien.getIdOwner();
            if (idOwner != null) {
                idOwner.setKlien(null);
                idOwner = em.merge(idOwner);
            }
            em.remove(klien);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Klien> findKlienEntities() {
        return findKlienEntities(true, -1, -1);
    }

    public List<Klien> findKlienEntities(int maxResults, int firstResult) {
        return findKlienEntities(false, maxResults, firstResult);
    }

    private List<Klien> findKlienEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Klien.class));
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

    public Klien findKlien(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Klien.class, id);
        } finally {
            em.close();
        }
    }

    public int getKlienCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Klien> rt = cq.from(Klien.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
