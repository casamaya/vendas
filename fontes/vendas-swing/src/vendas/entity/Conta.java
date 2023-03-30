/*
 * Conta.java
 * 
 * Created on 24/10/2007, 22:15:16
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBPLANOCONTA")
@NamedQueries({})
public class Conta implements Serializable {
    @Id
    @GeneratedValue(generator = "TBPLANOCONTA_IDCONTA_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBPLANOCONTA_IDCONTA_SEQ", sequenceName = "TBPLANOCONTA_IDCONTA_SEQ", allocationSize = 1)
    @Column(name = "IDCONTA", nullable = false)
    private Integer idConta;
    @Column(name = "NOME", nullable = false, length=40)
    private String nome;
    @Column(name = "TIPO", nullable = false)
    private short tipo;
    @Column(name = "GRUPO", nullable = false)
    private short grupo;
    @OneToMany(mappedBy = "conta", cascade=CascadeType.REMOVE)
    private Collection<AReceber> aReceberCollection;
    @OneToMany(mappedBy = "contaMaster", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
    private Collection<Conta> contaCollection;
    @JoinColumn(name = "IDContaMaster", referencedColumnName = "IDCONTA")
    @ManyToOne
    private Conta contaMaster;
    @OneToMany(mappedBy = "conta", cascade=CascadeType.REMOVE)
    private Collection<APagar> aPagarCollection;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;

    public Conta() {
    }

    public Conta(Integer idConta) {
        this.idConta = idConta;
    }

    public Conta(Integer idConta, String nome, short tipo, short grupo) {
        this.idConta = idConta;
        this.nome = nome;
        this.tipo = tipo;
        this.grupo = grupo;
    }

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public short getGrupo() {
        return grupo;
    }

    public void setGrupo(short grupo) {
        this.grupo = grupo;
    }

    public Collection<AReceber> getContasAReceber() {
        return aReceberCollection;
    }

    public void setContasAReceber(Collection<AReceber> aReceberCollection) {
        this.aReceberCollection = aReceberCollection;
    }

    public Collection<Conta> getContas() {
        return contaCollection;
    }

    public void setContas(Collection<Conta> contaCollection) {
        this.contaCollection = contaCollection;
    }

    public Conta getContaMaster() {
        return contaMaster;
    }

    public void setContaMaster(Conta idContaMaster) {
        this.contaMaster = idContaMaster;
    }

    public Collection<APagar> getContasAPagar() {
        return aPagarCollection;
    }

    public void setContasAPagar(Collection<APagar> aPagarCollection) {
        this.aPagarCollection = aPagarCollection;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    
    public Conta getChildAt(int index) {
        Conta conta = null;
        Iterator i = contaCollection.iterator();
        int n = 0;
        while (i.hasNext()) {
            if (n == index) {
                conta = (Conta)i.next();
                break;
            }
        }
        return conta;
    }
    
    public int getIndexOfChild(Conta child) {
        Iterator i = contaCollection.iterator();
        int n = 0;
        Conta conta;
        while (i.hasNext()) {
            conta = (Conta)i.next();
            if (conta.getIdConta().intValue() == child.getIdConta().intValue()) {
                break;
            } else
                n++;
        }
        return n;
    }
    
    public int getChildCount() {
        if (contaCollection == null)
            return 0;
        else
            return contaCollection.size();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConta != null ? idConta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conta)) {
            return false;
        }
        Conta other = (Conta) object;
        if ((this.idConta == null && other.idConta != null) || (this.idConta != null && !this.idConta.equals(other.idConta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
