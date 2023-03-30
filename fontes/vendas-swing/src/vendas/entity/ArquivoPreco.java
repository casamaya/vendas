/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.Hibernate;

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBARQUIVOPRECO")
@NamedQueries({
    @NamedQuery(name = "ArquivoPreco.findAll", query = "SELECT a FROM ArquivoPreco a")})
public class ArquivoPreco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "TBARQUIVOPRECO_IDARQUIVO_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBARQUIVOPRECO_IDARQUIVO_SEQ", sequenceName = "TBARQUIVOPRECO_IDARQUIVO_SEQ", allocationSize = 1)
    @Column(name = "IDARQUIVO", nullable = false)
    private Integer idArquivo;
    @Column(name = "DESCRICAO", length = 512)
    private String descricao;
    @Lob
    @Column(name = "ARQUIVO", length = 0)
    private byte[] arquivo;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false)
    @ManyToOne(optional = false)
    private Repres repres;

    public ArquivoPreco() {
    }

    public ArquivoPreco(Integer idarquivo) {
        this.idArquivo = idarquivo;
    }

    public Integer getIdarquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Integer idarquivo) {
        this.idArquivo = idarquivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public void setBlob(Blob imageBlob) {
        this.arquivo = toByteArray(imageBlob);
    }

    public Blob getBlob() {
        return Hibernate.createBlob(this.arquivo);
    }

    private byte[] toByteArray(Blob fromImageBlob) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            return toByteArrayImpl(fromImageBlob, baos);
        } catch (Exception e) {
        }
        return null;
    }

    private byte[] toByteArrayImpl(Blob fromImageBlob,
            ByteArrayOutputStream baos) throws SQLException, IOException {
        byte buf[] = new byte[4000];
        int dataSize;
        InputStream is = fromImageBlob.getBinaryStream();

        try {
            while ((dataSize = is.read(buf)) != -1) {
                baos.write(buf, 0, dataSize);
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return baos.toByteArray();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArquivo != null ? idArquivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArquivoPreco)) {
            return false;
        }
        ArquivoPreco other = (ArquivoPreco) object;
        if ((this.idArquivo == null && other.idArquivo != null) || (this.idArquivo != null && !this.idArquivo.equals(other.idArquivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
