/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "TBCLIENTEREPRES")
@NamedQueries({
    @NamedQuery(name = "ClienteRepres.findAll", query = "SELECT c FROM ClienteRepres c"),
    @NamedQuery(name = "ClienteRepres.findByIdrepres", query = "SELECT c FROM ClienteRepres c WHERE c.clienteRepresPK.idRepres = :idrepres"),
    @NamedQuery(name = "ClienteRepres.findByIdcliente", query = "SELECT c FROM ClienteRepres c WHERE c.clienteRepresPK.idCliente = :idcliente")
})
public class ClienteRepres implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClienteRepresPK clienteRepresPK;
    @Column(name = "CODREPR", length = 10)
    private String codRepres;
    @Column(name = "CODIDENT", length = 10)
    private String codident;
    @Column(name = "LIMITECREDITO", precision = 10, scale = 2)
    private BigDecimal limitecredito;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Repres repres;

    public ClienteRepres() {
    }

    public ClienteRepres(ClienteRepresPK clienteRepresPK) {
        this.clienteRepresPK = clienteRepresPK;
    }

    public ClienteRepres(int idrepres, int idcliente) {
        this.clienteRepresPK = new ClienteRepresPK(idrepres, idcliente);
    }

    public ClienteRepresPK getClienteRepresPK() {
        return clienteRepresPK;
    }

    public void setClienteRepresPK(ClienteRepresPK clienteRepresPK) {
        this.clienteRepresPK = clienteRepresPK;
    }

    public String getCodRepres() {
        return codRepres;
    }

    public void setCodRepres(String codrepr) {
        this.codRepres = codrepr;
    }

    public String getCodIdentificador() {
        return codident;
    }

    public void setCodIdentificador(String codident) {
        this.codident = codident;
    }

    public BigDecimal getLimiteCredito() {
        return limitecredito;
    }

    public void setLimiteCredito(BigDecimal limitecredito) {
        this.limitecredito = limitecredito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clienteRepresPK != null ? clienteRepresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteRepres)) {
            return false;
        }
        ClienteRepres other = (ClienteRepres) object;
        if ((this.clienteRepresPK == null && other.clienteRepresPK != null) || (this.clienteRepresPK != null && !this.clienteRepresPK.equals(other.clienteRepresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return clienteRepresPK.toString();
    }

}
