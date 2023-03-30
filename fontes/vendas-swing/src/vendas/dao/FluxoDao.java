/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import ritual.util.DateUtils;
import vendas.beans.MovimentoFilter;
import vendas.beans.ResumoMovimento;
import vendas.entity.ContaFluxo;
import vendas.entity.Movimento;
import vendas.entity.Vendedor;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;
import vendas.util.StringUtils;

/**
 *
 * @author sam
 */
public class FluxoDao extends BaseDao {
    
    public List<ContaFluxo> getContasFluxo(Integer idVendedor) throws DAOException {
        getLogger().info("findMovimento");
        if (idVendedor == null)
            return new ArrayList<ContaFluxo>();
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(ContaFluxo.class);
        Criteria vendedorCriteria = criteria.createCriteria("vendedor");
        vendedorCriteria.add(Restrictions.eq("idVendedor", idVendedor));
        return criteria.list();
    }

    public void deleteConta(ContaFluxo conta) throws DAOException {
        getLogger().info("deleteConta");
        EntityManager em = getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Query q = em.createQuery("delete from Movimento where contaFluxo.idConta = :conta");
            q.setParameter("conta", conta.getIdConta());
            q.executeUpdate();
            q = em.createQuery("delete from ContaFluxo where idConta = :conta");
            q.setParameter("conta", conta.getIdConta());
            q.executeUpdate();
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }
    
    public BigDecimal getTotalSaldo(Integer idConta, Integer idVendedor) {
        getLogger().info("getTotalSaldo");
        BigDecimal debito;
        BigDecimal credito;
        Query q = ManagerFactory.getEntityManager().createQuery("select sum(o.valor) from Movimento o where o.tipo = :tipo and o.contaFluxo.idConta = :conta and o.dtMov <= :mov and o.vendedor.idVendedor = :vendedor");
        q.setParameter("tipo", 0);
        q.setParameter("conta", idConta);
        q.setParameter("mov", DateUtils.getNextDate(DateUtils.getCurrentDate(), -1));
        q.setParameter("vendedor", idVendedor);
        Object obj = q.getSingleResult();
        debito = (BigDecimal)obj;
        if (debito == null)
            debito = BigDecimal.ZERO;
        q.setParameter("tipo", 1);
        obj = q.getSingleResult();
        credito = (BigDecimal)obj;
        if (credito == null)
            credito = BigDecimal.ZERO;
        return credito.subtract(debito);
    }
    
    public BigDecimal getTotalSaldoBancario(Integer idVendedor) {
        getLogger().info("getTotalSaldoBancario");
        BigDecimal debito;
        BigDecimal credito;
        Query q = ManagerFactory.getEntityManager().createQuery("select sum(o.valor) from Movimento o where o.tipo = :tipo and o.contaFluxo.indicaContaBancaria = :conta and o.dtMov <= :mov and o.vendedor.idVendedor = :vendedor");
        q.setParameter("tipo", 0);
        q.setParameter("conta", "S");
        q.setParameter("mov", DateUtils.getNextDate(DateUtils.getCurrentDate(), -1));
        q.setParameter("vendedor", idVendedor);
        Object obj = q.getSingleResult();
        debito = (BigDecimal)obj;
        if (debito == null)
            debito = BigDecimal.ZERO;
        q.setParameter("tipo", 1);
        obj = q.getSingleResult();
        credito = (BigDecimal)obj;
        if (credito == null)
            credito = BigDecimal.ZERO;
        return credito.subtract(debito);
    }
    
    public BigDecimal getTotalGeral(Integer idVendedor) {
        getLogger().info("getTotalGeral");
        BigDecimal debito;
        BigDecimal credito;
        Query q = ManagerFactory.getEntityManager().createQuery("select sum(o.valor) from Movimento o where o.tipo = :tipo and o.contaFluxo.indicaContaBancaria = :conta and o.vendedor.idVendedor = :vendedor");
        q.setParameter("tipo", 0);
        q.setParameter("conta", "S");
        q.setParameter("vendedor", idVendedor);
        Object obj = q.getSingleResult();
        debito = (BigDecimal)obj;
        if (debito == null)
            debito = BigDecimal.ZERO;
        q.setParameter("tipo", 1);
        obj = q.getSingleResult();
        credito = (BigDecimal)obj;
        if (credito == null)
            credito = BigDecimal.ZERO;
        return credito.subtract(debito);
    }
    
    public BigDecimal getSaldoMovimento(Movimento mov) {
        getLogger().info("getSaldoMovimento");
        BigDecimal debito;
        BigDecimal credito;
        Query q = ManagerFactory.getEntityManager().createQuery("select sum(o.valor) from Movimento o where o.tipo = :tipo and o.contaFluxo.idConta = :conta and o.dtMov <= :mov and o.vendedor.idVendedor = :vendedor");
        q.setParameter("tipo", 0);
        q.setParameter("conta", mov.getContaFluxo().getIdConta());
        q.setParameter("mov", mov.getDtMov());
        q.setParameter("vendedor", mov.getVendedor().getIdVendedor());
        Object obj = q.getSingleResult();
        debito = (BigDecimal)obj;
        if (debito == null)
            debito = BigDecimal.ZERO;
        q.setParameter("tipo", 1);
        obj = q.getSingleResult();
        credito = (BigDecimal)obj;
        if (credito == null)
            credito = BigDecimal.ZERO;
        return credito.subtract(debito);
    }
    
    public List<Movimento> findMovimento(MovimentoFilter filtro) {        
        getLogger().info("findMovimento");
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Movimento.class);
        Criteria vendedorCriteria = criteria.createCriteria("vendedor");
        vendedorCriteria.add(Restrictions.eq("idVendedor", filtro.getVendedor()));
        if (filtro.getConta() != null) {
            Criteria contaCriteria = criteria.createCriteria("contaFluxo");
            if ((filtro.getConta() != null) && (filtro.getConta().getIdConta() != null)) {
                contaCriteria.add(Restrictions.eq("idConta", filtro.getConta().getIdConta()));
            }
        }
        if (filtro.getTipo() != -1) {
            criteria.add(Restrictions.eq("tipo", filtro.getTipo())); 
        }
        if (filtro.getDtInicio() != null && filtro.getDtTermino() != null) {
            criteria.add(Restrictions.between("dtMov", filtro.getDtInicio(), filtro.getDtTermino()));
        } else if (filtro.getDtInicio() != null) {
            criteria.add(Restrictions.ge("dtMov", filtro.getDtInicio()));
        }
        
        if (filtro.getGrupo() != null) {
            Criteria grupoCriteria = criteria.createCriteria("grupoMovimento");
            grupoCriteria.add(Restrictions.eq("idgrupo", filtro.getGrupo().getIdgrupo()));
        }
        
        criteria.addOrder(Order.asc("dtMov")).addOrder(Order.desc("tipo")).addOrder(Order.asc("idMovimento"));
        return criteria.list();        
    }
    
    public Movimento findResumoMovimento(MovimentoFilter mov) {
        getLogger().info("findResumoMovimento ini");
        
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(case tipo when '1' then 0 else valor end) as debito, sum(case tipo when '0' then 0 else valor end) as credito");
        sb.append(" from tbmovimento");
        sb.append(" where idvendedor = :vendedor ");
        sb.append(" and (:conta is null or idconta = :conta) ");
        sb.append(" and (:grupo is null or idgrupo = :grupo) ");
        
        if (mov.getDtInicio() != null && mov.getDtTermino() != null) {
            sb.append(" and dtmov between :dtini and :dtend");
        } else if (mov.getDtInicio() != null) {
            sb.append(" and dtmov >= :dtini");
        }
        
        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        q.setParameter("vendedor", mov.getVendedor());
        
        if (mov.getConta() != null && mov.getConta().getIdConta() != null) {
            q.setParameter("conta", mov.getConta().getIdConta());
        } else {
            q.setParameter("conta", null);
        }
        
        if (mov.getGrupo() != null && mov.getGrupo().getIdgrupo() != null) {
            q.setParameter("grupo", mov.getGrupo().getIdgrupo());
        } else {
            q.setParameter("grupo", null);
        }
        
        if (mov.getDtInicio() != null && mov.getDtTermino() != null) {
            q.setParameter("dtini", mov.getDtInicio());
            q.setParameter("dtend", mov.getDtTermino());
        } else {
            q.setParameter("dtini", mov.getDtInicio());
        }
        
        List<Object[]> lista = q.list();
        
        if (lista.isEmpty()) return null;

        Movimento result = null;
        Integer ano;
        Integer mes;
        
        for (Object[] item : lista) {
            result = new Movimento();
            
            result.setDebito((BigDecimal) item[0]);
            result.setCredito((BigDecimal) item[1]);
            break;
        }

        return result;
        
    }
    
    public List<ResumoMovimento> findResumoGrupoMovimento(MovimentoFilter mov) {
        StringBuilder sb = new StringBuilder();
        String intervalo;
        
        if (mov.getDtTermino() != null) {
            intervalo = "        AND (m.DTMOV between :dtini and :dtend)";
        } else {
            intervalo = "        AND (m.DTMOV >= :dtini)";
        }
        
        sb.append(" SELECT CASE when c.nomeGrupo IS NULL then d.nomeGrupo ELSE c.nomegrupo END AS nomeGrupo, ");
        sb.append(" 	c.credito, d.debito");
        sb.append(" FROM (");
        sb.append(" 	SELECT g.nomeGrupo, sum(m.valor) AS credito");
        sb.append(" 	FROM tbmovimento m ");
        sb.append(" 	INNER JOIN TBGRUPOMOVIMENTO g  ON m.idgrupo = g.idgrupo ");
        sb.append("     INNER JOIN TBCONTAFLUXO cf on cf.idconta = m.idconta");
        sb.append(" 	WHERE m.idvendedor = :vendedor");
        sb.append("        AND m.tipo = 1");
        
        if (mov.getConta() != null && mov.getConta().getIdConta() != null) {
            sb.append("        AND m.IDCONTA = :conta");
        } else {
            sb.append("        AND cf.in_conta_bancaria = 'S'");
        }
        
        sb.append(intervalo);
        sb.append(" 	GROUP BY g.nomeGrupo) c");
        sb.append(" FULL JOIN ( ");
        sb.append(" 	SELECT g.nomeGrupo, sum(m.valor) AS debito");
        sb.append(" 	FROM tbmovimento m ");
        sb.append(" 	INNER JOIN TBGRUPOMOVIMENTO g  ON m.idgrupo = g.idgrupo ");
        sb.append("     INNER JOIN TBCONTAFLUXO cf on cf.idconta = m.idconta");
        sb.append(" 	WHERE m.idvendedor = :vendedor");
        sb.append("        AND m.tipo = 0");
        
        if (mov.getConta() != null && mov.getConta().getIdConta() != null) {
            sb.append("        AND m.IDCONTA = :conta");
        } else {
            sb.append("        AND cf.in_conta_bancaria = 'S'");
        }
        
        sb.append(intervalo);
        sb.append(" 	GROUP BY g.nomeGrupo) d ON c.nomegrupo = d.nomegrupo ");
        sb.append(" ORDER BY 1");
        
        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        q.setParameter("vendedor", mov.getVendedor());
        q.setParameter("dtini", mov.getDtInicio());
        
        if (mov.getDtTermino() != null) {
            q.setParameter("dtend", mov.getDtTermino());
        }
        
        if (mov.getConta() != null && mov.getConta().getIdConta() != null) {
            q.setParameter("conta", mov.getConta().getIdConta());
        }
        
        List<Object[]> lista = q.list();
        
        if (lista.isEmpty()) return null;
        
        List<ResumoMovimento> result = new ArrayList<>();
        ResumoMovimento to;
        
        for (Object[] item : lista) {
            to = new ResumoMovimento();
            if (mov.getConta() != null && mov.getConta().getIdConta() != null) {
                to.setConta(mov.getConta().getNome());
            } else {
                to.setConta("");
            }
            to.setGrupoMovimento((String) item[0]);
            to.setValorCredito((BigDecimal) item[1]);
            to.setValorDebito((BigDecimal) item[2]);
            
            result.add(to);
        }
        
        return result;
    }
    
    public Movimento findResumoGeralMovimento(MovimentoFilter mov) {
        getLogger().info("findResumoGeralMovimento ini");
        
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(case tipo when '1' then 0 else valor end) as debito, sum(case tipo when '0' then 0 else valor end) as credito");
        sb.append(" from tbmovimento");
        sb.append(" where idvendedor = :vendedor and ");
        
        if (mov.getDtInicio() != null && mov.getDtTermino() != null) {
            sb.append("dtmov between :dtini and :dtend");
        } else if (mov.getDtInicio() != null) {
            sb.append("dtmov >= :dtini");
        }
        
        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        q.setParameter("vendedor", mov.getVendedor());
        
        if (mov.getDtInicio() != null && mov.getDtTermino() != null) {
            q.setParameter("dtini", mov.getDtInicio());
            q.setParameter("dtend", mov.getDtTermino());
        } else {
            q.setParameter("dtini", mov.getDtInicio());
        }
        
        List<Object[]> lista = q.list();
        
        if (lista.isEmpty()) return null;

        Movimento result = null;
        Integer ano;
        Integer mes;
        
        for (Object[] item : lista) {
            result = new Movimento();
            
            result.setDebito((BigDecimal) item[0]);
            result.setCredito((BigDecimal) item[1]);
            break;
        }

        return result;
        
    }
    
    public List<ResumoMovimento> findHistoricoMovimento(Integer idVendedor, Integer idConta) {
        getLogger().info("findHistoricoMovimento ini");
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("select extract(year from dtmov) as ano, extract(month from dtmov) as mes, sum(case tipo when '1' then 0 else valor end) as debito, sum(case tipo when '0' then 0 else valor end) as credito");
        sb.append(" from tbmovimento");
        sb.append(" where idvendedor = :vendedor and idconta = :conta");
        sb.append(" group by 1, 2");
        sb.append(" order by 1 desc, 2 desc");
        
        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());
        
        q.setParameter("vendedor", idVendedor);
        q.setParameter("conta", idConta);
        
        List<Object[]> lista = q.list();
        
        if (lista.isEmpty()) return null;

        List<ResumoMovimento> result = new ArrayList<>();
        ResumoMovimento resumo;
        Integer ano;
        Integer mes;
        
        for (Object[] item : lista) {
            resumo = new ResumoMovimento();
            ano = (Integer) item[0];
            mes = (Integer) item[1];
            resumo.setAnoMes(ano.toString() + "/" + StringUtils.padl(mes.toString(), 2, "0"));
            resumo.setValorDebito((BigDecimal) item[2]);
            resumo.setValorCredito((BigDecimal) item[3]);
            result.add(resumo);
        }

        return result;
    }

    public Vendedor getVendedor(Integer idVendedor) {
        try {
            return (Vendedor)findById(Vendedor.class, idVendedor);
        } catch (DAOException e) {
            getLogger().error(e);
            return null;
        }
    }
    
    public void exportarLancamento(List <Movimento> lista, int month) throws Exception {
        for (int j = 0; j < month; j++) {
            for (Movimento compromisso: lista) {
                Movimento conta = new Movimento();
                
                conta.setContaFluxo(compromisso.getContaFluxo());
                conta.setCredito(compromisso.getCredito());
                conta.setDebito(compromisso.getDebito());
                conta.setValor(compromisso.getValor());
                conta.setDocumento(compromisso.getDocumento());
                conta.setDescricao(compromisso.getDescricao());
                conta.setDtMov(DateUtils.addMonthToDate(compromisso.getDtMov(), j + 1));
                conta.setObs(compromisso.getObs());
                conta.setTipo(compromisso.getTipo());
                conta.setValor(compromisso.getValor());
                conta.setVendedor(compromisso.getVendedor());
                conta.setGrupoMovimento(compromisso.getGrupoMovimento());
                
                insertRecord(conta);
            }
        }
    } 
}
