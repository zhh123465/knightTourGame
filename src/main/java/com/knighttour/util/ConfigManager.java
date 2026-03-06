package com.knighttour.util;

import com.knighttour.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * 配置管理器
 * 
 * 负责加载和保存应用配置。
 */
public class ConfigManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final String DEFAULT_CONFIG_PATH = "config/app.properties";
    
    /**
     * 加载配置
     * 
     * @return 加载的配置对象，如果文件不存在则返回默认配置
     */
    public AppConfig loadConfig() {
        File configFile = new File(DEFAULT_CONFIG_PATH);
        if (!configFile.exists()) {
            logger.info("Config file not found, using defaults");
            return AppConfig.getDefault();
        }
        
        try (InputStream input = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(input);
            return parseConfig(props);
        } catch (IOException e) {
            logger.error("Error reading config file", e);
            return AppConfig.getDefault();
        } catch (Exception e) {
            logger.error("Error parsing config file", e);
            return AppConfig.getDefault();
        }
    }
    
    /**
     * 保存配置
     * 
     * @param config 要保存的配置对象
     * @throws ConfigException 如果保存失败
     */
    public void saveConfig(AppConfig config) throws ConfigException {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        
        File configFile = new File(DEFAULT_CONFIG_PATH);
        File configDir = configFile.getParentFile();
        if (configDir != null && !configDir.exists()) {
            if (!configDir.mkdirs()) {
                throw new ConfigException("无法创建配置目录: " + configDir.getAbsolutePath());
            }
        }
        
        Properties props = toProperties(config);
        
        try (OutputStream output = new FileOutputStream(configFile)) {
            props.store(output, "Knight Tour Game Configuration");
            logger.info("Configuration saved to {}", configFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error saving config file", e);
            throw new ConfigException("无法保存配置: " + e.getMessage(), e);
        }
    }
    
    private AppConfig parseConfig(Properties props) {
        AppConfig config = new AppConfig();
        
        String delayStr = props.getProperty("animation.delay", "100");
        try {
            config.setAnimationDelayMs(Integer.parseInt(delayStr));
        } catch (NumberFormatException e) {
            logger.warn("Invalid animation delay: {}", delayStr);
        }
        
        String soundStr = props.getProperty("sound.enable", "false");
        config.setEnableSound(Boolean.parseBoolean(soundStr));
        
        String logLevel = props.getProperty("log.level", "INFO");
        config.setLogLevel(logLevel);
        
        // ColorScheme parsing is complex, skipping for now or using default
        // In a real app, we would parse color codes
        
        return config;
    }
    
    private Properties toProperties(AppConfig config) {
        Properties props = new Properties();
        props.setProperty("animation.delay", String.valueOf(config.getAnimationDelayMs()));
        props.setProperty("sound.enable", String.valueOf(config.isEnableSound()));
        props.setProperty("log.level", config.getLogLevel());
        // Add colors
        return props;
    }
}
