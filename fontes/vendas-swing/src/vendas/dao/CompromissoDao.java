/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import vendas.beans.CompromissoFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import ritual.util.DateUtils;
import vendas.entity.Compromisso;
import vendas.entity.Contabil;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class CompromissoDao extends BaseDao {

    public List findByExample(Object obj, boolean orderByMaster) throws Exception {
        return null;
    }
    
    @Override
    public List findAll() throws DAOException {
        return findByExample(new CompromissoFilter());
    }
    
    public BigDecimal getTotais(Class c, CompromissoFilter apagar) {
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(o.valor) from ").append(c.getName()).append(" o where");
        sb.append(" o.dtVencimento between :d1 and :d2");
        sb.append(" and o.conta.vendedor.idVendedor = ").append(apagar.getConta().getVendedor().getIdVendedor());
        if ((apagar.getConta() != null) && (!"TODOS".equals(apagar.getConta().getNome())))
            sb.append(" and o.conta.idConta = " + apagar.getConta().getIdConta());
        if (apagar.getTipoPgto() != null)
            sb.append(" and o.tipoPgto.idTipoPgto = " + apagar.getTipoPgto().getIdTipoPgto());
        if (apagar.getSituacao() == 1)
            sb.append(" and o.dtPgto is null");
        if (apagar.getSituacao() == 2)
            sb.append(" and o.dtPgto is not null");
        Query query = getEntityManager().createQuery(sb.toString());
        query.setParameter("d1", apagar.getDtIni());
        query.setParameter("d2", apagar.getDtEnd());
        List sumResult = query.getResultList();
        if (sumResult.isEmpty())
            return new BigDecimal(0);
        else
            return (BigDecimal)sumResult.get(0);
    }

    public List findFechamento(Class c, CompromissoFilter apagar) {
        getLogger().info("findFechamento");
        StringBuilder sb = new StringBuilder();
        sb.append("select m.nome, sum(c.valor) from ").append(c.getName()).append(" c, Conta p, Conta m")
        .append(" where c.conta.vendedor.idVendedor = ").append(apagar.getConta().getVendedor().getIdVendedor());
        sb.append(" and c.dtVencimento between :d1 and :d2");

        if (apagar.getTipoPgto() != null)
            sb.append(" and c.tipoPgto.idTipoPgto = ").append(apagar.getTipoPgto().getIdTipoPgto());
        if (apagar.getSituacao() == 1)
            sb.append(" and c.dtPgto is null");
        if (apagar.getSituacao() == 2)
            sb.append(" and c.dtPgto is not null");

        sb.append(" and c.conta.idConta = p.idConta and m.idConta = p.contaMaster.idConta");
        sb.append(" and c.conta.vendedor.idVendedor = ").append(apagar.getConta().getVendedor().getIdVendedor());

        sb.append(" group by m.nome");
        sb.append(" order by 2 desc");
        
        Query query = getEntityManager().createQuery(sb.toString());
        query.setParameter("d1", apagar.getDtIni());
        query.setParameter("d2", apagar.getDtEnd());
        
        List<Object[]> lista = query.getResultList();
        
        return lista;
    }
    
    public List getContabil(int ano, int mes, int index) {
        getLogger().info("getContabil ini");
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Contabil.class);

        criteria.add(Restrictions.eq("numAno", ano));
        criteria.add(Restrictions.eq("numMes", mes));
        criteria.add(Restrictions.eq("idTipo", index));
        
        if (index == 1)
            criteria.addOrder(Order.asc("compromisso"));
        else
            criteria.addOrder(Order.asc("dtVencimento"));

        return criteria.list();
    }
    
    public List findCompromisso(Class c, CompromissoFilter apagar, boolean orderByMaster) {
        getLogger().info("findCompromisso");
        StringBuilder sb = new StringBuilder();
        sb.append("select o from ").append(c.getName()).append(" o where ");

        List<String> lista = new ArrayList<>();
        lista.add(" o.conta.vendedor.idVendedor = " + apagar.getConta().getVendedor().getIdVendedor());

        if (apagar.getDtIni() != null && apagar.getDtEnd() == null)
            lista.add(" o.dtVencimento >= :d1");
        else if (apagar.getDtIni() != null)
            lista.add(" o.dtVencimento between :d1 and :d2");
        if ((apagar.getConta() != null) && (apagar.getConta().getIdConta() != null))
            lista.add(" o.conta.idConta = " + apagar.getConta().getIdConta());
        if ((apagar.getGrupo() != null) && (apagar.getGrupo().getIdConta() != null))
            lista.add(" o.conta.contaMaster.idConta = " + apagar.getGrupo().getIdConta());
        if (apagar.getTipoPgto() != null)
            lista.add(" o.tipoPgto.idTipoPgto = " + apagar.getTipoPgto().getIdTipoPgto());
        if (apagar.getSituacao() == 1)
            lista.add(" o.dtPgto is null");
        if (apagar.getSituacao() == 2)
            lista.add(" o.dtPgto is not null");
        if (apagar.getDtIniPgto() != null) {
            lista.add(" o.dtPgto between :p1 and :p2");
        }
        int i = 0;
        for (String s : lista) {
            if (i++ > 0)
                sb.append(" and ");
            sb.append(s);
        }
        if (orderByMaster)
            sb.append(" order by o.conta.contaMaster.nome, o.conta.nome, o.dtVencimento");
        else
            sb.append(" order by o.dtVencimento");
        getLogger().info(sb.toString());
        Query query = getEntityManager().createQuery(sb.toString());
        if (apagar.getDtIni() != null && apagar.getDtEnd() == null)
            query.setParameter("d1", apagar.getDtIni());
        else if (apagar.getDtIni() != null) {
            query.setParameter("d1", apagar.getDtIni());
            query.setParameter("d2", apagar.getDtEnd());
        }
        if (apagar.getDtIniPgto() != null) {
            query.setParameter("p1", apagar.getDtIniPgto());
            query.setParameter("p2", apagar.getDtEndPgto());            
        }
        return query.getResultList();
    }

    public void exportarLancamento(List <Compromisso> lista, int month) throws Exception {
    }
    
    public void exportarLancamento(List <Compromisso> lista, Class nameClass, int month) throws Exception {
        for (int j = 0; j < month; j++) {
            for (Compromisso compromisso: lista) {
                Compromisso conta = (Compromisso)nameClass.newInstance();
                conta.setDtVencimento(DateUtils.addMonthToDate(compromisso.getDtVencimento(), j + 1));
                conta.setValor(compromisso.getValor());
                conta.setConta(compromisso.getConta());
                conta.setTipoPgto(compromisso.getTipoPgtoFinanceiro());
                conta.setObservacao(compromisso.getObservacao());
                insertRecord(conta);
            }
        }
    }    
    
    public void exportarLancamento(Class nameClass, int month, int year, int repeat) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        Date iniDate = calendar.getTime();
        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        CompromissoFilter filter = new CompromissoFilter();
        filter.setDtIni(iniDate);
        filter.setDtEnd(endDate);
        List <Compromisso> lista = findByExample(filter, false);
            
        for (Compromisso compromisso: lista) {
            for (int j = 0; j < repeat; j++) {
                Compromisso conta = (Compromisso)nameClass.newInstance();
                conta.setDtVencimento(DateUtils.addMonthToDate(compromisso.getDtVencimento(), j + 1));
                conta.setValor(compromisso.getValor());
                conta.setConta(compromisso.getConta());
                conta.setTipoPgto(compromisso.getTipoPgtoFinanceiro());
                conta.setObservacao(compromisso.getObservacao());
                insertRecord(conta);
            }
        }
    }    
}
