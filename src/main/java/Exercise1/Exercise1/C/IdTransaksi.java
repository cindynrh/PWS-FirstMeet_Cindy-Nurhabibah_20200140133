/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercise1.Exercise1.C;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author user
 */
@Entity
@Table(name = "id_transaksi")
@NamedQueries({
    @NamedQuery(name = "IdTransaksi.findAll", query = "SELECT i FROM IdTransaksi i"),
    @NamedQuery(name = "IdTransaksi.findByIdTransaksi", query = "SELECT i FROM IdTransaksi i WHERE i.idTransaksi = :idTransaksi"),
    @NamedQuery(name = "IdTransaksi.findByTglpembayaran", query = "SELECT i FROM IdTransaksi i WHERE i.tglpembayaran = :tglpembayaran")})
public class IdTransaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Transaksi")
    private String idTransaksi;
    @Basic(optional = false)
    @Column(name = "Tgl_pembayaran")
    @Temporal(TemporalType.DATE)
    private Date tglpembayaran;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTransaksi1")
    private Collection<Produk> produkCollection;
    @JoinColumn(name = "Id_Owner", referencedColumnName = "Id_Owner")
    @OneToOne(optional = false)
    private Supplier idOwner;
    @JoinColumn(name = "Id_Klien", referencedColumnName = "Id_Klien")
    @OneToOne(optional = false)
    private Klien idKlien;

    public IdTransaksi() {
    }

    public IdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public IdTransaksi(String idTransaksi, Date tglpembayaran) {
        this.idTransaksi = idTransaksi;
        this.tglpembayaran = tglpembayaran;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Date getTglpembayaran() {
        return tglpembayaran;
    }

    public void setTglpembayaran(Date tglpembayaran) {
        this.tglpembayaran = tglpembayaran;
    }

    public Collection<Produk> getProdukCollection() {
        return produkCollection;
    }

    public void setProdukCollection(Collection<Produk> produkCollection) {
        this.produkCollection = produkCollection;
    }

    public Supplier getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Supplier idOwner) {
        this.idOwner = idOwner;
    }

    public Klien getIdKlien() {
        return idKlien;
    }

    public void setIdKlien(Klien idKlien) {
        this.idKlien = idKlien;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransaksi != null ? idTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IdTransaksi)) {
            return false;
        }
        IdTransaksi other = (IdTransaksi) object;
        if ((this.idTransaksi == null && other.idTransaksi != null) || (this.idTransaksi != null && !this.idTransaksi.equals(other.idTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Exercise1.Exercise1.C.IdTransaksi[ idTransaksi=" + idTransaksi + " ]";
    }
    
}
