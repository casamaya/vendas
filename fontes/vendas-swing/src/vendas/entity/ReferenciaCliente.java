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
@Table(name = "TBREFERENCIACLIENTE")
public class ReferenciaCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="tbrefcliente_idreferencia_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="tbrefcliente_idreferencia_seq", sequenceName="tbrefcliente_idreferencia_seq", allocationSize=1)
    @Column(name = "IDREFERENCIACLIENTE")
    private Integer idReferenciaCliente;
    @JoinColumn(name = "IDREFERENCIA", referencedColumnName = "IDREFERENCIA", nullable = false)
    @ManyToOne(optional = false)
    private Referencia referencia;
    @Column(name = "FONE1")
    private String fone1;
    @Column(name = "FONE2")
    private String fone2;
    @Column(name = "EMAIL")
    private String email;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE")
    @ManyToOne(optional = false)
    private Cliente cliente;

    public ReferenciaCliente() {
    }

    public Integer getIdReferenciaCliente() {
        return idReferenciaCliente;
    }

    public void setIdReferenciaCliente(Integer idreferencia) {
        this.idReferenciaCliente = idreferencia;
    }

    public String getFone1() {
        return fone1;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Referencia getReferencia() {
        return referencia;
    }

    public void setReferencia(Referencia referencia) {
        this.referencia = referencia;
    }

    public String getDescricaoReferencia() {
        StringBuilder sb = new StringBuilder();
        sb.append(referencia.getNome()).append("  FONE: ").append(fone1);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferenciaCliente != null ? idReferenciaCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReferenciaCliente)) {
            return false;
        }
        ReferenciaCliente other = (ReferenciaCliente) object;
        if ((this.idReferenciaCliente == null && other.idReferenciaCliente != null) || (this.idReferenciaCliente != null && !this.idReferenciaCliente.equals(other.idReferenciaCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ReferenciaCliente[idreferenciaCliente=" + idReferenciaCliente + "]";
    }

}
