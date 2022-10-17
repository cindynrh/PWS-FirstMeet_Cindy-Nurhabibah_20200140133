/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercise1.Exercise1.C;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "produk")
@NamedQueries({
    @NamedQuery(name = "Produk.findAll", query = "SELECT p FROM Produk p"),
    @NamedQuery(name = "Produk.findByKodeProduk", query = "SELECT p FROM Produk p WHERE p.produkPK.kodeProduk = :kodeProduk"),
    @NamedQuery(name = "Produk.findByNamaProduk", query = "SELECT p FROM Produk p WHERE p.namaProduk = :namaProduk"),
    @NamedQuery(name = "Produk.findByHargaProduk", query = "SELECT p FROM Produk p WHERE p.hargaProduk = :hargaProduk"),
    @NamedQuery(name = "Produk.findByIdTransaksi", query = "SELECT p FROM Produk p WHERE p.produkPK.idTransaksi = :idTransaksi")})
public class Produk implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProdukPK produkPK;
    @Basic(optional = false)
    @Column(name = "Nama_Produk")
    private String namaProduk;
    @Basic(optional = false)
    @Column(name = "Harga_Produk")
    private int hargaProduk;
    @JoinColumn(name = "Id_Transaksi", referencedColumnName = "Id_Transaksi", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private IdTransaksi idTransaksi1;

    public Produk() {
    }

    public Produk(ProdukPK produkPK) {
        this.produkPK = produkPK;
    }

    public Produk(ProdukPK produkPK, String namaProduk, int hargaProduk) {
        this.produkPK = produkPK;
        this.namaProduk = namaProduk;
        this.hargaProduk = hargaProduk;
    }

    public Produk(String kodeProduk, String idTransaksi) {
        this.produkPK = new ProdukPK(kodeProduk, idTransaksi);
    }

    public ProdukPK getProdukPK() {
        return produkPK;
    }

    public void setProdukPK(ProdukPK produkPK) {
        this.produkPK = produkPK;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getHargaProduk() {
        return hargaProduk;
    }

    public void setHargaProduk(int hargaProduk) {
        this.hargaProduk = hargaProduk;
    }

    public IdTransaksi getIdTransaksi1() {
        return idTransaksi1;
    }

    public void setIdTransaksi1(IdTransaksi idTransaksi1) {
        this.idTransaksi1 = idTransaksi1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (produkPK != null ? produkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produk)) {
            return false;
        }
        Produk other = (Produk) object;
        if ((this.produkPK == null && other.produkPK != null) || (this.produkPK != null && !this.produkPK.equals(other.produkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Exercise1.Exercise1.C.Produk[ produkPK=" + produkPK + " ]";
    }
    
}
