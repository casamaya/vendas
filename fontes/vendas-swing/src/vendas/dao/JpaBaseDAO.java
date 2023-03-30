/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sam
 */
public class JpaBaseDAO<T> {

  public void insert(T t) {
    //getJpaTemplate().persist(t);
  }

  public void delete(T t) {
    //getJpaTemplate().remove(t);
  }

  public void update(T t) {
//    getJpaTemplate().merge(t);
  }

   public List<T> findAll(Class<T> objectClass) {
    List<T> entities = new ArrayList<T>();
    /*
    try {
      String s = "select c from " + objectClass.getSimpleName() + " c";
      entities = getJpaTemplate().find(s);
    } catch (Exception e) {
      e.printStackTrace();
    } */
    return entities;
  }

}
