/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
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
@Table(name = "TBCLIENTECONTATO")
@NamedQueries({
    @NamedQuery(name = "ClienteContato.findAll", query = "SELECT c FROM ClienteContato c"),
    @NamedQuery(name = "ClienteContato.findByIdCliente", query = "SELECT c FROM ClienteContato c WHERE c.clienteContatoPK.idCliente = :idcliente")
})
public class ClienteContato implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClienteContatoPK clienteContatoPK;
    @Column(name = "ENDERECO")
    private String endereco;

    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;

    public ClienteContato() {
        clienteContatoPK = new ClienteContatoPK();
    }

    public ClienteContato(ClienteContatoPK clienteContatoPK) {
        this.clienteContatoPK = clienteContatoPK;
    }

    public ClienteContato(int idcliente, char tipo, int numitem) {
        this.clienteContatoPK = new ClienteContatoPK(idcliente, tipo, numitem);
    }

    public ClienteContatoPK getClienteContatoPK() {
        return clienteContatoPK;
    }

    public void setClienteContatoPK(ClienteContatoPK clienteContatoPK) {
        this.clienteContatoPK = clienteContatoPK;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clienteContatoPK != null ? clienteContatoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteContato)) {
            return false;
        }
        ClienteContato other = (ClienteContato) object;
        if ((this.clienteContatoPK == null && other.clienteContatoPK != null) || (this.clienteContatoPK != null && !this.clienteContatoPK.equals(other.clienteContatoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTipoContato();
    }

    public static String EMAILCOMERCIAL = "E-mail comercial";
    public static String EMAILFINANCEIRO = "E-mail financeiro";
    public static String EMAILFNE = "E-mail NFE";
    public static String MSN = "E-mail";
    public static String SKYPE = "Skype";

    public String getTipoContato() {
        String value;
        switch (getClienteContatoPK().getTipo()) {
        case '1': value = EMAILCOMERCIAL; break;
        case '2': value = MSN; break;
        case '3': value = SKYPE; break;
        case '4': value = EMAILFINANCEIRO; break;
        case '5': value = EMAILFNE; break;
        default: value = EMAILCOMERCIAL; break;
        }
        return value;

    }

}
