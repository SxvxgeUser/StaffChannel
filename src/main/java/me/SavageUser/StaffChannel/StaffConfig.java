package me.SavageUser.StaffChannel;

import org.bukkit.util.config.Configuration;

import java.io.File;

public class StaffConfig extends Configuration {
    public StaffConfig(File file) {
        super(file);
        this.reload();
    }

    public void reload() {
        this.load();
        this.write();
        this.save();
    }

    public void write() {
        generateConfigOption("Channels.ADMIN.permission", "StaffChannel.Admin");
        generateConfigOption("Channels.STAFF.permission", "StaffChannel.Staff");
    }

    private void generateConfigOption(String key, Object defaultValue) {
        if (this.getProperty(key) == null) {
            this.setProperty(key, defaultValue);
        }

        Object value = this.getProperty(key);
        this.removeProperty(key);
        this.setProperty(key, value);
    }

    public Object getConfigOption(String key) {
        return this.getProperty(key);
    }

    public Object getConfigOption(String key, Object defaultValue) {
        Object value = this.getConfigOption(key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public void updateNicknameSetting(String playername, boolean option) {
        if (this.getConfigOption("Player-Settings." + playername) != null) {
            try {
                this.setProperty("Player-Settings." + playername + ".Show-Nicknames", option);
                this.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            createPlayerSettings(playername);
        }
    }

    public void updatePlayerStaffChatSetting(String playername, boolean option) {
        if (this.getConfigOption("Player-Settings." + playername) != null) {
            try {
                this.setProperty("Player-Settings." + playername + ".See-Staff-Chat", option);
                this.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            createPlayerSettings(playername);
        }
    }

    public void createPlayerSettings(String playername) {
        if (this.getConfigOption("Player-Settings." + playername) == null) {
            try {
                this.setProperty("Player-Settings." + playername + ".See-Staff-Chat", true);
                this.setProperty("Player-Settings." + playername + ".Show-Nicknames", false);
                this.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
