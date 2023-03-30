/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;
import ritual.util.DateUtils;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBPGTOCLIENTE")
@NamedQueries({
    @NamedQuery(name = "PgtoCliente.findAll", query = "SELECT p FROM PgtoCliente p"),
    @NamedQuery(name = "PgtoCliente.findByIdPgtoCliente", query = "SELECT p FROM PgtoCliente p WHERE p.idPgtoCliente = :idPgtoCliente"),
    @NamedQuery(name = "PgtoCliente.findByDtVencimento", query = "SELECT p FROM PgtoCliente p WHERE p.dtVencimento = :dtVencimento"),
    @NamedQuery(name = "PgtoCliente.findByDtPgto", query = "SELECT p FROM PgtoCliente p WHERE p.dtPgto = :dtPgto")
})
public class PgtoCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "TBPGTOCLIENTE_IDPGTOCLIENTE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBPGTOCLIENTE_IDPGTOCLIENTE_SEQ", sequenceName = "TBPGTOCLIENTE_IDPGTOCLIENTE_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDPGTOCLIENTE", nullable = false)
    private Integer idPgtoCliente;
    @Basic(optional = false)
    @Column(name = "VENC", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtVencimento;
    @Column(name = "COMPLEMEN", length = 30)
    private String complemento;
    @Basic(optional = false)
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "TPPGTO", length = 2)
    private String tipoPgto;
    @Column(name = "DTPGTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtPgto;
    @Column(name = "DTPREVPG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtPrevisaoPgto;
    @Column(name = "VLRPGTO", precision = 10, scale = 2)
    private BigDecimal valorPgto;
    //@Column(name = "VALORCOBRANCA", precision = 10, scale = 2)
    //private BigDecimal valorCobranca;
    @Column(name = "COBRANCA", length = 1)
    private String cobranca;
    @Lob
    @Column(name = "OBSERVACAO", length = 0)
    private String observacao;
    @JoinColumns({
        @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO", nullable = false),
        @JoinColumn(name = "NF", referencedColumnName = "NF", nullable = false)})
    @ManyToOne(optional = false)
    private AtendimentoPedido atendimentoPedido;
    @JoinColumn(name = "IDCONTAREPRES", referencedColumnName = "IDCONTAREPRES")
    @ManyToOne
    private ContaRepres contaRepres;

    public PgtoCliente() {
        cobranca = "1";
    }

    public PgtoCliente(Integer idpgtocliente) {
        this.idPgtoCliente = idpgtocliente;
    }

    public PgtoCliente(Integer idpgtocliente, Date venc, BigDecimal valor) {
        this.idPgtoCliente = idpgtocliente;
        this.dtVencimento = venc;
        this.valor = valor;
    }

    public Integer getIdPgtoCliente() {
        return idPgtoCliente;
    }

    public void setIdPgtoCliente(Integer idpgtocliente) {
        this.idPgtoCliente = idpgtocliente;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date venc) {
        this.dtVencimento = venc;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemen) {
        this.complemento = complemen;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipoPgto() {
        return tipoPgto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao.toLowerCase();
    }
    
    public String getDescarga() {
        if (atendimentoPedido.getDtFrete() == null)
            return "DESCARGA";
        else
            return DateUtils.format(atendimentoPedido.getDtFrete());
    }
        
    public Boolean getImprimeBoletoFrete() {
        return "1".equals(atendimentoPedido.getBoletoFrete());
    }
    public Boolean getImprimeTransportador() {
        Map map = new HashMap();
        map.put("F", "Frete");
        map.put("FT", "Frete");       
        map.put("IC", "ICMS frete");
        return map.containsKey(tipoPgto.trim());
    }
    
    public Boolean getImprimeConta() {
        Map map = new HashMap();
        map.put("DP", "Depósito");
        map.put("D", "Depósito");
        map.put("P", "Depósito");
        map.put("PS", "Depósito");
        return map.containsKey(tipoPgto.trim());
    }
    
    public String getDetalheConta() {
        Map map = new HashMap();
        map.put("BO", "Cobrança bancária");
        map.put("BF", "Cobrança bancária");
        map.put("CC", "Cheque Correio");
        map.put("CT", "Cheque transportador");
        map.put("CH", "Pagamento ao vendedor");
        map.put("CA", "Cartório");
        String s;
        if (map.containsKey(tipoPgto.trim())) {
            s = (String) map.get(tipoPgto.trim());

        } else {
            s = "";

        }
        return s;
    }
    
    public String getContaExtenso() {
        if (contaRepres == null) {
            return null;
        }
        
        String result;
        
        if (contaRepres.getTipoConta().equals("PIX")) {
                result = contaRepres.getContaCorrente();
        } else if (getImprimeTransportador()) {
            if (getImprimeBoletoFrete()) {
                result = "Cobrança bancária";
            } else {
                result = "Pgto ao transportador";
            }
        } else if (getImprimeConta()) {
            result = contaRepres.getBanco().getNome() + " AG " + contaRepres.getAgencia() + " CC " + contaRepres.getContaCorrente();
        } else {
            result = getDetalheConta();
        }

        return result;
    }

    public String getTipoPgtoExtenso() {
        if (contaRepres == null) {
            return null;
        }
        
        if (contaRepres.getTipoConta().equals("PIX")) {
            return "PIX";
        }
        
        Map map = new HashMap();
        map.put("F", "Frete");
        map.put("FT", "Frete");
        map.put("BO", "Boleto");
        map.put("BF", "Boleto");
        map.put("DS", "Desconto");
        map.put("CH", "CH");
        map.put("DP", "Dep");
        map.put("D", "Dep");
        map.put("P", "Dep");
        map.put("PS", "Dep");
        map.put("PX", "PIX");
        
        String s;
        if (map.containsKey(tipoPgto.trim())) {
            s = (String) map.get(tipoPgto.trim());

        } else {
            s = tipoPgto;

        }
        return s;
    }

    public void setTipoPgto(String tppgto) {
        this.tipoPgto = tppgto;
    }

    public Date getDtPgto() {
        return dtPgto;
    }

    public void setDtPgto(Date dtpgto) {
        this.dtPgto = dtpgto;
    }

    public Date getDtPrevisaoPgto() {
        return dtPrevisaoPgto;
    }

    public void setDtPrevisaoPgto(Date dtprevpg) {
        this.dtPrevisaoPgto = dtprevpg;
    }

    public BigDecimal getValorPgto() {
        return valorPgto;
    }

    public void setValorPgto(BigDecimal vlrpgto) {
        this.valorPgto = vlrpgto;
    }

    public AtendimentoPedido getAtendimentoPedido() {
        return atendimentoPedido;
    }

    public void setAtendimentoPedido(AtendimentoPedido atendimentoPedido) {
        this.atendimentoPedido = atendimentoPedido;
    }

    public ContaRepres getContaRepres() {
        return contaRepres;
    }

    public void setContaRepres(ContaRepres idcontarepres) {
        this.contaRepres = idcontarepres;
    }

    public String getCobranca() {
        return cobranca;
    }

    public void setCobranca(String cobranca) {
        this.cobranca = cobranca;
    }

    public void setCobranca(boolean value) {
        if (value) {
            this.cobranca = "1";
        } else {
            this.cobranca = "0";
        }
    }

    public BigDecimal getJuros() {
        if ("CH".equals(tipoPgto)) {
            if (atendimentoPedido.getPercDesconto() != null) {
                BigDecimal percentual = valor.divide(BigDecimal.valueOf(100l)).multiply(atendimentoPedido.getPercDesconto());
                BigDecimal valor30 = percentual.divide(BigDecimal.valueOf(30l), 2);
                return valor30.multiply(new BigDecimal(getDias().longValue()));
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getLiquido() {
        if ("CH".equals(tipoPgto)) {
            return valor.subtract(getJuros());
        }
        return BigDecimal.ZERO;

    }

    public Integer getDias() {
        if ("CH".equals(tipoPgto)) {
            if (atendimentoPedido.getDtDesconto() != null) {
                return diferencaEmDias(atendimentoPedido.getDtDesconto(), dtVencimento);
            }

        }
        return 0;
    }

    public Boolean getImprimirCobranca() {
        return "1".equals(cobranca);
    }
    /*
    public BigDecimal getValorCobranca() {
    return valorCobranca;
    }

    public void setValorCobranca(BigDecimal valorCobranca) {
    this.valorCobranca = valorCobranca;
    }
     */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPgtoCliente != null ? idPgtoCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PgtoCliente)) {
            return false;
        }
        PgtoCliente other = (PgtoCliente) object;
        if ((this.idPgtoCliente == null && other.idPgtoCliente != null) || (this.idPgtoCliente != null && !this.idPgtoCliente.equals(other.idPgtoCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.PgtoCliente[idpgtocliente=" + idPgtoCliente + "]";
    }

    public int diferencaEmDias(Date dataLow, Date dataHigh) {

        GregorianCalendar startTime = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();

        GregorianCalendar curTime = new GregorianCalendar();
        GregorianCalendar baseTime = new GregorianCalendar();

        startTime.setTime(dataLow);
        endTime.setTime(dataHigh);

        int dif_multiplier = 1;

        // Verifica a ordem de inicio das datas  
        if (dataLow.compareTo(dataHigh) < 0) {
            baseTime.setTime(dataHigh);
            curTime.setTime(dataLow);
            dif_multiplier = 1;
        } else {
            baseTime.setTime(dataLow);
            curTime.setTime(dataHigh);
            dif_multiplier = -1;
        }

        int result_years = 0;
        int result_months = 0;
        int result_days = 0;

        // Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import acumulando  
        // no total de dias. Ja leva em consideracao ano bissesto  
        while (curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR)
                || curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)) {

            int max_day = curTime.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            result_months += max_day;
            curTime.add(GregorianCalendar.MONTH, 1);

        }

        // Marca que é um saldo negativo ou positivo  
        result_months = result_months * dif_multiplier;


        // Retirna a diferenca de dias do total dos meses  
        result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));

        return result_years + result_months + result_days;

    }

}
