/*
 * FilterBean.java
 * 
 * Created on 08/09/2007, 18:01:22
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

/**
 *
 * @author Sam
 */
public class FilterBean {
    
    private StringBuilder title;
    private String singleValue;
    private Integer numRows;

    public FilterBean() {
        title = new StringBuilder();
    }

    public String getTitle() {
        return title.toString();
    }

    public void setTitle(String value) {
        if (value == null)
            this.title = new StringBuilder();
        else
            this.title = new StringBuilder(value);
    }

    public String getSingleValue() {
        return singleValue;
    }

    public void setSingleValue(String singleValue) {
        this.singleValue = singleValue;
    }

    public Integer getNumRows() {
        return numRows;
    }

    public void setNumRows(Integer numRows) {
        this.numRows = numRows;
    }
    
    public void addTitle(String value) {
        String s = title.toString();
        if (s == null || s.length() == 0)
            title = new StringBuilder(value);
        else
            title.append(". ").append(value);
    }

}
