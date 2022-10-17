/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercise1.Exercise1.C;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author user
 */
@Embeddable
public class ProdukPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "Kode_Produk")
    private String kodeProduk;
    @Basic(optional = false)
    @Column(name = "Id_Transaksi")
    private String idTransaksi;

    public ProdukPK() {
    }

    public ProdukPK(String kodeProduk, String idTransaksi) {
        this.kodeProduk = kodeProduk;
        this.idTransaksi = idTransaksi;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeProduk != null ? kodeProduk.hashCode() : 0);
        hash += (idTransaksi != null ? idTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdukPK)) {
            return false;
        }
        ProdukPK other = (ProdukPK) object;
        if ((this.kodeProduk == null && other.kodeProduk != null) || (this.kodeProduk != null && !this.kodeProduk.equals(other.kodeProduk))) {
            return false;
        }
        if ((this.idTransaksi == null && other.idTransaksi != null) || (this.idTransaksi != null && !this.idTransaksi.equals(other.idTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Exercise1.Exercise1.C.ProdukPK[ kodeProduk=" + kodeProduk + ", idTransaksi=" + idTransaksi + " ]";
    }
    
}
