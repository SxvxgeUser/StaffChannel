package me.SavageUser.StaffChannel;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStaff implements CommandExecutor {
    private final Core plugin;

    public CommandStaff(Core plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("staffchat").setExecutor(this);
    }

    String noPermission = "§cI'm sorry, Dave. I'm afraid I can't do that.";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean senderIsPlayer = sender instanceof Player;
        Player player = senderIsPlayer ? (Player) sender : null;

        if (cmd.getName().equalsIgnoreCase("staffchat") || cmd.getName().equalsIgnoreCase("sc")) {
            boolean canUseAny = sender.isOp() || (senderIsPlayer && (plugin.hasAdminPerm(player) || plugin.hasStaffPerm(player)));
            if (!canUseAny) {
                sender.sendMessage(noPermission);
                return true;
            }

            // Help Menu
            if (args.length == 0) {
                sender.sendMessage("§6StaffChannel §eCommands:");
                sender.sendMessage("§b/adminchat §3<message> §8- §7Send a message to the admin channel.");
                sender.sendMessage("§b/staffchat §3<message> §8- §7Send a message to the staff channel .");
                sender.sendMessage("§b/staffchat §3shownicknames §8- §7Toggle nickname visibility.");
                sender.sendMessage("§b/staffchat §3togglechat §8- §7Toggle admin/staff chat visibility.");
                sender.sendMessage("§b/staffchat §3reload §8- §7Reload the config file.");
                sender.sendMessage("§7All Channels: §4ADMIN§8, §5STAFF");
                return true;
            }
            // Show Nicknames / Show Nicks
            if (args[0].equalsIgnoreCase("shownicknames") || args[0].equalsIgnoreCase("shownicks") || args[0].equalsIgnoreCase("togglenicknames") || args[0].equalsIgnoreCase("togglenicks")) {
                if (!senderIsPlayer) {
                    sender.sendMessage("Only players can execute this command!");
                    return true;
                }
                if (args.length != 1) {
                    player.sendMessage("§cUsage: /staffchat shownicknames");
                    return true;
                }

                boolean currently = plugin.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".Show-Nicknames").equals(true);
                plugin.StaffConfig.updateNicknameSetting(player.getName(), !currently);

                if (currently) {
                    player.sendMessage("§6SC§8: §7You will §4no longer §7see staff nicknames!");
                } else {
                    player.sendMessage("§6SC§8: §7You will §anow §7see staff nicknames!");
                }
                return true;
            }

            // Toggle Chat
            else if (args[0].equalsIgnoreCase("togglechat")) {
                if (!senderIsPlayer) {
                    sender.sendMessage("Only players can execute this command!");
                    return true;
                }
                if (args.length != 1) {
                    player.sendMessage("§cUsage: /staffchat togglechat");
                    return true;
                }

                boolean currently = plugin.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true);
                plugin.StaffConfig.updatePlayerStaffChatSetting(player.getName(), !currently);

                if (currently) {
                    player.sendMessage("§6SC§8: §7You will §4no longer §7see messages from §6Admin & Staff Chat§7!");
                } else {
                    player.sendMessage("§6SC§8: §7You will §anow §7see messages from §6Admin & Staff Chat§7!");
                }
                return true;
            }

            // Reload / ReloadCFG
            else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("reloadcfg")) {
                boolean canReload = sender.isOp() || (senderIsPlayer && plugin.hasAdminPerm(player));
                if (!canReload) {
                    sender.sendMessage(noPermission);
                    return true;
                }

                if (args.length != 1) {
                    sender.sendMessage("§cUsage: /staffchat reload or /staffchat reloadcfg");
                    return true;
                }

                plugin.StaffConfig.reload();
                sender.sendMessage("§6SC§8: §estaffchannel-data.yml §7reloaded!");
                return true;
            }
            else { //Send a message to the staff channel.
                boolean canUseStaff = sender.isOp() || (senderIsPlayer && plugin.hasStaffPerm(player));
                if (!canUseStaff) { //Unlikely.
                    sender.sendMessage("§cYou do not have access to the STAFF channel!");
                    return true;
                }

                String msg = "";
                for (int i = 0; i < args.length; i++) {
                    msg = msg + args[i] + " ";
                }
                msg = ChatColor.translateAlternateColorCodes('&', msg);

                if (senderIsPlayer) {
                    if (plugin.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true)) {
                        plugin.sendStaffChannelMessage(player, msg);
                    } else {
                        player.sendMessage("§6SC§8: §7You currently have the admin/staff chat §4disabled§7!");
                        player.sendMessage("§6SC§8: §7Type §e/staffchat togglechat §7to §are-enable §7it!");
                    }
                } else {
                    plugin.sendStaffChannelMessage(sender, msg);
                }
                return true;
            }
        }

        return true;
    }
}
