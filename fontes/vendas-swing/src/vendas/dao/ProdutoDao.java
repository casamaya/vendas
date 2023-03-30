/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import ritual.util.DateUtils;
import vendas.beans.AlteraPreco;
import vendas.beans.ProdutoFilter;
import vendas.entity.Produto;
import vendas.entity.RepresProduto;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class ProdutoDao extends BaseDao {

    public Integer getNextValue() {
        String s = "select gen_id( TBPRODUTO_IDPRODUTO_SEQ, 1 ) from RDB$DATABASE";
        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(s);
        BigInteger bi = (BigInteger) q.uniqueResult();

        return bi.intValue();

    }

    @Override
    public void insertRecord(Object object) throws DAOException {
        if (object instanceof Produto) {
            Produto produto = (Produto) object;
            if (produto.getIdProduto() == null) {
                produto.setIdProduto(getNextValue());
            }
            super.insertRecord(produto);
        } else {
            super.insertRecord(object);
        }
    }

    public boolean alterarPreco(AlteraPreco value) {
        org.hibernate.Query q;
        Session session = ManagerFactory.getSession();
        boolean result = false;
        if (value.getTipo() == 0) {
            q = session.createQuery("update RepresProduto p set p.preco = :valor where p.repres.idRepres = :repres");
        } else {
            q = session.createQuery("update RepresProduto p set p.preco = p.preco + (p.preco * :valor / 100) where p.repres.idRepres = :repres");
        }
        q.setParameter("valor", value.getValor());
        q.setParameter("repres", value.getRepres().getIdRepres());


        Transaction newTransaction = null;
        try {
            newTransaction = session.beginTransaction();
            result = q.executeUpdate() > 0;
            newTransaction.commit();
            newTransaction = session.beginTransaction();
            q = session.createQuery("update RepresProduto p set p.precoFinal = p.preco2 + (p.preco2 / 100 * p.ipi) + p.frete where p.repres.idRepres = :repres");
            q.setParameter("repres", value.getRepres().getIdRepres());
            q.executeUpdate();
            newTransaction.commit();
            result = true;
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            result = false;
            try {
                if (newTransaction != null) {
                    newTransaction.rollback();
                }
            } catch (Exception ex) {
                getLogger().error(ex.getMessage(), ex);
            }
        }
        return result;
    }

    public boolean alterarComissao(AlteraPreco value) {
        org.hibernate.Query q;
        Session session = ManagerFactory.getSession();
        boolean result = false;
        if (value.getTipo() == 0) {
            q = session.createQuery("update RepresProduto p set p.percComissao = :valor where p.repres.idRepres = :repres");
        } else {
            q = session.createQuery("update RepresProduto p set p.percComissao = p.percComissao + (p.percComissao * :valor / 100) where p.repres.idRepres = :repres");
        }
        q.setParameter("valor", value.getValor());
        q.setParameter("repres", value.getRepres().getIdRepres());


        Transaction newTransaction = null;
        try {
            newTransaction = session.beginTransaction();
            result = q.executeUpdate() > 0;
            q = session.createQuery("update RepresProduto p set p.precoFinal = p.preco2 + (p.preco2 / 100 * p.ipi) + p.frete where p.repres.idRepres = :repres");
            q.setParameter("repres", value.getRepres().getIdRepres());
            q.executeUpdate();
            newTransaction.commit();
            result = true;
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            result = false;
            try {
                if (newTransaction != null) {
                    newTransaction.rollback();
                }
            } catch (Exception ex) {
                getLogger().error(ex.getMessage(), ex);
            }
        }
        return result;
    }
    
    private String getStringAlterarPrecoValor(AlteraPreco value) {
        StringBuilder sb = new StringBuilder();
        List<String> lista = new ArrayList();
        sb.append("update RepresProduto p set ");
        if (value.getAlteraComissao())
            lista.add(" p.percComissao = :percComissao");
        if (value.getAlteraPreco())
            lista.add(" p.preco = :preco");
        if (value.getAlteraPreco2())
            lista.add(" p.preco2 = :preco2");
        if (value.getAlteraPrecoFinal())
            lista.add(" p.precoFinal = :precoFinal");
        if (value.getAlteraFrete())
            lista.add(" p.frete = :frete");
        if (value.getAlteraIPI())
            lista.add(" p.ipi = :ipi");
        int i = 0;
        for (String s : lista) {
            if (i > 0)
                sb.append(",");
            sb.append(s);
            i++;
        }
        sb.append(" where p.repres.idRepres = :repres and p.produto.idProduto = :produto");
        return sb.toString();
    }
    
    public Boolean temProdutosBloqueados() {
        getLogger().info("temProdutosBloqueados ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select count(*) from Produto where bloqueado = 'S'");
        q.setMaxResults(1);
        Long result = (Long) q.getSingleResult();
        return result.intValue() > 0;
    }

    private String getStringAlterarPrecoPerc(AlteraPreco value) {
        StringBuilder sb = new StringBuilder();
        List<String> lista = new ArrayList();
        sb.append("update RepresProduto p set ");
        if (value.getAlteraComissao())
            lista.add(" p.percComissao = p.percComissao + (p.percComissao * :percComissao / 100)");
        if (value.getAlteraFrete())
            lista.add(" p.frete = p.frete + (p.frete * :frete / 100)");
        if (value.getAlteraPreco())
            lista.add(" p.preco = p.preco + (p.preco * :preco / 100)");
        if (value.getAlteraPreco2())
            lista.add(" p.preco2 = p.preco2 + (p.preco2 * :preco2 / 100)");
        if (value.getAlteraPrecoFinal())
            lista.add(" p.precoFinal = p.precoFinal + (p.precoFinal * :precoFinal / 100)");
        if (value.getAlteraIPI())
            lista.add(" p.ipi = p.ipi + (p.ipi * :ipi / 100)");
        int i = 0;
        for (String s : lista) {
            if (i > 0)
                sb.append(",");
            sb.append(s);
            i++;
        }
        sb.append(" where p.repres.idRepres = :repres and p.produto.idProduto = :produto");
        return sb.toString();
    }

    public boolean alterarPreco(AlteraPreco value, List<Integer> lista) {
        org.hibernate.Query q;
        Session session = ManagerFactory.getSession();
        boolean result = false;
        if (value.getTipo() == 0) {
            q = session.createQuery(getStringAlterarPrecoValor(value));
        } else {
            q = session.createQuery(getStringAlterarPrecoPerc(value));
        }

        Transaction newTransaction = null;
        try {
            newTransaction = session.beginTransaction();
            for (Integer row : lista) {
                if (value.getAlteraComissao())
                    q.setParameter("percComissao", value.getValor());
                if (value.getAlteraFrete())
                    q.setParameter("frete", value.getValor());
                if (value.getAlteraPreco())
                    q.setParameter("preco", value.getValor());
                if (value.getAlteraPreco2())
                    q.setParameter("preco2", value.getValor());
                if (value.getAlteraPrecoFinal())
                    q.setParameter("precoFinal", value.getValor());
                if (value.getAlteraIPI())
                    q.setParameter("ipi", value.getValor());
                q.setParameter("repres", value.getRepres().getIdRepres());
                q.setParameter("produto", row);
                q.executeUpdate();
            }
            newTransaction.commit();
            newTransaction = session.beginTransaction();
            q = session.createQuery("update RepresProduto p set p.precoFinal = p.preco2 + (p.preco2 / 100 * p.ipi) + p.frete where p.repres.idRepres = :repres and p.produto.idProduto = :produto");
            for (Integer row : lista) {
                q.setParameter("repres", value.getRepres().getIdRepres());
                q.setParameter("produto", row);
                q.executeUpdate();
            }
            newTransaction.commit();
            result = true;
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            result = false;
            try {
                if (newTransaction != null) {
                    newTransaction.rollback();
                }
            } catch (Exception ex) {
                getLogger().error(ex.getMessage(), ex);
            }
        }
        return result;
    }

    public boolean alterarComissao(AlteraPreco value, List<Integer> lista) {
        org.hibernate.Query q;
        Session session = ManagerFactory.getSession();
        boolean result = false;
        if (value.getTipo() == 0) {
            q = session.createQuery("update RepresProduto p set p.percComissao = :valor where p.repres.idRepres = :repres and p.produto.idProduto = :produto");
        } else {
            q = session.createQuery("update RepresProduto p set p.percComissao = p.percComissao + (p.percComissao * :valor / 100) where p.repres.idRepres = :repres and p.produto.idProduto = :produto");
        }

        Transaction newTransaction = null;
        try {
            newTransaction = session.beginTransaction();
            for (Integer row : lista) {
                q.setParameter("valor", value.getValor());
                q.setParameter("repres", value.getRepres().getIdRepres());
                q.setParameter("produto", row);
                q.executeUpdate();
            }
            newTransaction.commit();
            result = true;
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            result = false;
            try {
                if (newTransaction != null) {
                    newTransaction.rollback();
                }
            } catch (Exception ex) {
                getLogger().error(ex.getMessage(), ex);
            }
        }
        return result;
    }

    @Override
    public List findAll() {
        getLogger().info("findAll");
        ProdutoFilter filtro = new ProdutoFilter();
        filtro.setOrder(1);
        return findByExample(filtro);
    }

    public List findProdutoRepres(ProdutoFilter filtro) {
        getLogger().info("findProdutoRepres");
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(RepresProduto.class);
        Criteria represCriteria = criteria.createCriteria("repres");
        Criteria produtoCriteria = criteria.createCriteria("produto");
        if (filtro.getOrder() == ProdutoFilter.ORDER_CODIGO) {
            produtoCriteria.addOrder(Order.asc("idProduto"));
        } else {
            produtoCriteria.addOrder(Order.asc("descricao"));
        }
        if ((filtro.getProduto() != null) && (filtro.getProduto().getIdProduto() != null)) {
            produtoCriteria.add(Restrictions.eq("idProduto", filtro.getProduto().getIdProduto()));
        } else if ((filtro.getProduto().getDescricao() != null) && (filtro.getProduto().getDescricao().length() > 0)) {
            produtoCriteria.add(Restrictions.like("descricao", "%" + filtro.getProduto().getDescricao() + "%"));
        }
        if (filtro.getProduto().getUnidade().getIdUnidade() != null) {
            produtoCriteria.createCriteria("unidade").add(Restrictions.eq("idUnidade", filtro.getProduto().getUnidade().getIdUnidade()));
        }
        if ((filtro.getProduto().getSubGrupoProduto() != null) && (filtro.getProduto().getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            produtoCriteria.createCriteria("subGrupoProduto").add(Restrictions.eq("idCodSubGrupo", filtro.getProduto().getSubGrupoProduto().getIdCodSubGrupo()));
        } else if (filtro.getProduto().getGrupoProduto().getIdCodGrupo() != null) {
            produtoCriteria.createCriteria("grupoProduto").add(Restrictions.eq("idCodGrupo", filtro.getProduto().getGrupoProduto().getIdCodGrupo()));
        }
        if (filtro.isInativo()) {
            //criteria.add(Restrictions.eq("ativo", Short.valueOf("0")));
        } else {
            criteria.add(Restrictions.eq("ativo", Short.valueOf("1")));
        }
        if (filtro.getRepres().getIdRepres() != null) {
            represCriteria.add(Restrictions.eq("idRepres", filtro.getRepres().getIdRepres()));
        }
        return criteria.list();
    }

    @Override
    public List findByExample(Object obj) {
        getLogger().info("findByExample");
        ProdutoFilter filtro = (ProdutoFilter) obj;

        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Produto.class);
        if ((filtro.getProduto() != null) && (filtro.getProduto().getIdProduto() != null)) {
            criteria.add(Restrictions.eq("idProduto", filtro.getProduto().getIdProduto()));
        } else if ((filtro.getProduto().getDescricao() != null) && (filtro.getProduto().getDescricao().length() > 0)) {
            criteria.add(Restrictions.like("descricao", "%" + filtro.getProduto().getDescricao() + "%"));
        }
        if (filtro.getProduto().isBloqueado()) {
            criteria.add(Restrictions.eq("bloqueado", "S")); 
        }
        if (filtro.getProduto().getUnidade().getIdUnidade() != null) {
            criteria.createCriteria("unidade").add(Restrictions.eq("idUnidade", filtro.getProduto().getUnidade().getIdUnidade()));
        }
        if ((filtro.getProduto().getSubGrupoProduto() != null) && (filtro.getProduto().getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            criteria.createCriteria("subGrupoProduto").add(Restrictions.eq("idCodSubGrupo", filtro.getProduto().getSubGrupoProduto().getIdCodSubGrupo()));
        } else if (filtro.getProduto().getGrupoProduto().getIdCodGrupo() != null) {
            criteria.createCriteria("grupoProduto").add(Restrictions.eq("idCodGrupo", filtro.getProduto().getGrupoProduto().getIdCodGrupo()));
        }
        //Criteria produtos = criteria.createCriteria("produtos");
        //Criteria repres = produtos.createCriteria("repres");
        //repres.add(Restrictions.eq("inativo", '0'));

        //produtos.add(Restrictions.eq("ativo", Short.decode("1")));
        
        criteria.addOrder(Order.desc("bloqueado"));

        if (filtro.getOrder() == ProdutoFilter.ORDER_CODIGO) {
            criteria.addOrder(Order.asc("idProduto"));
        } else {
            criteria.addOrder(Order.asc("descricao"));
        }
        return criteria.list();
    }

    @Override
    public void deleteRow(Object object) throws DAOException {

        if (object instanceof Produto) {
            deleteProduto((Produto) object);
        } else {
            deleteRepresProduto((RepresProduto) object);
        }
    }

    private void deleteProduto(Produto p) throws DAOException {
        deleteRow(Produto.class, p.getIdProduto());
    }

    private void deleteRepresProduto(RepresProduto p) throws DAOException {
        deleteRow(RepresProduto.class, p.getRepresProdutoPK());
        deleteRow(Produto.class, p.getProduto().getIdProduto());
    }

    public void updateProdutoRepres(RepresProduto produtoRepres) throws DAOException {
        getLogger().info("updateProdutoRepres ini");
//        Object obj = findById(Produto.class, produto.getProduto().getIdProduto());
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q;
            q = em.createQuery("update RepresProduto p set p.ativo = :ativo, p.frete = :frete, p.conversaoFrete = :conversaoFrete, p.embalagem = :embalagem, p.ipi = :ipi, p.preco = :preco, p.preco2 = :preco2, p.precoFinal = :precoFinal, p.qtdUnd = :qtdUnd, p.tabela = :tabela, p.percComissao = :comissao where p.repres.idRepres = :idRepres and p.produto.idProduto = :idProduto");
            q.setParameter("ativo", produtoRepres.getAtivo());
            q.setParameter("embalagem", produtoRepres.getEmbalagem());
            q.setParameter("ipi", produtoRepres.getIpi());
            q.setParameter("conversaoFrete", produtoRepres.getConversaoFrete());
            q.setParameter("preco", produtoRepres.getPreco());
            q.setParameter("preco2", produtoRepres.getPreco2());
            q.setParameter("frete", produtoRepres.getFrete());
            q.setParameter("precoFinal", produtoRepres.getPrecoFinal());
            q.setParameter("qtdUnd", produtoRepres.getQtdUnd());
            q.setParameter("tabela", produtoRepres.getTabela());
            q.setParameter("comissao", produtoRepres.getPercComissao());
            q.setParameter("idRepres", produtoRepres.getRepres().getIdRepres());
            q.setParameter("idProduto", produtoRepres.getProduto().getIdProduto());
            q.executeUpdate();
            q = em.createQuery("update Produto p set p.descricao = :descricao, p.fatorConversao = :fatorConversao, p.grupoProduto.idCodGrupo = :idCodGrupo, p.subGrupoProduto.idCodSubGrupo = :idCodSubGrupo, p.undCumulativa.idUnidade = :idUndCumulativa, p.unidade.idUnidade = :unidade, p.peso = :peso where p.idProduto = :idProduto");
            q.setParameter("descricao", produtoRepres.getProduto().getDescricao());
            q.setParameter("peso", produtoRepres.getProduto().getPeso());
            q.setParameter("fatorConversao", produtoRepres.getProduto().getFatorConversao());
            q.setParameter("idCodGrupo", produtoRepres.getProduto().getGrupoProduto().getIdCodGrupo());
            if (produtoRepres.getProduto().getSubGrupoProduto() != null) {
                q.setParameter("idCodSubGrupo", produtoRepres.getProduto().getSubGrupoProduto().getIdCodSubGrupo());
            } else {
                q.setParameter("idCodSubGrupo", null);
            }
            q.setParameter("idUndCumulativa", produtoRepres.getProduto().getUndCumulativa().getIdUnidade());
            q.setParameter("unidade", produtoRepres.getProduto().getUnidade().getIdUnidade());
            q.setParameter("idProduto", produtoRepres.getProduto().getIdProduto());
            q.executeUpdate();
            q = em.createQuery("update Repres set dataTabela = :tabela where idRepres = :repres");
            q.setParameter("tabela", DateUtils.getDate());
            q.setParameter("repres", produtoRepres.getRepres().getIdRepres());
            q.executeUpdate();

            /*           if (obj == null) {
            getLogger().info("setProduto");
            produtoRepres.setProduto(em.merge(produtoRepres.getProduto()));
            getLogger().info("merge produtorepres");
            produtoRepres = em.merge(produtoRepres);
            //            }
            //            produto = em.merge(produto); */
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
        getLogger().info("updateProdutoRepres - end");
    }
}
