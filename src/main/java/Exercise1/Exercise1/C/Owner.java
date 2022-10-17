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
@Table(name = "owner")
@NamedQueries({
    @NamedQuery(name = "Owner.findAll", query = "SELECT o FROM Owner o"),
    @NamedQuery(name = "Owner.findByIdOwner", query = "SELECT o FROM Owner o WHERE o.idOwner = :idOwner"),
    @NamedQuery(name = "Owner.findByNamaOwner", query = "SELECT o FROM Owner o WHERE o.namaOwner = :namaOwner"),
    @NamedQuery(name = "Owner.findByNoTelpOwner", query = "SELECT o FROM Owner o WHERE o.noTelpOwner = :noTelpOwner")})
public class Owner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Owner")
    private String idOwner;
    @Basic(optional = false)
    @Column(name = "Nama_Owner")
    private String namaOwner;
    @Basic(optional = false)
    @Column(name = "No_TelpOwner")
    private String noTelpOwner;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idOwner")
    private Klien klien;

    public Owner() {
    }

    public Owner(String idOwner) {
        this.idOwner = idOwner;
    }

    public Owner(String idOwner, String namaOwner, String noTelpOwner) {
        this.idOwner = idOwner;
        this.namaOwner = namaOwner;
        this.noTelpOwner = noTelpOwner;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public String getNamaOwner() {
        return namaOwner;
    }

    public void setNamaOwner(String namaOwner) {
        this.namaOwner = namaOwner;
    }

    public String getNoTelpOwner() {
        return noTelpOwner;
    }

    public void setNoTelpOwner(String noTelpOwner) {
        this.noTelpOwner = noTelpOwner;
    }

    public Klien getKlien() {
        return klien;
    }

    public void setKlien(Klien klien) {
        this.klien = klien;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOwner != null ? idOwner.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Owner)) {
            return false;
        }
        Owner other = (Owner) object;
        if ((this.idOwner == null && other.idOwner != null) || (this.idOwner != null && !this.idOwner.equals(other.idOwner))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Exercise1.Exercise1.C.Owner[ idOwner=" + idOwner + " ]";
    }
    
}
