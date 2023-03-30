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
 * @author Sam
 */
@Entity
@Table(name = "TBREPRESPRODUTO")
@NamedQueries({
    @NamedQuery(name = "RepresProduto.findAll", query = "SELECT r FROM RepresProduto r"),
    @NamedQuery(name = "RepresProduto.findByIdrepres", query = "SELECT r FROM RepresProduto r WHERE r.represProdutoPK.idRepres = :idrepres"),
    @NamedQuery(name = "RepresProduto.findByIdproduto", query = "SELECT r FROM RepresProduto r WHERE r.represProdutoPK.idproduto = :idproduto")
})
public class RepresProduto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RepresProdutoPK represProdutoPK;
    @Basic(optional = false)
    @Column(name = "TABELA", nullable = false, length = 1)
    private String tabela;
    @Column(name = "IPI", precision = 10, scale = 2)
    private BigDecimal ipi;
    @Column(name = "PRECO", precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(name = "PRECOFINAL", precision = 10, scale = 2)
    private BigDecimal precoFinal;
    @Column(name = "PRECOV", precision = 10, scale = 2)
    private BigDecimal precov;
    @Column(name = "QTDUND", precision = 10, scale = 2)
    private BigDecimal qtdUnd;
    @Column(name = "COMISSAO", precision = 10, scale = 2)
    private BigDecimal percComissao;
    @Column(name = "FRETE", precision = 10, scale = 2)
    private BigDecimal frete;
    @Column(name = "PRECO2", precision = 10, scale = 2)
    private BigDecimal preco2;
    @Column(name = "EMBALAGEM", precision = 10, scale = 4)
    private BigDecimal embalagem;
    @Column(name = "ATIVO")
    private Short ativo;
    @Column(name = "CONVERSAOFRETE", length = 1)
    private String conversaoFrete;
    @JoinColumn(name = "IDPRODUTO", referencedColumnName = "IDPRODUTO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produto produto;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Repres repres;

    public RepresProduto() {
        represProdutoPK = new RepresProdutoPK();
        produto = new Produto();
        ativo = 1;
        tabela = "1";
    }

    public RepresProduto(RepresProdutoPK represProdutoPK) {
        this.represProdutoPK = represProdutoPK;
        produto = new Produto();
        ativo = 1;
        tabela = "1";
    }

    public RepresProduto(RepresProdutoPK represProdutoPK, String tabela) {
        this.represProdutoPK = represProdutoPK;
        this.tabela = tabela;
        produto = new Produto();
        ativo = 1;
        tabela = "1";
    }

    public RepresProduto(int idrepres, Integer idproduto) {
        this.represProdutoPK = new RepresProdutoPK(idrepres, idproduto);
        produto = new Produto();
        ativo = 1;
        tabela = "1";
        conversaoFrete = "U";
    }

    public RepresProdutoPK getRepresProdutoPK() {
        return represProdutoPK;
    }

    public void setRepresProdutoPK(RepresProdutoPK represProdutoPK) {
        this.represProdutoPK = represProdutoPK;
    }

    public BigDecimal getPercComissao() {
        return percComissao;
    }

    public void setPercComissao(BigDecimal percComissao) {
        this.percComissao = percComissao;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public BigDecimal getIpi() {
        return ipi;
    }

    public void setIpi(BigDecimal ipi) {
        this.ipi = ipi;
        calculaPrecoFinal(getFrete(), ipi, getPreco2());
        
        //Preço 02 + IPI + Frete
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(BigDecimal precofinal) {
        this.precoFinal = precofinal;
    }

    public BigDecimal getPrecov() {
        return precov;
    }

    public void setPrecov(BigDecimal precov) {
        this.precov = precov;
    }

    public String getConversaoFrete() {
        return conversaoFrete;
    }

    public void setConversaoFrete(String conversaoFrete) {
        this.conversaoFrete = conversaoFrete;
    }

    public BigDecimal getQtdUnd() {
        return qtdUnd;
    }

    public void setQtdUnd(BigDecimal qtdund) {
        this.qtdUnd = qtdund;
    }

    public BigDecimal getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(BigDecimal embalagem) {
        this.embalagem = embalagem;
    }

    public Short getAtivo() {
        return ativo;
    }

    public void setAtivo(Short ativo) {
        this.ativo = ativo;
    }


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Boolean getAtivado() {
        return (ativo == 1);
    }

    public void setAtivado(Boolean value) {
        if (value) {
            ativo = 1;
        } else {
            ativo = 0;
        }
    }

    public BigDecimal getPrecoCompleto() {
        BigDecimal valor = preco;
        BigDecimal divisor = new BigDecimal(100);
        if (ipi != null) {
            valor = preco.add(preco.divide(divisor).multiply(ipi));
        }
        if (produto.getPeso() != null) {
            if ("U".equals(conversaoFrete)) {
                
            }
        }
        return valor;
    }

    public BigDecimal getPreco2() {
        return preco2;
    }

    public void setPreco2(BigDecimal preco2) {
        this.preco2 = preco2;
        calculaPrecoFinal(getFrete(), getIpi(), preco2);
    }

    public BigDecimal getFrete() {
        return frete;
    }

    public void setFrete(BigDecimal frete) {
        this.frete = frete;
        calculaPrecoFinal(frete, getIpi(), getPreco2());
        
        
    }
    
    private void calculaPrecoFinal(BigDecimal vlFrete, BigDecimal vlIpi, BigDecimal vlPreco2) {
        if (vlPreco2 == null) vlPreco2 = BigDecimal.ZERO;
        if (vlIpi == null) vlIpi = BigDecimal.ZERO;
        if (vlFrete == null) vlFrete = BigDecimal.ZERO;
        
        precoFinal = vlPreco2.add(vlIpi.multiply(vlPreco2).divide(new BigDecimal(100))).add(vlFrete);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (represProdutoPK != null ? represProdutoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RepresProduto)) {
            return false;
        }
        RepresProduto other = (RepresProduto) object;
        if ((this.represProdutoPK == null && other.represProdutoPK != null) || (this.represProdutoPK != null && !this.represProdutoPK.equals(other.represProdutoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getProduto().getDescricao();
    }
}
