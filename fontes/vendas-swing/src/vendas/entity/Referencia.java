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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBREFERENCIA")
public class Referencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBREFERENCIA_IDREFERENCIA_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBREFERENCIA_IDREFERENCIA_SEQ", sequenceName="TBREFERENCIA_IDREFERENCIA_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDREFERENCIA", nullable = false)
    private Integer idReferencia;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;
    @Basic(optional = false)
    @Column(name = "FONE", nullable = false, length = 20)
    private String fone;

    public Referencia() {
    }

    public Referencia(Integer idsegmento) {
        this.idReferencia = idsegmento;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idsegmento) {
        this.idReferencia = idsegmento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
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
        if (!(object instanceof Referencia)) {
            return false;
        }
        Referencia other = (Referencia) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
