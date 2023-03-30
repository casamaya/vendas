/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import org.hibernate.Hibernate;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBPRODUTO")
@NamedQueries({
    @NamedQuery(name = "Produto.findAll", query = "SELECT p FROM Produto p"),
    @NamedQuery(name = "Produto.findByIdProduto", query = "SELECT p FROM Produto p WHERE p.idProduto = :idProduto"),
    @NamedQuery(name = "Produto.findByDescricao", query = "SELECT p FROM Produto p WHERE p.descricao = :descricao")
})
public class Produto implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "IDPRODUTO", nullable = false)
    private Integer idProduto;
    @Basic(optional = false)
    @Column(name = "DESCRICAO", nullable = false, length = 100)
    private String descricao;
    @Column(name = "BLOQUEADO", length = 1, nullable = false)
    private String bloqueado;
    @Basic(optional = false)
    @Column(name = "FATORCNV", nullable = false, precision = 10, scale = 4)
    private BigDecimal fatorConversao;
    @Column(name = "PESO", precision = 10, scale = 4)
    private BigDecimal peso;
    @Lob
    @Column(name = "FOTO", length = 0)
    private byte[] foto;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "produto")
    private Collection<ItemPedido> itemPedidoCollection;
    @JoinColumn(name = "IDCODGRUPO", referencedColumnName = "IDCODGRUPO")
    @ManyToOne
    private GrupoProduto grupoProduto;
    @JoinColumn(name = "IDCODSUBGRUPO", referencedColumnName = "IDCODSUBGRUPO")
    @ManyToOne
    private SubGrupoProduto subGrupoProduto;
    @JoinColumn(name = "IDUNIDADE", referencedColumnName = "IDUNIDADE", nullable = false)
    @ManyToOne(optional = false)
    private UnidadeProduto unidade;
    @JoinColumn(name = "IDUNDCUMUL", referencedColumnName = "IDUNIDADE", nullable = false)
    @ManyToOne(optional = false)
    private UnidadeProduto undCumulativa;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "produto")
    private Collection<ItemPedidoAtend> itemPedidoAtendCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "produto")
    private Collection<RepresProduto> produtos;

    public Produto() {
        unidade = new UnidadeProduto();
        undCumulativa = new UnidadeProduto();
        grupoProduto = new GrupoProduto();
        fatorConversao = new BigDecimal(1);
        peso = BigDecimal.ONE;
        bloqueado = "N";
    }

    public Produto(Integer idproduto) {
        this();
        this.idProduto = idproduto;
        peso = BigDecimal.ONE;
    }

    public Produto(Integer idproduto, String descricao, BigDecimal fatorcnv) {
        this();
        this.idProduto = idproduto;
        this.descricao = descricao;
        this.fatorConversao = fatorcnv;
        peso = BigDecimal.ONE;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idproduto) {
        this.idProduto = idproduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getFatorConversao() {
        return fatorConversao;
    }

    public void setFatorConversao(BigDecimal fatorcnv) {
        this.fatorConversao = fatorcnv;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal value) {
        this.peso = value;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public void setBlob(Blob imageBlob) {
        this.foto = toByteArray(imageBlob);
    }

    public Blob getBlob() {
        Blob value;
        if (this.foto == null) {
            value = null;
        } else {
            value = Hibernate.createBlob(this.foto);
        }
        return value;
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
    
    public Collection<ItemPedido> getItemPedidoCollection() {
        return itemPedidoCollection;
    }

    public void setItemPedidoCollection(Collection<ItemPedido> itemPedidoCollection) {
        this.itemPedidoCollection = itemPedidoCollection;
    }

    public GrupoProduto getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(GrupoProduto idcodgrupo) {
        this.grupoProduto = idcodgrupo;
    }

    public SubGrupoProduto getSubGrupoProduto() {
        return subGrupoProduto;
    }

    public void setSubGrupoProduto(SubGrupoProduto subGrupoProduto) {
        this.subGrupoProduto = subGrupoProduto;
    }

    public UnidadeProduto getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeProduto idunidade) {
        this.unidade = idunidade;
    }

    public UnidadeProduto getUndCumulativa() {
        return undCumulativa;
    }

    public void setUndCumulativa(UnidadeProduto idundcumul) {
        this.undCumulativa = idundcumul;
    }

    public Collection<ItemPedidoAtend> getItemPedidoAtendCollection() {
        return itemPedidoAtendCollection;
    }

    public void setItemPedidoAtendCollection(Collection<ItemPedidoAtend> itemPedidoAtendCollection) {
        this.itemPedidoAtendCollection = itemPedidoAtendCollection;
    }

    public Collection<RepresProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Collection<RepresProduto> represProdutoCollection) {
        this.produtos = represProdutoCollection;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }
    
    public void setBloqueado(Boolean isbloqueado) {
        this.bloqueado = isbloqueado ? "S" : "N";
    }
    
    public Boolean isBloqueado() {
        return "S".equals(this.bloqueado);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProduto != null ? idProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.idProduto == null && other.idProduto != null) || (this.idProduto != null && !this.idProduto.equals(other.idProduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
