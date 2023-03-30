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
import javax.persistence.ManyToMany;
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
@Table(name = "TBROTEIROVENDEDOR")
@NamedQueries({
    @NamedQuery(name = "RoteiroVendedor.findAll", query = "SELECT r FROM RoteiroVendedor r order by r.descricao"),
    @NamedQuery(name = "RoteiroVendedor.findByIdRoteiro", query = "SELECT r FROM RoteiroVendedor r WHERE r.idRoteiro = :idroteiro"),
    @NamedQuery(name = "RoteiroVendedor.findByDescricao", query = "SELECT r FROM RoteiroVendedor r WHERE r.descricao = :descricao")
})
public class RoteiroVendedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "TBROTEIROVENDEDOR_IDROTEIRO_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBROTEIROVENDEDOR_IDROTEIRO_SEQ", sequenceName = "TBROTEIROVENDEDOR_IDROTEIRO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDROTEIRO", nullable = false)
    private Integer idRoteiro;
    @Basic(optional = false)
    @Column(name = "DESCRICAO", nullable = false, length = 45)
    private String descricao;
    @ManyToMany(mappedBy = "roteiros")
    private Collection<Cliente> clientes;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR", nullable = false)
    @ManyToOne(optional = false)
    private Vendedor vendedor;

    public RoteiroVendedor() {
    }

    public RoteiroVendedor(Integer idroteiro) {
        this.idRoteiro = idroteiro;
    }

    public RoteiroVendedor(Integer idroteiro, String descricao) {
        this.idRoteiro = idroteiro;
        this.descricao = descricao;
    }

    public Integer getIdRoteiro() {
        return idRoteiro;
    }

    public void setIdRoteiro(Integer idroteiro) {
        this.idRoteiro = idroteiro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Collection<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Collection<Cliente> clienteCollection) {
        this.clientes = clienteCollection;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor idvendedor) {
        this.vendedor = idvendedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRoteiro != null ? idRoteiro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoteiroVendedor)) {
            return false;
        }
        RoteiroVendedor other = (RoteiroVendedor) object;
        if ((this.idRoteiro == null && other.idRoteiro != null) || (this.idRoteiro != null && !this.idRoteiro.equals(other.idRoteiro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
