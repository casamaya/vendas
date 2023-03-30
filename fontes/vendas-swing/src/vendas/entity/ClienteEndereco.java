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
@Table(name = "TBCLIENTEENDERECO")
@NamedQueries({
    @NamedQuery(name = "ClienteEndereco.findAll", query = "SELECT c FROM ClienteEndereco c"),
    @NamedQuery(name = "ClienteEndereco.findByIdcliente", query = "SELECT c FROM ClienteEndereco c WHERE c.clienteEnderecoPK.idcliente = :idcliente")
})
public class ClienteEndereco implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static char RESIDENCIAL = '0';
    public static char COMERCIAL = '1';
    public static char COBRANCA = '2';
    public static char ENTREGA = '3';

    @EmbeddedId
    protected ClienteEnderecoPK clienteEnderecoPK;
    @Column(name = "ENDERECO", length = 40)
    private String endereco;
    @Column(name = "BAIRRO", length = 25)
    private String bairro;
    @Column(name = "CIDADE", length = 30)
    private String cidade;
    @Column(name = "CEP", length = 8)
    private String cep;
    @Column(name = "UF", length = 2)
    private String uf;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;

    public ClienteEndereco() {
        this.clienteEnderecoPK = new ClienteEnderecoPK();
    }

    public ClienteEndereco(ClienteEnderecoPK clienteEnderecoPK) {
        this.clienteEnderecoPK = clienteEnderecoPK;
    }

    public ClienteEndereco(int idcliente, char tipo) {
        this.clienteEnderecoPK = new ClienteEnderecoPK(idcliente, tipo);
    }

    public ClienteEnderecoPK getClienteEnderecoPK() {
        return clienteEnderecoPK;
    }

    public void setClienteEnderecoPK(ClienteEnderecoPK clienteEnderecoPK) {
        this.clienteEnderecoPK = clienteEnderecoPK;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
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
        hash += (clienteEnderecoPK != null ? clienteEnderecoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteEndereco)) {
            return false;
        }
        ClienteEndereco other = (ClienteEndereco) object;
        if ((this.clienteEnderecoPK == null && other.clienteEnderecoPK != null) || (this.clienteEnderecoPK != null && !this.clienteEnderecoPK.equals(other.clienteEnderecoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (endereco != null)
            sb.append(endereco).append(" ");
        if (bairro != null)
            sb.append(bairro).append(" ");
        if (cidade != null)
            sb.append(cidade).append(" ");
        if (cep != null)
            sb.append(cep).append(" ");
        if (uf != null)
            sb.append(uf);
        return sb.toString();
    }

}
