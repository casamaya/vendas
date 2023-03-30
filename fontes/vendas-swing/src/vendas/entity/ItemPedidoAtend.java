/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBITEMPEDIDOATEND")
@NamedQueries({
    @NamedQuery(name = "ItemPedidoAtend.findAll", query = "SELECT i FROM ItemPedidoAtend i"),
    @NamedQuery(name = "ItemPedidoAtend.findByIdPedido", query = "SELECT i FROM ItemPedidoAtend i WHERE i.itemPedidoAtendPK.idPedido = :idPedido"),
    @NamedQuery(name = "ItemPedidoAtend.findByNf", query = "SELECT i FROM ItemPedidoAtend i WHERE i.itemPedidoAtendPK.nf = :nf"),
    @NamedQuery(name = "ItemPedidoAtend.findByIdProduto", query = "SELECT i FROM ItemPedidoAtend i WHERE i.itemPedidoAtendPK.idProduto = :idProduto")
})
public class ItemPedidoAtend implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ItemPedidoAtendPK itemPedidoAtendPK;
    @Basic(optional = false)
    @Column(name = "QTD", nullable = false, precision = 10, scale = 3)
    private BigDecimal qtd;
    @Column(name = "IPI")
    private Short ipi;
    @Basic(optional = false)
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "COMISSAO", precision = 10, scale = 2)
    private BigDecimal percComissao;
    @Column(name = "EMBALAGEM", precision = 10, scale = 2)
    private BigDecimal embalagem;
    @Transient
    private BigDecimal saldo;
    @Column(name = "DTENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtentrega;
    @JoinColumns({
        @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "NF", referencedColumnName = "NF", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private AtendimentoPedido atendimentoPedido;
    @JoinColumn(name = "IDPRODUTO", referencedColumnName = "IDPRODUTO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produto produto;

    public ItemPedidoAtend() {
        itemPedidoAtendPK = new ItemPedidoAtendPK();
    }

    public ItemPedidoAtend(ItemPedidoAtendPK itemPedidoAtendPK) {
        this.itemPedidoAtendPK = itemPedidoAtendPK;
    }

    public ItemPedidoAtend(ItemPedidoAtendPK itemPedidoAtendPK, BigDecimal qtd, BigDecimal valor) {
        this.itemPedidoAtendPK = itemPedidoAtendPK;
        this.qtd = qtd;
        this.valor = valor;
    }

    public ItemPedidoAtend(int idpedido, String nf, Integer idproduto) {
        this.itemPedidoAtendPK = new ItemPedidoAtendPK(idpedido, nf, idproduto);
    }

    public ItemPedidoAtendPK getItemPedidoAtendPK() {
        return itemPedidoAtendPK;
    }

    public void setItemPedidoAtendPK(ItemPedidoAtendPK itemPedidoAtendPK) {
        this.itemPedidoAtendPK = itemPedidoAtendPK;
    }

    public BigDecimal getQtd() {
        return qtd;
    }

    public void setQtd(BigDecimal qtd) {
        this.qtd = qtd;
    }

    public Short getIpi() {
        return ipi;
    }

    public void setIpi(Short ipi) {
        this.ipi = ipi;
    }

    public BigDecimal getValor() {
        return valor;
    }
    public BigDecimal getTotal() {
        return this.qtd.multiply(this.valor);
    }

    public BigDecimal getValorCliente() {
        return valor;
    }
    
    public BigDecimal getTotalCliente() {
        BigDecimal value = this.valor;
        if (value == null)
            return null;
        return this.qtd.multiply(value);
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(BigDecimal embalagem) {
        this.embalagem = embalagem;
    }

    public Date getDtEntrega() {
        return dtentrega;
    }

    public void setDtEntrega(Date dtentrega) {
        this.dtentrega = dtentrega;
    }

    public AtendimentoPedido getAtendimentoPedido() {
        return atendimentoPedido;
    }

    public void setAtendimentoPedido(AtendimentoPedido atendimentoPedido) {
        this.atendimentoPedido = atendimentoPedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getPercComissao() {
        return percComissao;
    }

    public void setPercComissao(BigDecimal percComissao) {
        this.percComissao = percComissao;
    }

    public BigDecimal getValorComissao() {
        BigDecimal bd100 = new BigDecimal(100);
        return valor.multiply(qtd).divide(bd100).multiply(percComissao);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemPedidoAtendPK != null ? itemPedidoAtendPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPedidoAtend)) {
            return false;
        }
        ItemPedidoAtend other = (ItemPedidoAtend) object;
        if ((this.itemPedidoAtendPK == null && other.itemPedidoAtendPK != null) || (this.itemPedidoAtendPK != null && !this.itemPedidoAtendPK.equals(other.itemPedidoAtendPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ItemPedidoAtend[itemPedidoAtendPK=" + itemPedidoAtendPK + "]";
    }
}
