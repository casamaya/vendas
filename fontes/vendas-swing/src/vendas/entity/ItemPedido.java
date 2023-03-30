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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBITEMPEDIDO")
@NamedQueries({
    @NamedQuery(name = "ItemPedido.findAll", query = "SELECT i FROM ItemPedido i"),
    @NamedQuery(name = "ItemPedido.findByIdPedido", query = "SELECT i FROM ItemPedido i WHERE i.itemPedidoPK.idPedido = :idPedido")
})
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ItemPedidoPK itemPedidoPK;
    @Basic(optional = false)
    @Column(name = "QTD", nullable = false, precision = 10, scale = 3)
    private BigDecimal qtd;
    @Column(name = "QTDROMANEIO", precision = 10, scale = 2)
    private BigDecimal qtdromaneio;
    @Column(name = "IPI", precision = 10, scale = 2)
    private BigDecimal ipi;
    @Basic(optional = false)
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "VALORCLIENTE", precision = 10, scale = 2)
    private BigDecimal valorCliente;
    @Column(name = "COMISSAO", precision = 10, scale = 2)
    private BigDecimal percComissao;
    @Column(name = "VALORROMANEIO", precision = 10, scale = 2)
    private BigDecimal valorromaneio;
    @Column(name = "EMBALAGEM", precision = 10, scale = 1)
    private BigDecimal embalagem;
    @Basic(optional = false)
    @Column(name = "SITUACAO", nullable = false, length = 1)
    private String situacao;
    @Column(name = "DTENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEntrega;
    @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "IDPRODUTO", referencedColumnName = "IDPRODUTO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produto produto;

    public ItemPedido() {
        valor = new BigDecimal(0);
        qtd = new BigDecimal(0);
        ipi = new BigDecimal(0);
        situacao = "N";
        itemPedidoPK = new ItemPedidoPK();
    }

    public ItemPedido(ItemPedidoPK itemPedidoPK) {
        this();
        this.itemPedidoPK = itemPedidoPK;
    }

    public ItemPedido(ItemPedidoPK itemPedidoPK, BigDecimal qtd, BigDecimal valor, String situacao) {
        this();
        this.itemPedidoPK = itemPedidoPK;
        this.qtd = qtd;
        this.valor = valor;
        this.situacao = situacao;
    }

    public ItemPedido(int idpedido, Integer idproduto) {
        this();
        this.itemPedidoPK = new ItemPedidoPK(idpedido, idproduto);
    }

    public ItemPedidoPK getItemPedidoPK() {
        return itemPedidoPK;
    }

    public void setItemPedidoPK(ItemPedidoPK itemPedidoPK) {
        this.itemPedidoPK = itemPedidoPK;
    }

    public BigDecimal getPercComissao() {
        if (percComissao == null)
            percComissao = new BigDecimal(0);        
        return percComissao;
    }

    public void setPercComissao(BigDecimal percComissao) {
        this.percComissao = percComissao;
    }

    public BigDecimal getQtd() {
        if (qtd == null)
            qtd = new BigDecimal(0);
        return qtd;
    }

    public void setQtd(BigDecimal qtd) {
        this.qtd = qtd;
    }

    public BigDecimal getQtdromaneio() {
        return qtdromaneio;
    }

    public void setQtdromaneio(BigDecimal qtdromaneio) {
        this.qtdromaneio = qtdromaneio;
    }

    public BigDecimal getIpi() {
        return ipi;
    }

    public void setIpi(BigDecimal ipi) {
        this.ipi = ipi;
    }

    public BigDecimal getValor() {
         if (valor == null)
            valor = new BigDecimal(0);
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorCliente() {
        return valorCliente;
    }

    public void setValorCliente(BigDecimal valorCliente) {
        this.valorCliente = valorCliente;
    }

    public BigDecimal getValorromaneio() {
        return valorromaneio;
    }

    public void setValorromaneio(BigDecimal valorromaneio) {
        this.valorromaneio = valorromaneio;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(Date dtentrega) {
        this.dtEntrega = dtentrega;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getTotal() {
        if (qtd != null && valor != null)
            return this.qtd.multiply(this.valor);
        else
            return null;
    }
    public BigDecimal getTotalCliente() {
        BigDecimal value = this.valorCliente;
        if (value == null)
            return null;
        return this.qtd.multiply(value);
    }

    public BigDecimal getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(BigDecimal embalagem) {
        this.embalagem = embalagem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemPedidoPK != null ? itemPedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPedido)) {
            return false;
        }
        ItemPedido other = (ItemPedido) object;
        if ((this.itemPedidoPK == null && other.itemPedidoPK != null) || (this.itemPedidoPK != null && !this.itemPedidoPK.equals(other.itemPedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ItemPedido[itemPedidoPK=" + itemPedidoPK + "]";
    }
}
