package com.mid.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {
    
    private static final String CONFIG_FILE = "/config.properties";
    private static Environment currentEnvironment = Environment.LIVE;
    private static Properties properties = new Properties();
    
    public enum Environment {
        LIVE("live"),
        TEST("test");
        
        private final String value;
        
        Environment(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static Environment fromString(String value) {
            if (value == null) {
                return LIVE;
            }
            for (Environment env : Environment.values()) {
                if (env.value.equalsIgnoreCase(value)) {
                    return env;
                }
            }
            return LIVE;
        }
    }
    
    static {
        loadConfiguration();
    }
    
    private static void loadConfiguration() {
        try (InputStream input = ConfigurationManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                String envValue = properties.getProperty("environment", "live");
                currentEnvironment = Environment.fromString(envValue);
            } else {
                System.err.println("Configuration file not found: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
        }
    }
    
    public static void initialize() {
        // Apply the configuration from the file
        applyConfiguration(currentEnvironment);
    }
    
    public static void initialize(Environment env) {
        currentEnvironment = env;
        applyConfiguration(env);
    }
    
    private static void applyConfiguration(Environment env) {
        if (env == Environment.TEST) {
            System.setProperty("PropFile", "/milleniumTestConfig.properties");
           
        } else {
            System.setProperty("PropFile", "/milleniumLiveConfig.properties");
          
        }
        
        System.setProperty("BenefitsFile", "/milleniumBenefitsStructure.properties");
        System.setProperty("environment", env.getValue());
    }
    
    public static Environment getCurrentEnvironment() {
        return currentEnvironment;
    }
    
    public static boolean isTestEnvironment() {
        return currentEnvironment == Environment.TEST;
    }
    
    public static boolean isLiveEnvironment() {
        return currentEnvironment == Environment.LIVE;
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // Optional: Add method to reload configuration at runtime
    public static void reloadConfiguration() {
        loadConfiguration();
        applyConfiguration(currentEnvironment);
    }
}