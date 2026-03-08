package com.knighttour.util;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源管理器
 * 
 * 负责统一加载和缓存应用程序资源（图片、音频等）。
 */
public class ResourceManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);
    private static final ResourceManager INSTANCE = new ResourceManager();
    
    private final Map<String, Image> imageCache = new HashMap<>();
    
    // 图片路径常量
    public static final String IMAGE_KNIGHT = "/images/knight_sketch.png";
    
    private ResourceManager() {
        // 私有构造函数，单例模式
    }
    
    public static ResourceManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * 加载图片资源
     * 
     * @param path 图片资源路径 (相对于 classpath)
     * @return 加载的 Image 对象，如果加载失败返回 null
     */
    public Image getImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                logger.error("Resource not found: {}", path);
                return null;
            }
            
            Image image = new Image(is);
            if (image.isError()) {
                logger.error("Failed to load image: {}", path);
                return null;
            }
            
            imageCache.put(path, image);
            return image;
        } catch (Exception e) {
            logger.error("Error loading image: " + path, e);
            return null;
        }
    }
    
    /**
     * 获取骑士图标
     * @return 骑士图片
     */
    public Image getKnightImage() {
        return getImage(IMAGE_KNIGHT);
    }
}
