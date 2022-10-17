/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercise1.Exercise1.C;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "supplier")
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findByNamaSupplier", query = "SELECT s FROM Supplier s WHERE s.namaSupplier = :namaSupplier"),
    @NamedQuery(name = "Supplier.findByIdSupplier", query = "SELECT s FROM Supplier s WHERE s.idSupplier = :idSupplier"),
    @NamedQuery(name = "Supplier.findByIdOwner", query = "SELECT s FROM Supplier s WHERE s.idOwner = :idOwner")})
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "Nama_Supplier")
    private String namaSupplier;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Supplier")
    private String idSupplier;
    @Basic(optional = false)
    @Column(name = "Id_Owner")
    private String idOwner;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idOwner")
    private IdTransaksi idTransaksi;

    public Supplier() {
    }

    public Supplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public Supplier(String idSupplier, String namaSupplier, String idOwner) {
        this.idSupplier = idSupplier;
        this.namaSupplier = namaSupplier;
        this.idOwner = idOwner;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public void setNamaSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
    }

    public String getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public IdTransaksi getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(IdTransaksi idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSupplier != null ? idSupplier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.idSupplier == null && other.idSupplier != null) || (this.idSupplier != null && !this.idSupplier.equals(other.idSupplier))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Exercise1.Exercise1.C.Supplier[ idSupplier=" + idSupplier + " ]";
    }
    
}
