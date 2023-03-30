/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author sam
 */
@Entity
@Table(name = "TBITEMORCAMENTO")
public class ItemOrcamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ItemOrcamentoPK itemOrcamentoPK;
    @Basic(optional = false)
    @Column(name = "QTD")
    private BigDecimal qtd;
    @Column(name = "IPI")
    private BigDecimal ipi;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "VALORCLIENTE", precision = 10, scale = 2)
    private BigDecimal valorCliente;
    @Column(name = "COMISSAO", precision = 10, scale = 2)
    private BigDecimal percComissao;
    @Basic(optional = false)
    @Column(name = "SITUACAO")
    private String situacao;
    @Column(name = "EMBALAGEM")
    private Integer embalagem;
    @JoinColumn(name = "IDPRODUTO", referencedColumnName = "IDPRODUTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produto produto;
    @JoinColumn(name = "IDORCAMENTO", referencedColumnName = "IDORCAMENTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orcamento orcamento;

    public ItemOrcamento() {
        valor = new BigDecimal(0);
        qtd = new BigDecimal(0);
        ipi = new BigDecimal(0);
        situacao = "N";
        itemOrcamentoPK = new ItemOrcamentoPK();
    }

    public ItemOrcamento(ItemOrcamentoPK itemOrcamentoPK) {
        this.itemOrcamentoPK = itemOrcamentoPK;
    }

    public ItemOrcamento(ItemOrcamentoPK itemOrcamentoPK, BigDecimal qtd, BigDecimal valor, String situacao) {
        this.itemOrcamentoPK = itemOrcamentoPK;
        this.qtd = qtd;
        this.valor = valor;
        this.situacao = situacao;
    }

    public ItemOrcamento(int idorcamento, int idproduto) {
        this.itemOrcamentoPK = new ItemOrcamentoPK(idorcamento, idproduto);
    }

    public ItemOrcamentoPK getItemOrcamentoPK() {
        return itemOrcamentoPK;
    }

    public void setItemOrcamentoPK(ItemOrcamentoPK itemOrcamentoPK) {
        this.itemOrcamentoPK = itemOrcamentoPK;
    }

    public BigDecimal getQtd() {
        if (qtd == null)
            qtd = new BigDecimal(0);
        return qtd;
    }

    public void setQtd(BigDecimal qtd) {
        this.qtd = qtd;
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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Integer getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(Integer embalagem) {
        this.embalagem = embalagem;
    }

    public BigDecimal getValorCliente() {
        return valorCliente;
    }

    public void setValorCliente(BigDecimal valorCliente) {
        this.valorCliente = valorCliente;
    }

    public BigDecimal getPercComissao() {
        if (percComissao == null)
            percComissao = new BigDecimal(0);
        return percComissao;
    }

    public void setPercComissao(BigDecimal percComissao) {
        this.percComissao = percComissao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }
    public BigDecimal getTotal() {
        if (qtd != null && valorCliente != null)
            return this.qtd.multiply(this.valorCliente);
        else
            return null;
    }
    
    public BigDecimal getTotalCliente() {
        BigDecimal value = this.valorCliente;
        if (value == null)
            return null;
        return this.qtd.multiply(value);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemOrcamentoPK != null ? itemOrcamentoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemOrcamento)) {
            return false;
        }
        ItemOrcamento other = (ItemOrcamento) object;
        if ((this.itemOrcamentoPK == null && other.itemOrcamentoPK != null) || (this.itemOrcamentoPK != null && !this.itemOrcamentoPK.equals(other.itemOrcamentoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ItemOrcamento[itemOrcamentoPK=" + itemOrcamentoPK + "]";
    }

}
