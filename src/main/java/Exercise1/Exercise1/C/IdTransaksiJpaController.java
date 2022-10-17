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
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author user
 */
public class IdTransaksiJpaController implements Serializable {

    public IdTransaksiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Exercise1_Exercise1.C_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public IdTransaksiJpaController() {
    }
    
    

    public void create(IdTransaksi idTransaksi) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (idTransaksi.getProdukCollection() == null) {
            idTransaksi.setProdukCollection(new ArrayList<Produk>());
        }
        List<String> illegalOrphanMessages = null;
        Supplier idOwnerOrphanCheck = idTransaksi.getIdOwner();
        if (idOwnerOrphanCheck != null) {
            IdTransaksi oldIdTransaksiOfIdOwner = idOwnerOrphanCheck.getIdTransaksi();
            if (oldIdTransaksiOfIdOwner != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Supplier " + idOwnerOrphanCheck + " already has an item of type IdTransaksi whose idOwner column cannot be null. Please make another selection for the idOwner field.");
            }
        }
        Klien idKlienOrphanCheck = idTransaksi.getIdKlien();
        if (idKlienOrphanCheck != null) {
            IdTransaksi oldIdTransaksiOfIdKlien = idKlienOrphanCheck.getIdTransaksi();
            if (oldIdTransaksiOfIdKlien != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Klien " + idKlienOrphanCheck + " already has an item of type IdTransaksi whose idKlien column cannot be null. Please make another selection for the idKlien field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier idOwner = idTransaksi.getIdOwner();
            if (idOwner != null) {
                idOwner = em.getReference(idOwner.getClass(), idOwner.getIdSupplier());
                idTransaksi.setIdOwner(idOwner);
            }
            Klien idKlien = idTransaksi.getIdKlien();
            if (idKlien != null) {
                idKlien = em.getReference(idKlien.getClass(), idKlien.getIdKlien());
                idTransaksi.setIdKlien(idKlien);
            }
            Collection<Produk> attachedProdukCollection = new ArrayList<Produk>();
            for (Produk produkCollectionProdukToAttach : idTransaksi.getProdukCollection()) {
                produkCollectionProdukToAttach = em.getReference(produkCollectionProdukToAttach.getClass(), produkCollectionProdukToAttach.getProdukPK());
                attachedProdukCollection.add(produkCollectionProdukToAttach);
            }
            idTransaksi.setProdukCollection(attachedProdukCollection);
            em.persist(idTransaksi);
            if (idOwner != null) {
                idOwner.setIdTransaksi(idTransaksi);
                idOwner = em.merge(idOwner);
            }
            if (idKlien != null) {
                idKlien.setIdTransaksi(idTransaksi);
                idKlien = em.merge(idKlien);
            }
            for (Produk produkCollectionProduk : idTransaksi.getProdukCollection()) {
                IdTransaksi oldIdTransaksi1OfProdukCollectionProduk = produkCollectionProduk.getIdTransaksi1();
                produkCollectionProduk.setIdTransaksi1(idTransaksi);
                produkCollectionProduk = em.merge(produkCollectionProduk);
                if (oldIdTransaksi1OfProdukCollectionProduk != null) {
                    oldIdTransaksi1OfProdukCollectionProduk.getProdukCollection().remove(produkCollectionProduk);
                    oldIdTransaksi1OfProdukCollectionProduk = em.merge(oldIdTransaksi1OfProdukCollectionProduk);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIdTransaksi(idTransaksi.getIdTransaksi()) != null) {
                throw new PreexistingEntityException("IdTransaksi " + idTransaksi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(IdTransaksi idTransaksi) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IdTransaksi persistentIdTransaksi = em.find(IdTransaksi.class, idTransaksi.getIdTransaksi());
            Supplier idOwnerOld = persistentIdTransaksi.getIdOwner();
            Supplier idOwnerNew = idTransaksi.getIdOwner();
            Klien idKlienOld = persistentIdTransaksi.getIdKlien();
            Klien idKlienNew = idTransaksi.getIdKlien();
            Collection<Produk> produkCollectionOld = persistentIdTransaksi.getProdukCollection();
            Collection<Produk> produkCollectionNew = idTransaksi.getProdukCollection();
            List<String> illegalOrphanMessages = null;
            if (idOwnerNew != null && !idOwnerNew.equals(idOwnerOld)) {
                IdTransaksi oldIdTransaksiOfIdOwner = idOwnerNew.getIdTransaksi();
                if (oldIdTransaksiOfIdOwner != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Supplier " + idOwnerNew + " already has an item of type IdTransaksi whose idOwner column cannot be null. Please make another selection for the idOwner field.");
                }
            }
            if (idKlienNew != null && !idKlienNew.equals(idKlienOld)) {
                IdTransaksi oldIdTransaksiOfIdKlien = idKlienNew.getIdTransaksi();
                if (oldIdTransaksiOfIdKlien != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Klien " + idKlienNew + " already has an item of type IdTransaksi whose idKlien column cannot be null. Please make another selection for the idKlien field.");
                }
            }
            for (Produk produkCollectionOldProduk : produkCollectionOld) {
                if (!produkCollectionNew.contains(produkCollectionOldProduk)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Produk " + produkCollectionOldProduk + " since its idTransaksi1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idOwnerNew != null) {
                idOwnerNew = em.getReference(idOwnerNew.getClass(), idOwnerNew.getIdSupplier());
                idTransaksi.setIdOwner(idOwnerNew);
            }
            if (idKlienNew != null) {
                idKlienNew = em.getReference(idKlienNew.getClass(), idKlienNew.getIdKlien());
                idTransaksi.setIdKlien(idKlienNew);
            }
            Collection<Produk> attachedProdukCollectionNew = new ArrayList<Produk>();
            for (Produk produkCollectionNewProdukToAttach : produkCollectionNew) {
                produkCollectionNewProdukToAttach = em.getReference(produkCollectionNewProdukToAttach.getClass(), produkCollectionNewProdukToAttach.getProdukPK());
                attachedProdukCollectionNew.add(produkCollectionNewProdukToAttach);
            }
            produkCollectionNew = attachedProdukCollectionNew;
            idTransaksi.setProdukCollection(produkCollectionNew);
            idTransaksi = em.merge(idTransaksi);
            if (idOwnerOld != null && !idOwnerOld.equals(idOwnerNew)) {
                idOwnerOld.setIdTransaksi(null);
                idOwnerOld = em.merge(idOwnerOld);
            }
            if (idOwnerNew != null && !idOwnerNew.equals(idOwnerOld)) {
                idOwnerNew.setIdTransaksi(idTransaksi);
                idOwnerNew = em.merge(idOwnerNew);
            }
            if (idKlienOld != null && !idKlienOld.equals(idKlienNew)) {
                idKlienOld.setIdTransaksi(null);
                idKlienOld = em.merge(idKlienOld);
            }
            if (idKlienNew != null && !idKlienNew.equals(idKlienOld)) {
                idKlienNew.setIdTransaksi(idTransaksi);
                idKlienNew = em.merge(idKlienNew);
            }
            for (Produk produkCollectionNewProduk : produkCollectionNew) {
                if (!produkCollectionOld.contains(produkCollectionNewProduk)) {
                    IdTransaksi oldIdTransaksi1OfProdukCollectionNewProduk = produkCollectionNewProduk.getIdTransaksi1();
                    produkCollectionNewProduk.setIdTransaksi1(idTransaksi);
                    produkCollectionNewProduk = em.merge(produkCollectionNewProduk);
                    if (oldIdTransaksi1OfProdukCollectionNewProduk != null && !oldIdTransaksi1OfProdukCollectionNewProduk.equals(idTransaksi)) {
                        oldIdTransaksi1OfProdukCollectionNewProduk.getProdukCollection().remove(produkCollectionNewProduk);
                        oldIdTransaksi1OfProdukCollectionNewProduk = em.merge(oldIdTransaksi1OfProdukCollectionNewProduk);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = idTransaksi.getIdTransaksi();
                if (findIdTransaksi(id) == null) {
                    throw new NonexistentEntityException("The idTransaksi with id " + id + " no longer exists.");
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
            IdTransaksi idTransaksi;
            try {
                idTransaksi = em.getReference(IdTransaksi.class, id);
                idTransaksi.getIdTransaksi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The idTransaksi with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Produk> produkCollectionOrphanCheck = idTransaksi.getProdukCollection();
            for (Produk produkCollectionOrphanCheckProduk : produkCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This IdTransaksi (" + idTransaksi + ") cannot be destroyed since the Produk " + produkCollectionOrphanCheckProduk + " in its produkCollection field has a non-nullable idTransaksi1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Supplier idOwner = idTransaksi.getIdOwner();
            if (idOwner != null) {
                idOwner.setIdTransaksi(null);
                idOwner = em.merge(idOwner);
            }
            Klien idKlien = idTransaksi.getIdKlien();
            if (idKlien != null) {
                idKlien.setIdTransaksi(null);
                idKlien = em.merge(idKlien);
            }
            em.remove(idTransaksi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<IdTransaksi> findIdTransaksiEntities() {
        return findIdTransaksiEntities(true, -1, -1);
    }

    public List<IdTransaksi> findIdTransaksiEntities(int maxResults, int firstResult) {
        return findIdTransaksiEntities(false, maxResults, firstResult);
    }

    private List<IdTransaksi> findIdTransaksiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(IdTransaksi.class));
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

    public IdTransaksi findIdTransaksi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(IdTransaksi.class, id);
        } finally {
            em.close();
        }
    }

    public int getIdTransaksiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<IdTransaksi> rt = cq.from(IdTransaksi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
