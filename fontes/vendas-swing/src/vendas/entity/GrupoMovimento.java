/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author jaime
 */
@Entity
@Table(name = "TBGRUPOMOVIMENTO")
@NamedQueries({
    @NamedQuery(name = "GrupoMovimento.findAll", query = "SELECT g FROM GrupoMovimento g")})
public class GrupoMovimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBGRUPOMOVIMENTO_ID_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBGRUPOMOVIMENTO_ID_SEQ", sequenceName="TBGRUPOMOVIMENTO_ID_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDGRUPO")
    private Integer idgrupo;
    @Basic(optional = false)
    @Column(name = "NOMEGRUPO")
    private String nomeGrupo;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;
    
    public GrupoMovimento() {
    }

    public GrupoMovimento(Integer idgrupo) {
        this.idgrupo = idgrupo;
    }

    public GrupoMovimento(Integer idgrupo, String nomegrupo) {
        this.idgrupo = idgrupo;
        this.nomeGrupo = nomegrupo;
    }

    public Integer getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(Integer idgrupo) {
        this.idgrupo = idgrupo;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idgrupo != null ? idgrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoMovimento)) {
            return false;
        }
        GrupoMovimento other = (GrupoMovimento) object;
        if ((this.idgrupo == null && other.idgrupo != null) || (this.idgrupo != null && !this.idgrupo.equals(other.idgrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nomeGrupo;
    }
}
