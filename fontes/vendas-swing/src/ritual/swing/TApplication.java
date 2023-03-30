/*
 * TApplication.java
 * 
 * Created on 07/07/2007, 16:59:55
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ritual.swing;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;
import vendas.dao.UserDao;
import vendas.entity.User;

/**
 *
 * @author Sam
 */
public class TApplication {

    static private TApplication _instance;
    private ConfigurableListableBeanFactory factory;
    private ResourceBundle bundle;
    private String logoPath;
    private String resourcePath;
    private String factoryPath;
    private JFrame mainFrame;
    private MDIDesktopPane desktopPane;
    private User user;
    private Map<String, Boolean> recursos;

    private TApplication() {
        recursos = new HashMap<>();
    }

    static public TApplication getInstance() {
        if (_instance == null) {
            _instance = new TApplication();
        }
        return _instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        UserDao ud = (UserDao) TApplication.getInstance().lookupService("userDao");
        setRecursos(ud.getPermissoes(user.getPerfis()));
    }
    
    public Boolean isGrant(String value) {
        Boolean grant = recursos.get(value);
        return getUser().isAdmin() || grant != null;
    }

    public Map<String, Boolean> getRecursos() {
        return recursos;
    }

    public void setRecursos(Map<String, Boolean> recursos) {
        this.recursos = recursos;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
        bundle = ResourceBundle.getBundle(resourcePath);
    }

    public String getFactoryPath() {
        return factoryPath;
    }

    public void setFactoryPath(String factoryPath) {
        this.factoryPath = factoryPath;
        factory = new XmlBeanFactory(new UrlResource(getClass().getResource(factoryPath)));
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public MDIDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public void setDesktopPane(MDIDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
    }

    /**
     * Lookup service based on service bean name.
     *
     * @param serviceBeanName the service bean name
     * @return the service bean
     */
    public Object lookupService(String serviceBeanName) {
        return factory.getBean(serviceBeanName);
    }

    public String getResourceString(String value) {
        return bundle.getString(value);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void closeAll() {
        factory.destroySingletons();
    }

    public Map getDefaultMap(String title) {
        return getDefaultMap(title, "");
    }

    public Map getDefaultMap(String title, String subTitle) {
        Map model = new HashMap();
        model.put("ReportTitle", title);
        model.put("SubTitle", subTitle);
        model.put("LogoURL", getClass().getResource(TApplication.getInstance().getLogoPath()));
        return model;
    }

    public JInternalFrame getFrame(String frameTitle) {
        boolean found = false;
        try {
            for (JInternalFrame frame : desktopPane.getAllFrames()) {
                if (frameTitle.equals(frame.getTitle())) {
                    frame.setSelected(true);
                    return frame;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return null;
    }

    public boolean locateFrame(String frameTitle) {
        boolean found = false;
        try {
            for (JInternalFrame frame : desktopPane.getAllFrames()) {
                if (frameTitle.equals(frame.getTitle())) {
                    frame.setSelected(true);
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            found = false;
        }
        return found;
    }
}

