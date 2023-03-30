/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBORCAMENTO")
public class Orcamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDORCAMENTO")
    private Integer idorcamento;
    @Column(name = "IDPEDIDO")
    private Integer idpedido;
    @Basic(optional = false)
    @Column(name = "DTORCAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtorcamento;
    @Basic(optional = false)
    @Column(name = "DTVALIDADE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtvalidade;
    @Column(name = "DTENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEntrega;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Basic(optional = false)
    @Column(name = "SITUACAO")
    private String situacao;
    @Lob
    @Column(name = "OBS2", length = 0)
    private String obs2;
    @Lob
    @Column(name = "OBS3", length = 0)
    private String obs3;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES")
    @ManyToOne(optional = false)
    private Repres repres;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "orcamento")
    private List<ItemOrcamento> items;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "orcamento")
    private List<ArquivoOrcamento> arquivos;
    @JoinColumn(name = "IDPGTO", referencedColumnName = "IDPGTO", nullable = false)
    @ManyToOne(optional = false)
    private FormaPgto formaPgto;


    public Orcamento() {
        situacao = "N";
        items = new ArrayList<ItemOrcamento>();
    }

    public Orcamento(Integer idorcamento) {
        this.idorcamento = idorcamento;
    }

    public Orcamento(Integer idorcamento, Date dtorcamento, Date dtvalidade, BigDecimal valor, String situacao) {
        this.idorcamento = idorcamento;
        this.dtorcamento = dtorcamento;
        this.dtvalidade = dtvalidade;
        this.valor = valor;
        this.situacao = situacao;
    }

    public Integer getIdorcamento() {
        return idorcamento;
    }

    public void setIdorcamento(Integer idorcamento) {
        this.idorcamento = idorcamento;
    }

    public Integer getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(Integer idpedido) {
        this.idpedido = idpedido;
    }

    public Date getDtorcamento() {
        return dtorcamento;
    }

    public void setDtorcamento(Date dtorcamento) {
        this.dtorcamento = dtorcamento;
    }

    public Date getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(Date dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public Date getDtvalidade() {
        return dtvalidade;
    }

    public void setDtvalidade(Date dtvalidade) {
        this.dtvalidade = dtvalidade;
    }

    public BigDecimal getValor() {
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

    public String getObs2() {
        return obs2;
    }
    
    public String getObsOrcamento() {
        StringBuilder sb = new StringBuilder();
        String result;
        if (obs2 == null && obs3 == null) {
            result = null;
        } else {
            if (obs2 != null) {
                sb.append(obs2).append("\n\n");
            }
            if (obs3 != null) {
                sb.append(obs3);
            }  
            result = sb.toString();
        }
        
        return result;
    }

    public void setObs2(String obs2) {
        this.obs2 = obs2;
    }

    public String getObs3() {
        return obs3;
    }

    public void setObs3(String obs3) {
        this.obs3 = obs3;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemOrcamento> getItems() {
        return items;
    }

    public void setItems(List<ItemOrcamento> itemOrcamentoList) {
        this.items = itemOrcamentoList;
    }
    public BigDecimal getVlrUnidades() {
        BigDecimal vlrUnidades = new BigDecimal(0);
        for (ItemOrcamento item : items) {
            vlrUnidades = vlrUnidades.add(item.getQtd().multiply(item.getProduto().getFatorConversao()));
        }
        return vlrUnidades;
    }

    public List<ArquivoOrcamento> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoOrcamento> arquivos) {
        this.arquivos = arquivos;
    }

    public FormaPgto getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(FormaPgto formaPgto) {
        this.formaPgto = formaPgto;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idorcamento != null ? idorcamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orcamento)) {
            return false;
        }
        Orcamento other = (Orcamento) object;
        if ((this.idorcamento == null && other.idorcamento != null) || (this.idorcamento != null && !this.idorcamento.equals(other.idorcamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Orcamento[idorcamento=" + idorcamento + "]";
    }

}
