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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBBANCOREFCLIENTE")
public class BancoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="TBBANCOREFCLI_IDREFERENCIA_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBBANCOREFCLI_IDREFERENCIA_SEQ", sequenceName="TBBANCOREFCLI_IDREFERENCIA_SEQ", allocationSize=1)
    @Column(name = "IDREFERENCIA")
    private Integer idReferencia;
    @JoinColumn(name = "IDBANCO", referencedColumnName = "IDBANCO", nullable = false)
    @ManyToOne(optional = false)
    private Banco banco;
    @Column(name = "CONTA")
    private String conta;
    @Column(name = "AGENCIA")
    private String agencia;
    @Column(name = "FONE")
    private String fone;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE")
    @ManyToOne(optional = false)
    private Cliente cliente;

    public BancoCliente() {
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idreferencia) {
        this.idReferencia = idreferencia;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferencia != null ? idReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BancoCliente)) {
            return false;
        }
        BancoCliente other = (BancoCliente) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    public String getReferencia() {
        StringBuilder sb = new StringBuilder();
        sb.append(banco.getNome()).append(" AG.: ").append(agencia).append("  C/C: ").append(conta).append("  FONE: ").append(fone);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "vendas.entity.ReferenciaCliente[idreferencia=" + idReferencia + "]";
    }

}
