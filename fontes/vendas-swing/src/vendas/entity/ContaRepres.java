/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBCONTAREPRES")
@NamedQueries({
    @NamedQuery(name = "ContaRepres.findAll", query = "SELECT c FROM ContaRepres c"),
    @NamedQuery(name = "ContaRepres.findByIdContaRepres", query = "SELECT c FROM ContaRepres c WHERE c.idContaRepres = :idContaRepres")
})
public class ContaRepres implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBCONTAREPRES_IDCONTAREPRES_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBCONTAREPRES_IDCONTAREPRES_SEQ", sequenceName="TBCONTAREPRES_IDCONTAREPRES_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDCONTAREPRES", nullable = false)
    private Integer idContaRepres;
    @Basic(optional = false)
    @Column(name = "AGENCIA", nullable = true, length = 10)
    private String agencia;
    @Column(name = "ATIVO", length = 1)
    private String ativo;
    @Basic(optional = false)
    @Column(name = "CCORRENTE", nullable = true, length = 50)
    private String contaCorrente;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 50)
    private String nome;
    @Basic(optional = false)
    @Column(name = "cpfCnpj", nullable = false, length = 14)
    private String cpfCnpj;
    @JoinColumn(name = "IDBANCO", referencedColumnName = "IDBANCO", nullable = true)
    @ManyToOne(optional = true)
    private Banco banco;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false)
    @ManyToOne(optional = false)
    private Repres repres;
    @OneToMany(mappedBy = "contaRepres")
    private Collection<PgtoCliente> pgtoClienteCollection;
    @Column(name = "TIPOCONTA", nullable = false, length = 20)
    private String tipoConta;

    public ContaRepres() {
        ativo = "A";
        tipoConta = "Conta corrente";
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
    
    public void setAtivo(Boolean value) {
        if (value)
            ativo = "A";
        else
            ativo = "I";
    }
    
    public Boolean isAtivo() {
        return "A".equals(ativo);
    }

    public ContaRepres(Integer idcontarepres, String agencia, String ccorrente, String nome) {
        this.idContaRepres = idcontarepres;
        this.agencia = agencia;
        this.contaCorrente = ccorrente;
        this.nome = nome;
        ativo = "A";
    }

    public Integer getIdContaRepres() {
        return idContaRepres;
    }

    public void setIdContaRepres(Integer idcontarepres) {
        this.idContaRepres = idcontarepres;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(String ccorrente) {
        this.contaCorrente = ccorrente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco idbanco) {
        this.banco = idbanco;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres idrepres) {
        this.repres = idrepres;
    }

    public Collection<PgtoCliente> getPgtoClienteCollection() {
        return pgtoClienteCollection;
    }

    public void setPgtoClienteCollection(Collection<PgtoCliente> pgtoClienteCollection) {
        this.pgtoClienteCollection = pgtoClienteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContaRepres != null ? idContaRepres.hashCode() : 0);
        return hash;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContaRepres)) {
            return false;
        }
        ContaRepres other = (ContaRepres) object;
        if ((this.idContaRepres == null && other.idContaRepres != null) || (this.idContaRepres != null && !this.idContaRepres.equals(other.idContaRepres))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tipoConta).append(" ");
        if (banco != null) {
            sb.append(banco.getNome()).append(" ").append(agencia).append(" ");
        }
        sb.append(contaCorrente);
        return sb.toString();
    }

}
