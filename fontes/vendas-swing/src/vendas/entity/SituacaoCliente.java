/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "TBSITCLIENTE")
@NamedQueries({
    @NamedQuery(name = "SituacaoCliente.findAll", query = "SELECT s FROM SituacaoCliente s order by s.nome"),
    @NamedQuery(name = "SituacaoCliente.findByName", query = "SELECT s FROM SituacaoCliente s WHERE s.nome = :name order by s.nome")
})
public class SituacaoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="tbsitcliente_idsitcliente_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="tbsitcliente_idsitcliente_seq", sequenceName="tbsitcliente_idsitcliente_seq", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDSITCLIENTE", nullable = false)
    private Integer idSitCliente;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 30)
    private String nome;
    @Basic(optional = false)
    @Column(name = "PEDIDO", nullable = false)
    private String pedido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "situacaoCliente")
    private Collection<Cliente> clienteCollection;

    public SituacaoCliente() {
        this.pedido = "1";
    }

    public SituacaoCliente(Integer idsitcliente) {
        this();
        this.idSitCliente = idsitcliente;
    }

    public SituacaoCliente(Integer idsitcliente, String nome) {
        this();
        this.idSitCliente = idsitcliente;
        this.nome = nome;
    }

    public Integer getIdSitCliente() {
        return idSitCliente;
    }

    public void setIdSitCliente(Integer idsitcliente) {
        this.idSitCliente = idsitcliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public Collection<Cliente> getClienteCollection() {
        return clienteCollection;
    }

    public void setClienteCollection(Collection<Cliente> clienteCollection) {
        this.clienteCollection = clienteCollection;
    }

    public Boolean getPermitePedido() {
        return new Boolean(pedido.equals("1"));
    }

    public void setPermitePedido(Boolean value) {
        if (value)
            pedido = "1";
        else
            pedido = "0";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSitCliente != null ? idSitCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SituacaoCliente)) {
            return false;
        }
        SituacaoCliente other = (SituacaoCliente) object;
        if ((this.idSitCliente == null && other.idSitCliente != null) || (this.idSitCliente != null && !this.idSitCliente.equals(other.idSitCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
