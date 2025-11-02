package me.SavageUser.StaffChannel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Core extends JavaPlugin {
    public static StaffConfig StaffConfig;
    private static List<String> dependencyNames = new ArrayList<String>();
    private String pluginname;
    public Logger logger = Bukkit.getLogger();
    private EssentialsPlugin essentials;
    @Override
    public void onEnable() {
        pluginname =  this.getDescription().getName();


        addDependency("Essentials");

        if(!loadDependencies()){
            System.out.print(pluginname + ": disabling plugin");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        essentials = new EssentialsPlugin(this);

        new CommandAdmin(this);
        new CommandStaff(this);

        this.StaffConfig = new StaffConfig(new File(this.getDataFolder(), "staffchannel-data.yml"));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PluginHookManager(this), this);
        pm.registerEvents(new CoreEvents(this), this);

    }

    @Override
    public void onDisable() {
        // Any necessary cleanup here
    }

    public final List<String> getDependenciesList() {
        return dependencyNames;
    }

    public boolean loadDependencies(){


        if(!getMissingDependencies().isEmpty()){

            for (String missingDependencyName : getMissingDependencies()) {

                logger.severe(pluginname + ": dependency " + missingDependencyName + " is missing");
            }
            return false;
        }

        for (String dependencyName : dependencyNames){

            if(!Bukkit.getPluginManager().getPlugin(dependencyName).isEnabled()){
                logger.info(pluginname + ": dependency " + dependencyName + " isn't enabled");
                return false;
            }

        }

        return true;
    }

    public void addDependency(String dependencyName) {
        dependencyNames.add(dependencyName);
    }

    public List<String> getMissingDependencies() {

        List<String> missingDependencies = new ArrayList<>();

        for(String dependencyName : dependencyNames){

            boolean isMissing = Bukkit.getPluginManager().getPlugin(dependencyName) == null;

            if(isMissing == true) {
                missingDependencies.add(dependencyName);
            }

        }
        return missingDependencies;
    }


    public boolean hasStaffChatEnabled(Player player) {
        return this.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true);
    }

    public boolean hasNicknamesEnabled(Player player) {
        return this.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".Show-Nicknames").equals(true);
    }

    public boolean hasAdminPerm(CommandSender player) {
        return player.hasPermission(String.valueOf(StaffConfig.getConfigOption("Channels.ADMIN.permission")));
    }

    public boolean hasStaffPerm(CommandSender player) {
        return player.hasPermission(String.valueOf(StaffConfig.getConfigOption("Channels.STAFF.permission")));
    }

    public void sendAdminChannelMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || hasAdminPerm(all)) {
                    if (hasStaffChatEnabled(all)) {
                        if (hasNicknamesEnabled(all)) {
                            if (essentials.getNick(sender.getName()) == null) {
                                all.sendMessage("§6SC§8:§4ADMIN§8:§c" + sender.getName() + "§8> §f" + message);
                            } else {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6SC§8:§4ADMIN§8:§c" + essentials.getNick(sender.getName()) + "§8> §f" + message));
                            }
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
                                if (essentials.getNick(sender.getName()) == null) {
                                    all.sendMessage("§6SC§8:§eSTAFF§8:§c" + sender.getName() + "§8> §f" + message);
                                } else {
                                    all.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6SC§8:§eSTAFF§8:§c" + essentials.getNick(sender.getName()) + "§8> §f" + message));
                                }
                            } else {
                                all.sendMessage("§6SC§8:§eSTAFF§8:§c" + sender.getName() + "§8> §f" + message);
                            }
                        }
                    } else {
                        if (hasStaffChatEnabled(all)) {
                            if (hasNicknamesEnabled(all)) {
                                if (essentials.getNick(sender.getName()) == null) {
                                    all.sendMessage("§6SC§8:§eSTAFF§8:§5" + sender.getName() + "§8> §f" + message);
                                } else {
                                    all.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6SC§8:§eSTAFF§8:§5" + essentials.getNick(sender.getName()) + "§8> §f" + message));
                                }
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
