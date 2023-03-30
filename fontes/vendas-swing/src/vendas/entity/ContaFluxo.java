/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
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

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBCONTAFLUXO")
@NamedQueries({
    @NamedQuery(name = "ContaFluxo.findAll", query = "SELECT c FROM ContaFluxo c")})
public class ContaFluxo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "TBCONTAFLUXO_IDCONTA_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBCONTAFLUXO_IDCONTA_SEQ", sequenceName = "TBCONTAFLUXO_IDCONTA_SEQ", allocationSize = 1)
    @Column(name = "IDCONTA")
    private Integer idConta;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Lob
    @Column(name = "OBS")
    private String obs;
    @Column(name = "IN_CONTA_BANCARIA", length = 1)
    private String indicaContaBancaria;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "contaFluxo")
    //private List<Movimento> movimentoList;

    public ContaFluxo() {
        indicaContaBancaria = "N";
    }

    public ContaFluxo(Integer idconta) {
        this.idConta = idconta;
    }

    public ContaFluxo(Integer idconta, String nome) {
        this.idConta = idconta;
        this.nome = nome;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndicaContaBancaria() {
        return indicaContaBancaria;
    }

    public void setIndicaContaBancaria(String indicaContaBancaria) {
        this.indicaContaBancaria = indicaContaBancaria;
    }

    public String getObs() {
        return obs;
    }
    
    public void setObs(String obs) {
        this.obs = obs;
    }
/*
    public List<Movimento> getMovimentoList() {
        return movimentoList;
    }

    public void setMovimentoList(List<Movimento> movimentoList) {
        this.movimentoList = movimentoList;
    }
*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConta != null ? idConta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContaFluxo)) {
            return false;
        }
        ContaFluxo other = (ContaFluxo) object;
        if ((this.idConta == null && other.idConta != null) || (this.idConta != null && !this.idConta.equals(other.idConta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
    
}
