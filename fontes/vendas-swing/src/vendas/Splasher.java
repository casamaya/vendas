/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas;

import ritual.swing.SplashWindow;

/**
 *
 * @author Sam
 * Copyright (c) 2003-2005 Werner Randelshofer
 * http://www.randelshofer.ch
 */
public class Splasher {
    public static void main(String[] args) {
        SplashWindow.splash(Splasher.class.getResource("/vendas/resources/splash.jpg"));
        SplashWindow.invokeMain("vendas.Main", args);
        SplashWindow.disposeSplash();
    }
}
