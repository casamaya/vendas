/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBCORREIO")
public class Correio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "TBCORREIO_IDMENSAGEM_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBCORREIO_IDMENSAGEM_SEQ", sequenceName = "TBCORREIO_IDMENSAGEM_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDMENSAGEM")
    private Integer idMensagem;
    @Basic(optional = false)
    @Column(name = "DATAENVIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEnvio;
    @Column(name = "DATALEITURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLeitura;
    @Basic(optional = false)
    @Lob
    @Column(name = "MENSAGEM")
    private String mensagem;
    @Basic(optional = false)
    @Column(name = "MENSAGEMLIDA")
    private char mensagemlida;
    @JoinColumn(name = "IDORIGEM", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false)
    private User userOrigem;
    @JoinColumn(name = "IDDESTINO", referencedColumnName = "USERNAME")
    @ManyToOne(optional = false)
    private User userDestino;

    public Correio() {
    }

    public Correio(Integer idMensagem) {
        this.idMensagem = idMensagem;
    }

    public Correio(Integer idMensagem, Date dataEnvio, char mensagemlida) {
        this.idMensagem = idMensagem;
        this.dataEnvio = dataEnvio;
        this.mensagemlida = mensagemlida;
    }

    public Integer getIdmensagem() {
        return idMensagem;
    }

    public void setIdmensagem(Integer idMensagem) {
        this.idMensagem = idMensagem;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Date getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public char getMensagemlida() {
        return mensagemlida;
    }

    public void setMensagemlida(char mensagemlida) {
        this.mensagemlida = mensagemlida;
    }

    public User getUserOrigem() {
        return userOrigem;
    }

    public void setUserOrigem(User idorigem) {
        this.userOrigem = idorigem;
    }

    public User getUserDestino() {
        return userDestino;
    }

    public void setUserDestino(User iddestino) {
        this.userDestino = iddestino;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMensagem != null ? idMensagem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Correio)) {
            return false;
        }
        Correio other = (Correio) object;
        if ((this.idMensagem == null && other.idMensagem != null) || (this.idMensagem != null && !this.idMensagem.equals(other.idMensagem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Correio[ idMensagem=" + idMensagem + " ]";
    }
    
}
