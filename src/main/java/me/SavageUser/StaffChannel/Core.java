package me.SavageUser.StaffChannel;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Core extends JavaPlugin implements Listener {
    public StaffConfig config;
    public Essentials essentials;

    public void onEnable() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Essentials") == null) {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            Bukkit.getServer().getLogger().severe("StaffChannel requires Essentials to run!");
        }
        else {
            new CommandStaff(this);
            this.config = new StaffConfig(new File(this.getDataFolder(), "staffchannel-data.yml"));
            essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            this.getServer().getPluginManager().registerEvents(new EssentialsHook(this), this);
            this.getServer().getPluginManager().registerEvents(this, this);
        }
    }

    public void onDisable() {}

    public void updatePlayerStaffChatSetting(Player player, boolean option) {
        if (this.config.getConfigOption("Player-Settings." + player.getName()) != null) {
            try {
                this.config.setProperty("Player-Settings." + player.getName() + ".See-Staff-Chat", option);
                this.config.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            createPlayerSettings(player);
        }
    }

    public void updateNicknameSetting(Player player, boolean option) {
        if (this.config.getConfigOption("Player-Settings." + player.getName()) != null) {
            try {
                this.config.setProperty("Player-Settings." + player.getName() + ".Show-Nicknames", option);
                this.config.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            createPlayerSettings(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp() || hasAdminPerm(player)|| hasStaffPerm(player)) {
            if (this.config.getConfigOption("Player-Settings." + player.getName()) == null) {
                createPlayerSettings(player);
            }

            if (!hasStaffChatEnabled(player))  {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("TAG") + " " + "§7You currently have the staff chat §4disabled§7!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("TAG") + " " + "§7Type §e/staffchat togglechat §7to §are-enable §7it!"));
            }
        }

    }

    public void createPlayerSettings(Player player) {
        if (this.config.getConfigOption("Player-Settings." + player.getName()) == null) {
            try {
                this.config.setProperty("Player-Settings." + player.getName() + ".See-Staff-Chat", true);
                this.config.setProperty("Player-Settings." + player.getName() + ".Show-Nicknames", false);
                this.config.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean hasStaffChatEnabled(Player player) {
        return this.config.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true);
    }

    public boolean hasNicknamesEnabled(Player player) {
        return this.config.getConfigOption("Player-Settings." + player.getName() + ".Show-Nicknames").equals(true);
    }

    public boolean hasAdminPerm(CommandSender sender) {
        return sender.hasPermission(String.valueOf(config.getConfigOption("Channels.ADMIN.permission")));
    }

    public boolean hasStaffPerm(CommandSender sender) {
        return sender.hasPermission(String.valueOf(config.getConfigOption("Channels.STAFF.permission")));
    }

    public void sendAdminChannelMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || hasAdminPerm(all)) {
                    if (hasStaffChatEnabled(all)) {
                        if (hasNicknamesEnabled(all)) {
                            all.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6SC§8:§4ADMIN§8:§c" + essentials.getUser(sender.getName()).getNickname() + "§8> §f" + message));
                        } else {
                            all.sendMessage("§6SC§8:§4ADMIN§8:§c" + sender.getName() + "§8> §f" + message);
                        }
                    }
                }
            }
        } else {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || hasAdminPerm(all)) {
                    if (hasStaffChatEnabled(all)) {
                        all.sendMessage("§6SC§8:§4ADMIN§8:§7CONSOLE§8> §f" + message);
                    }
                }
            }
        }
        System.out.println("SC:ADMIN:" + sender.getName() + "> " + message);
    }

    public void sendStaffChannelMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || hasStaffPerm(all)) {
                    if (sender.isOp() || hasAdminPerm(sender)) {
                        if (hasStaffChatEnabled(all)) {
                            if (hasNicknamesEnabled(all)) {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6SC§8:§eSTAFF§8:§c" + essentials.getUser(sender.getName()).getNickname() + "§8> §f" + message));
                            } else {
                                all.sendMessage("§6SC§8:§eSTAFF§8:§c" + sender.getName() + "§8> §f" + message);
                                all.sendMessage("Essentials Nickname: " + essentials.getUser(sender.getName()).getNickname());
                            }
                        }
                    } else {
                        if (hasStaffChatEnabled(all)) {
                            if (hasNicknamesEnabled(all)) {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6SC§8:§eSTAFF§8:§5" + essentials.getUser(sender.getName()).getNickname() + "§8> §f" + message));
                            } else {
                                all.sendMessage("§6SC§8:§eSTAFF§8:§5" + sender.getName() + "§8> §f" + message);
                            }
                        }
                    }
                }
            }
        } else {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || hasStaffPerm(all)) {
                    if (hasStaffChatEnabled(all)) {
                        all.sendMessage("§6SC§8:§eSTAFF§8:§7CONSOLE§8> §f" + message);
                    }
                }
            }
        }
        System.out.println("SC:STAFF:" + sender.getName() + "> " + message);
    }
}
