package org.kurodev.conf;

import org.kurodev.Main;

import java.util.Properties;

/**
 * @author kuro
 **/
public class MySettings extends Properties {
    public String getSetting(Setting setting) {
        String response = this.getProperty(setting.getKey());
        if (response == null) {
            this.put(setting.getKey(), setting.getDefaultVal());
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
}
