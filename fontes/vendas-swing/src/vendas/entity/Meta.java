/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBMETA")
public class Meta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ANOMES", nullable = false)
    private String anoMes;
    @Column(name = "VALORMETA", precision = 10, scale = 2)
    private BigDecimal valorMeta;

    public String getAnoMes() {
        return anoMes;
    }

    public String getMesAno() {
        return anoMes.substring(4) + "/" + anoMes.substring(0,4) ;
    }

    public void setAnoMes(String anoMes) {
        this.anoMes = anoMes;
    }

    public BigDecimal getValorMeta() {
        return valorMeta;
    }

    public void setValorMeta(BigDecimal valorMeta) {
        this.valorMeta = valorMeta;
    }
    
    public static void main(String[] args) {
        String anoMes = "201104";
        System.out.println(anoMes.substring(4));
        System.out.println(anoMes.substring(0, 4)); //ano
    }
 }
