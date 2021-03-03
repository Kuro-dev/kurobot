package org.kurodev.config;

import org.kurodev.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author kuro
 **/
public class MySettings extends Properties {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getSetting(Setting setting) {
        String response = this.getProperty(setting.getKey());
        if (response == null) {
            this.put(setting.getKey(), setting.getDefaultVal());
            logger.info("Adding missing setting to file: {} = {}", setting.getKey(), setting.getDefaultVal());
            Main.saveSettings();
            return setting.getDefaultVal();
        }
        return response;
    }

    public void restoreDefault() {
        for (Setting value : Setting.values()) {
            this.setProperty(value.getKey(), value.getDefaultVal());
        }
    }

    public boolean getSettingBool(Setting setting) {
        return Boolean.parseBoolean(getSetting(setting));
    }
}
