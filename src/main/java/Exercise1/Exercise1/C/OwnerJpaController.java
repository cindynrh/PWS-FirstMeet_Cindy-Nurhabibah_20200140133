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
public class OwnerJpaController implements Serializable {

    public OwnerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Owner owner) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Klien klien = owner.getKlien();
            if (klien != null) {
                klien = em.getReference(klien.getClass(), klien.getIdKlien());
                owner.setKlien(klien);
            }
            em.persist(owner);
            if (klien != null) {
                Owner oldIdOwnerOfKlien = klien.getIdOwner();
                if (oldIdOwnerOfKlien != null) {
                    oldIdOwnerOfKlien.setKlien(null);
                    oldIdOwnerOfKlien = em.merge(oldIdOwnerOfKlien);
                }
                klien.setIdOwner(owner);
                klien = em.merge(klien);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOwner(owner.getIdOwner()) != null) {
                throw new PreexistingEntityException("Owner " + owner + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Owner owner) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Owner persistentOwner = em.find(Owner.class, owner.getIdOwner());
            Klien klienOld = persistentOwner.getKlien();
            Klien klienNew = owner.getKlien();
            List<String> illegalOrphanMessages = null;
            if (klienOld != null && !klienOld.equals(klienNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Klien " + klienOld + " since its idOwner field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (klienNew != null) {
                klienNew = em.getReference(klienNew.getClass(), klienNew.getIdKlien());
                owner.setKlien(klienNew);
            }
            owner = em.merge(owner);
            if (klienNew != null && !klienNew.equals(klienOld)) {
                Owner oldIdOwnerOfKlien = klienNew.getIdOwner();
                if (oldIdOwnerOfKlien != null) {
                    oldIdOwnerOfKlien.setKlien(null);
                    oldIdOwnerOfKlien = em.merge(oldIdOwnerOfKlien);
                }
                klienNew.setIdOwner(owner);
                klienNew = em.merge(klienNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = owner.getIdOwner();
                if (findOwner(id) == null) {
                    throw new NonexistentEntityException("The owner with id " + id + " no longer exists.");
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
            Owner owner;
            try {
                owner = em.getReference(Owner.class, id);
                owner.getIdOwner();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The owner with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Klien klienOrphanCheck = owner.getKlien();
            if (klienOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Owner (" + owner + ") cannot be destroyed since the Klien " + klienOrphanCheck + " in its klien field has a non-nullable idOwner field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(owner);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Owner> findOwnerEntities() {
        return findOwnerEntities(true, -1, -1);
    }

    public List<Owner> findOwnerEntities(int maxResults, int firstResult) {
        return findOwnerEntities(false, maxResults, firstResult);
    }

    private List<Owner> findOwnerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Owner.class));
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

    public Owner findOwner(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Owner.class, id);
        } finally {
            em.close();
        }
    }

    public int getOwnerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Owner> rt = cq.from(Owner.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
