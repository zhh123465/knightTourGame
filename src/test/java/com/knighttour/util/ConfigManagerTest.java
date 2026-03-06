package com.knighttour.util;

import com.knighttour.exception.ConfigException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class ConfigManagerTest {
    
    private static final String TEST_CONFIG_PATH = "config/app.properties";
    private ConfigManager configManager;
    
    @BeforeEach
    void setUp() {
        configManager = new ConfigManager();
        // Clean up before test
        File file = new File(TEST_CONFIG_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
    
    @AfterEach
    void tearDown() {
        // Clean up after test
        File file = new File(TEST_CONFIG_PATH);
        if (file.exists()) {
            file.delete();
        }
        // Also delete dir if empty
        File dir = new File("config");
        if (dir.exists() && dir.isDirectory() && dir.list().length == 0) {
            dir.delete();
        }
    }

    @Test
    void testLoadDefaults() {
        AppConfig config = configManager.loadConfig();
        assertNotNull(config);
        assertEquals(100, config.getAnimationDelayMs());
        assertFalse(config.isEnableSound());
        assertEquals("INFO", config.getLogLevel());
    }

    @Test
    void testSaveAndLoad() throws ConfigException {
        AppConfig config = new AppConfig();
        config.setAnimationDelayMs(500);
        config.setEnableSound(true);
        config.setLogLevel("DEBUG");
        
        configManager.saveConfig(config);
        
        AppConfig loadedConfig = configManager.loadConfig();
        assertEquals(500, loadedConfig.getAnimationDelayMs());
        assertTrue(loadedConfig.isEnableSound());
        assertEquals("DEBUG", loadedConfig.getLogLevel());
    }
}
