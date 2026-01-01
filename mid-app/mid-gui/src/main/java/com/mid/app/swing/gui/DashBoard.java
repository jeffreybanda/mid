package com.mid.app.swing.gui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.http.auth.AuthenticationException;

import com.mid.app.config.ConfigurationManager;
import com.mid.app.http.utils.ConnectionUtils;

public class DashBoard {
    
    public static void main(final String[] args) {
        
        // First, check command line arguments (they override config file)
        ConfigurationManager.Environment env = getEnvironmentFromArgs(args);
        
        if (env != null) {
            // Command line argument overrides config file
            ConfigurationManager.initialize(env);
            System.out.println("Using environment from command line: " + env);
        } else {
            // Use configuration from file
            ConfigurationManager.initialize();
            System.out.println("Using environment from config file: " + ConfigurationManager.getCurrentEnvironment());
        }
        
        try {
            ConnectionUtils.checkConnectionToPortal();
        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
            // You might want to show a dialog to the user here
            System.exit(1);
        }
        
        try {
            System.out.println("PropFile: " + System.getProperty("PropFile"));
            UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
            
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
            // Continue with default look and feel
        }
        
        // Pass environment information to MainFrame if needed
        new MainFrame();
    }
    
    private static ConfigurationManager.Environment getEnvironmentFromArgs(String[] args) {
        if (args.length > 0) {
            String arg = args[0].toLowerCase();
            if ("test".equals(arg) || "--test".equals(arg)) {
                return ConfigurationManager.Environment.TEST;
            }
            if ("live".equals(arg) || "--live".equals(arg)) {
                return ConfigurationManager.Environment.LIVE;
            }
        }
        return null; // No valid environment argument
    }
}