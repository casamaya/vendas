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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBSEGMERCADO")
@NamedQueries({
    @NamedQuery(name = "SegMercado.findAll", query = "SELECT s FROM SegMercado s order by s.nome"),
    @NamedQuery(name = "SegMercado.findByName", query = "SELECT s FROM SegMercado s WHERE s.idSegmento like :name order by s.nome")
})
public class SegMercado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBSEGMERCADO_IDSEGMENTO_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBSEGMERCADO_IDSEGMENTO_SEQ", sequenceName="TBSEGMERCADO_IDSEGMENTO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDSEGMENTO", nullable = false)
    private Integer idSegmento;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 30)
    private String nome;
    @ManyToMany(mappedBy = "segmentos")
    private Collection<Repres> represCollection;
    @ManyToMany(mappedBy = "segmentos")
    private Collection<Cliente> clienteCollection;

    public SegMercado() {
    }

    public SegMercado(Integer idsegmento) {
        this.idSegmento = idsegmento;
    }

    public SegMercado(Integer idsegmento, String nome) {
        this.idSegmento = idsegmento;
        this.nome = nome;
    }

    public Integer getIdSegmento() {
        return idSegmento;
    }

    public void setIdSegmento(Integer idsegmento) {
        this.idSegmento = idsegmento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<Repres> getRepresCollection() {
        return represCollection;
    }

    public void setRepresCollection(Collection<Repres> represCollection) {
        this.represCollection = represCollection;
    }

    public Collection<Cliente> getClientes() {
        return clienteCollection;
    }

    public void setClientes(Collection<Cliente> clienteCollection) {
        this.clienteCollection = clienteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSegmento != null ? idSegmento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegMercado)) {
            return false;
        }
        SegMercado other = (SegMercado) object;
        if ((this.idSegmento == null && other.idSegmento != null) || (this.idSegmento != null && !this.idSegmento.equals(other.idSegmento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
