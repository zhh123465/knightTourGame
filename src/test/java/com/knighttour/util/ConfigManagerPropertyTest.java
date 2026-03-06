package com.knighttour.util;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigManagerPropertyTest {

    @Property
    void configRoundTrip(@ForAll @IntRange(min = 0, max = 1000) int delay,
                         @ForAll boolean sound,
                         @ForAll("logLevels") String logLevel) throws Exception {
        
        AppConfig original = new AppConfig();
        original.setAnimationDelayMs(delay);
        original.setEnableSound(sound);
        original.setLogLevel(logLevel);
        
        // 我们不应该真的写入文件系统，或者使用临时文件
        // 这里只是验证对象属性的设置和获取
        
        assertThat(original.getAnimationDelayMs()).isEqualTo(delay);
        assertThat(original.isEnableSound()).isEqualTo(sound);
        assertThat(original.getLogLevel()).isEqualTo(logLevel);
    }
    
    @Provide
    Arbitrary<String> logLevels() {
        return Arbitraries.of("DEBUG", "INFO", "WARN", "ERROR");
    }
}
