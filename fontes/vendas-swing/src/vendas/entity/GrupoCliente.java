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
import javax.persistence.ManyToMany;
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
@Table(name = "TBGRUPOCLIENTE")
@NamedQueries({
    @NamedQuery(name = "GrupoCliente.findAll", query = "SELECT g FROM GrupoCliente g order by g.nomeGrupo"),
    @NamedQuery(name = "GrupoCliente.findByName", query = "SELECT g FROM GrupoCliente g WHERE g.nomeGrupo like :name order by g.nomeGrupo")
})
public class GrupoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBGRUPOCLIENTE_IDGRPCLIENTE_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBGRUPOCLIENTE_IDGRPCLIENTE_SEQ", sequenceName="TBGRUPOCLIENTE_IDGRPCLIENTE_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDGRPCLIENTE", nullable = false)
    private Integer idGrupoCliente;
    @Basic(optional = false)
    @Column(name = "NOMEGRUPO", nullable = false, length = 40)
    private String nomeGrupo;
    @ManyToMany(mappedBy = "gruposCliente")
    private Collection<Cliente> clientes;

    public GrupoCliente() {
    }

    public GrupoCliente(Integer idgrpcliente) {
        this.idGrupoCliente = idgrpcliente;
    }

    public GrupoCliente(Integer idgrpcliente, String nomegrupo) {
        this.idGrupoCliente = idgrpcliente;
        this.nomeGrupo = nomegrupo;
    }

    public Integer getIdGrupoCliente() {
        return idGrupoCliente;
    }

    public void setIdGrupoCliente(Integer idgrpcliente) {
        this.idGrupoCliente = idgrpcliente;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomegrupo) {
        this.nomeGrupo = nomegrupo;
    }

    public Collection<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Collection<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupoCliente != null ? idGrupoCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoCliente)) {
            return false;
        }
        GrupoCliente other = (GrupoCliente) object;
        if ((this.idGrupoCliente == null && other.idGrupoCliente != null) || (this.idGrupoCliente != null && !this.idGrupoCliente.equals(other.idGrupoCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nomeGrupo;
    }

}
