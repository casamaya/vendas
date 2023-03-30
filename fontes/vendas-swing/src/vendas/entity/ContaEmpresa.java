/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBCONTA")
@NamedQueries({
    @NamedQuery(name = "ContaEmpresa.findAll", query = "SELECT c FROM ContaEmpresa c")
})
public class ContaEmpresa implements Serializable {

    @Id
    @GeneratedValue(generator="TBCONTA_IDCONTA_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBCONTA_IDCONTA_SEQ", sequenceName="TBCONTA_IDCONTA_SEQ", allocationSize=1)
    @Column(name = "IDCONTA", nullable = false)
    private Integer idConta;
    @Column(name = "AGENCIA")
    private String agencia;
    @Column(name = "CCORRENTE")
    private String contaCorrente;
    @JoinColumn(name = "IDBANCO", referencedColumnName = "IDBANCO")
    @ManyToOne
    private Banco banco;

    public ContaEmpresa() {
        banco = new Banco();
    }

    public ContaEmpresa(Integer idConta) {
        this.idConta = idConta;
    }

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
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

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
    public String getNome() {
        return "";
    }


    public String getCpfCnpj() {
        return "";
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConta != null ? idConta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ContaEmpresa)) {
            return false;
        }
        ContaEmpresa other = (ContaEmpresa) object;
        if ((this.idConta == null && other.idConta != null) || (this.idConta != null && !this.idConta.equals(other.idConta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (banco.getNome() != null)
            sb.append(banco.getNome()).append(" ");
        if (agencia != null)
            sb.append(agencia).append(" ");
        if (contaCorrente != null)
            sb.append(contaCorrente);
        return sb.toString();
    }

}
