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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "klien")
@NamedQueries({
    @NamedQuery(name = "Klien.findAll", query = "SELECT k FROM Klien k"),
    @NamedQuery(name = "Klien.findByNamaKlien", query = "SELECT k FROM Klien k WHERE k.namaKlien = :namaKlien"),
    @NamedQuery(name = "Klien.findByNoTelpKlien", query = "SELECT k FROM Klien k WHERE k.noTelpKlien = :noTelpKlien"),
    @NamedQuery(name = "Klien.findByIdKlien", query = "SELECT k FROM Klien k WHERE k.idKlien = :idKlien")})
public class Klien implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "Nama_Klien")
    private String namaKlien;
    @Basic(optional = false)
    @Column(name = "No_TelpKlien")
    private String noTelpKlien;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Klien")
    private String idKlien;
    @JoinColumn(name = "Id_Owner", referencedColumnName = "Id_Owner")
    @OneToOne(optional = false)
    private Owner idOwner;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idKlien")
    private IdTransaksi idTransaksi;

    public Klien() {
    }

    public Klien(String idKlien) {
        this.idKlien = idKlien;
    }

    public Klien(String idKlien, String namaKlien, String noTelpKlien) {
        this.idKlien = idKlien;
        this.namaKlien = namaKlien;
        this.noTelpKlien = noTelpKlien;
    }

    public String getNamaKlien() {
        return namaKlien;
    }

    public void setNamaKlien(String namaKlien) {
        this.namaKlien = namaKlien;
    }

    public String getNoTelpKlien() {
        return noTelpKlien;
    }

    public void setNoTelpKlien(String noTelpKlien) {
        this.noTelpKlien = noTelpKlien;
    }

    public String getIdKlien() {
        return idKlien;
    }

    public void setIdKlien(String idKlien) {
        this.idKlien = idKlien;
    }

    public Owner getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Owner idOwner) {
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
        hash += (idKlien != null ? idKlien.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Klien)) {
            return false;
        }
        Klien other = (Klien) object;
        if ((this.idKlien == null && other.idKlien != null) || (this.idKlien != null && !this.idKlien.equals(other.idKlien))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Exercise1.Exercise1.C.Klien[ idKlien=" + idKlien + " ]";
    }
    
}
