/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBMENSAGEM")
@NamedQueries({
    @NamedQuery(name = "Mensagem.findAll", query = "SELECT m FROM Mensagem m"),
    @NamedQuery(name = "Mensagem.findByIdmensagem", query = "SELECT m FROM Mensagem m WHERE m.idmensagem = :idmensagem")
})
public class Mensagem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDMENSAGEM", nullable = false)
    private Integer idmensagem;
    @Basic(optional = false)
    @Column(name = "TITULO", nullable = false, length = 50)
    private String titulo;
    @Basic(optional = false)
    @Column(name = "TIPO", nullable = false, length = 1)
    private String tipo;
    @Lob
    @Column(name = "MENSAGEM", length = 0)
    private String mensagem;

    public Mensagem() {
    }

    public Mensagem(Integer idmensagem) {
        this.idmensagem = idmensagem;
    }

    public Mensagem(Integer idmensagem, String titulo) {
        this.idmensagem = idmensagem;
        this.titulo = titulo;
    }

    public Integer getIdmensagem() {
        return idmensagem;
    }

    public void setIdmensagem(Integer idmensagem) {
        this.idmensagem = idmensagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmensagem != null ? idmensagem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mensagem)) {
            return false;
        }
        Mensagem other = (Mensagem) object;
        if ((this.idmensagem == null && other.idmensagem != null) || (this.idmensagem != null && !this.idmensagem.equals(other.idmensagem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Mensagem[idmensagem=" + idmensagem + "]";
    }

}
