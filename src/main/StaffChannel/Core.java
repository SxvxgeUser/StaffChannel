package StaffChannel;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        new CommandStaff(this);
    }

    @Override
    public void onDisable() {
    }

    public void sendAdminChannelMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || all.hasPermission("StaffChannel.Admin")) {
                    all.sendMessage("§6SC§8:§4ADMIN§8:§c" + sender.getName() + "§8> §f" + message); //Can only be an Admin.
                }
            }
        }
        else {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || all.hasPermission("StaffChannel.Admin")) {
                    all.sendMessage("§6SC§8:§4ADMIN§8:§7CONSOLE§8> §f" + message);
                }
            }
        }

        System.out.println("SC:ADMIN:" + sender.getName() + "> " + message); //Show message in CONSOLE
    }

    public void sendStaffChannelMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || all.hasPermission("StaffChannel.Staff")) {
                    if (sender.isOp() || sender.hasPermission("StaffChannel.Admin")) {
                        all.sendMessage("§6SC§8:§eSTAFF§8:§c" + sender.getName() + "§8> §f" + message); //Admin executed the command.
                    }
                    else {
                        all.sendMessage("§6SC§8:§eSTAFF§8:§5" + sender.getName() + "§8> §f" + message); //Sr. Mod or Mod executed the command.
                    }
                }
            }
        }
        else {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.isOp() || all.hasPermission("StaffChannel.Staff")) {
                    all.sendMessage("§6SC§8:§eSTAFF§8:§7CONSOLE§8> §f" + message);
                }
            }
        }

        System.out.println("SC:STAFF:" + sender.getName() + "> " + message); //Show message in CONSOLE
    }
}
