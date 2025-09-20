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

        if (!(cmd.getName().equalsIgnoreCase("staffchat") || cmd.getName().equalsIgnoreCase("sc"))) {
            return true;
        }

        // Base permission to use /staffchat at all
        boolean canUseAny =
                sender.isOp() ||
                        (senderIsPlayer && (plugin.hasAdminPerm(player) || plugin.hasStaffPerm(player)));

        if (!canUseAny) {
            sender.sendMessage(noPermission);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§6StaffChannel §eCommands:");
            sender.sendMessage("§b/staffchat §3<channel> <message> §8- §7Send a message to staff.");
            sender.sendMessage("§b/staffchat §3shownicknames §8- §7Toggle nickname visibility.");
            sender.sendMessage("§b/staffchat §3togglechat §8- §7Toggle staff chat visibility.");
            sender.sendMessage("§b/staffchat §3reload §8- §7Reload the config file.");
            sender.sendMessage("§7All Channels: §4ADMIN§8, §5STAFF");
            return true;
        }

        // Build message (args from index 1 onward)
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(' ');
            sb.append(args[i]);
        }
        String msg = ChatColor.translateAlternateColorCodes('&', sb.toString());

        // ADMIN channel
        if (args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("adminchat") || args[0].equalsIgnoreCase("administrator")) {
            boolean canUseAdmin = sender.isOp() || (senderIsPlayer && plugin.hasAdminPerm(player));
            if (!canUseAdmin) {
                sender.sendMessage("§cYou do not have access to the ADMIN channel!");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("§cUsage: /staffchat <channel> <message>");
                sender.sendMessage("§cAll Channels: §bADMIN§c, §bSTAFF");
                return true;
            }

            if (senderIsPlayer) {
                if (plugin.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true)) {
                    plugin.sendAdminChannelMessage(player, msg);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7You currently have the staff chat §4disabled§7!"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7Type §e/staffchat togglechat §7to §are-enable §7it!"));
                }
            } else {
                plugin.sendAdminChannelMessage(sender, msg);
            }
            return true;
        }

        // STAFF channel
        if (args[0].equalsIgnoreCase("staff") || args[0].equalsIgnoreCase("staffchat")) {
            boolean canUseStaff = sender.isOp() || (senderIsPlayer && plugin.hasStaffPerm(player));
            if (!canUseStaff) {
                sender.sendMessage("§cYou do not have access to the STAFF channel!");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("§cUsage: /staffchat <channel> <message>");
                sender.sendMessage("§cAll Channels: §bADMIN§c, §bSTAFF");
                return true;
            }

            if (senderIsPlayer) {
                if (plugin.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true)) {
                    plugin.sendStaffChannelMessage(player, msg);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7You currently have the staff chat §4disabled§7!"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7Type §e/staffchat togglechat §7to §are-enable §7it!"));
                }
            } else {
                plugin.sendStaffChannelMessage(sender, msg);
            }
            return true;
        }

        // shownicknames
        if (args[0].equalsIgnoreCase("shownicknames")) {
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7You will §4no longer §7see staff nicknames!"));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7You will §anow §7see staff nicknames!"));
            }
            return true;
        }

        // togglechat
        if (args[0].equalsIgnoreCase("togglechat")) {
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7You will §4no longer §7see messages from §6Staff Chat§7!"));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§7You will §anow §7see messages from §6Staff Chat§7!"));
            }
            return true;
        }

        // reload / reloadcfg
        if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("reloadcfg")) {
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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.StaffConfig.getString("TAG") + " " + "§estaffchannel-data.yml §7reloaded!"));
            return true;
        }

        // Fallback help
        sender.sendMessage("§6StaffChannel §eCommands:");
        sender.sendMessage("§b/staffchat §3<channel> <message> §8- §7Send a message to staff.");
        sender.sendMessage("§b/staffchat §3shownicknames §8- §7Toggle nickname visibility.");
        sender.sendMessage("§b/staffchat §3togglechat §8- §7Toggle staff chat visibility.");
        sender.sendMessage("§b/staffchat §3reload §8- §7Reload the config file.");
        sender.sendMessage("§7All Channels: §4ADMIN§8, §5STAFF");
        return true;
    }
}
