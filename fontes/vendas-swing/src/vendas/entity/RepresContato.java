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
@Table(name = "TBREPRESCONTATO")
@NamedQueries({
    @NamedQuery(name = "RepresContato.findAll", query = "SELECT r FROM RepresContato r"),
    @NamedQuery(name = "RepresContato.findByIdRepres", query = "SELECT r FROM RepresContato r WHERE r.represContatoPK.idRepres = :idRepres"),
    @NamedQuery(name = "RepresContato.findByTipo", query = "SELECT r FROM RepresContato r WHERE r.represContatoPK.tipo = :tipo"),
    @NamedQuery(name = "RepresContato.findByNumItem", query = "SELECT r FROM RepresContato r WHERE r.represContatoPK.numItem = :numItem"),
    @NamedQuery(name = "RepresContato.findByEndereco", query = "SELECT r FROM RepresContato r WHERE r.endereco = :endereco")
})
public class RepresContato implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RepresContatoPK represContatoPK;
    @Column(name = "ENDERECO")
    private String endereco;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Repres repres;

    public RepresContato() {
        represContatoPK = new RepresContatoPK();
    }

    public RepresContato(RepresContatoPK represContatoPK) {
        this.represContatoPK = represContatoPK;
    }

    public RepresContato(int idrepres, char tipo, int numitem) {
        this.represContatoPK = new RepresContatoPK(idrepres, tipo, numitem);
    }

    public RepresContatoPK getRepresContatoPK() {
        return represContatoPK;
    }

    public void setRepresContatoPK(RepresContatoPK represContatoPK) {
        this.represContatoPK = represContatoPK;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (represContatoPK != null ? represContatoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RepresContato)) {
            return false;
        }
        RepresContato other = (RepresContato) object;
        if ((this.represContatoPK == null && other.represContatoPK != null) || (this.represContatoPK != null && !this.represContatoPK.equals(other.represContatoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "demo.RepresContato[represContatoPK=" + represContatoPK + "]";
    }
    public static String EMAIL = "Email";
    public static String MSN = "MSN";
    public static String SKYPE = "Skype";

    public String getTipoContato() {
        String value;
        switch (getRepresContatoPK().getTipo()) {
        case '2': value = MSN; break;
        case '3': value = SKYPE; break;
        default: value = EMAIL; break;
        }
        return value;

    }
}
