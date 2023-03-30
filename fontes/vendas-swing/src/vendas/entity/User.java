/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import vendas.util.Constants;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBUSER")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u order by u.userName"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.userName = :userName")
})
public class User implements Serializable {
    @ManyToMany(mappedBy = "users")
    private List<Perfil> perfis;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "USERNAME", nullable = false, length = 20)
    private String userName;
    @Basic(optional = false)
    @Column(name = "PASSWD", nullable = false, length = 100)
    private String passwd;
    @Transient
    private String repeatPasswd;
    @Transient
    private String newPasswd;
    @Column(name = "NAMEDESC", length = 40)
    private String namedesc;
    @Column(name = "IDVENDEDOR")
    private Integer idvendedor;
    @Column(name = "PORT")
    private Integer port;
    @Column(name = "SERVERNAME", length = 100)
    private String serverName;
    @Column(name = "EMAIL", length = 100)
    private String email;
    @Column(name = "EMAILPASSWD")
    private String emailPasswd;
    @Lob
    @Column(name = "ASSINATURA", length = 0)
    private String assinatura;
    @Column(name = "ATIVO")
    private Character ativo;

    public User() {
    }

    public User(String username) {
        this.userName = username;
    }

    public User(String username, String passwd) {
        this.userName = username;
        this.passwd = passwd;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRepeatPasswd() {
        return repeatPasswd;
    }

    public void setRepeatPasswd(String repeatPasswd) {
        this.repeatPasswd = repeatPasswd;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public void setNewPasswd(String newPasswd) {
        this.newPasswd = newPasswd;
    }

    public String getNamedesc() {
        return namedesc;
    }

    public void setNamedesc(String namedesc) {
        this.namedesc = namedesc;
    }

    public Integer getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(Integer idvendedor) {
        this.idvendedor = idvendedor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPasswd() {
        return emailPasswd;
    }

    public void setEmailPasswd(String emailPasswd) {
        this.emailPasswd = emailPasswd;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Character getAtivo() {
        return ativo;
    }

    public void setAtivo(Character ativo) {
        this.ativo = ativo;
    }
    
    public String getPerfisExtenso() {
        StringBuilder sb = new StringBuilder();
        for (Perfil p : getPerfis()) {
            sb.append(p.getPerfil()).append(".");
        }
        return sb.toString();
    }
    
    public boolean isAdmin() {
        boolean contains = false;
        for (Perfil p : getPerfis()) {
            if (p.getPerfil().equals(Constants.ADMIN)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
    
    public boolean isEscritorio() {
        boolean contains = false;
        for (Perfil p : getPerfis()) {
            if (p.getPerfil().equals(Constants.ESCRITORIO)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
        
    public boolean isVendedor() {
        boolean contains = false;
        for (Perfil p : getPerfis()) {
            if (p.getPerfil().equals(Constants.VENDEDOR)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
    public boolean isPromotor() {
        boolean contains = false;
        for (Perfil p : getPerfis()) {
            if (p.getPerfil().equals(Constants.PROMOTOR)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
    
    public void addPerfil(Perfil value) {
        if (value == null) return;
        
        if (!perfis.contains(value)) {
            perfis.add(value);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return !((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName)));
    }

    @Override
    public String toString() {
        return userName;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(List<Perfil> perfilList) {
        this.perfis = perfilList;
    }

}
