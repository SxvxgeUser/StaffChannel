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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("staffchat") || cmd.getName().equalsIgnoreCase("sc")) {
            if (sender.isOp() || plugin.hasAdminPerm(sender) || plugin.hasStaffPerm(sender)) {
                if (args.length == 0) {
                    sender.sendMessage("§6StaffChannel §eCommands:");
                    sender.sendMessage("§b/staffchat §3<channel> <message> §8- §7Send a message to staff.");
                    sender.sendMessage("§b/staffchat §3shownicknames §8- §7Toggle nickname visibility.");
                    sender.sendMessage("§b/staffchat §3togglechat §8- §7Toggle staff chat visibility.");
                    sender.sendMessage("§b/staffchat §3reload §8- §7Reload the config file.");
                    sender.sendMessage("§7All Channels: §4ADMIN§8, §5STAFF");
                    return true;
                }
                String msg = "";
                for (int i = 1; i < args.length; i++) {
                    msg = msg + args[i] + " ";
                }
                msg = ChatColor.translateAlternateColorCodes('&', msg);

                if (args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("adminchat") || args[0].equalsIgnoreCase("administrator")) {
                    if (sender.isOp() || plugin.hasAdminPerm(sender)) {
                        if (args.length < 2) {
                            sender.sendMessage("§cUsage: /staffchat <channel> <message>");
                            sender.sendMessage("§cAll Channels: §bADMIN§c, §bSTAFF");
                            return true;
                        }
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (plugin.config.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true)) {
                                this.plugin.sendAdminChannelMessage(player, msg);
                                return true;

                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7You currently have the staff chat §4disabled§7!"));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7Type §e/staffchat togglechat §7to §are-enable §7it!"));
                                return true;
                            }
                        }
                        else {
                            this.plugin.sendAdminChannelMessage(sender, msg);
                            return true;
                        }
                    } else {
                        sender.sendMessage("§cYou do not have access to the ADMIN channel!");
                        return true;
                    }
                }
                else if (args[0].equalsIgnoreCase("staff") || args[0].equalsIgnoreCase("staffchat")) {
                    if (sender.isOp() || plugin.hasStaffPerm(sender)) {
                        if (args.length < 2) {
                            sender.sendMessage("§cUsage: /staffchat <channel> <message>");
                            sender.sendMessage("§cAll Channels: §bADMIN§c, §bSTAFF");
                            return true;
                        }
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (plugin.config.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true)) {
                                this.plugin.sendStaffChannelMessage(player, msg);
                                return true;

                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7You currently have the staff chat §4disabled§7!"));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7Type §e/staffchat togglechat §7to §are-enable §7it!"));
                                return true;
                            }
                        }
                        else {
                            this.plugin.sendStaffChannelMessage(sender, msg);
                            return true;
                        }
                    } else {
                        sender.sendMessage("§cYou do not have access to the STAFF channel!");
                        return true;
                    }
                }

                else if (args[0].equalsIgnoreCase("shownicknames")) {
                    if (sender.isOp() || plugin.hasAdminPerm(sender) || plugin.hasStaffPerm(sender)) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;

                            if (args.length != 1) {
                                player.sendMessage("§cUsage: /staffchat shownicknames");
                                return true;
                            }

                            if (plugin.config.getConfigOption("Player-Settings." + player.getName() + ".Show-Nicknames").equals(true)) {
                                plugin.updateNicknameSetting(player, false);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7You will §4no longer §7see staff nicknames!"));
                                return true;
                            } else {
                                plugin.updateNicknameSetting(player, true);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7You will §anow §7see staff nicknames!"));
                                return true;
                            }
                        } else {
                            sender.sendMessage("Only players can execute this command!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(noPermission);
                        return true;
                    }
                }

                else if (args[0].equalsIgnoreCase("togglechat")) {
                    if (sender.isOp() || plugin.hasAdminPerm(sender) || plugin.hasStaffPerm(sender)) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;

                            if (args.length != 1) {
                                player.sendMessage("§cUsage: /staffchat togglechat");
                                return true;
                            }

                            if (plugin.config.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true)) {
                                plugin.updatePlayerStaffChatSetting(player, false);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7You will §4no longer §7see messages from §6Staff Chat§7!"));
                                return true;
                            } else {
                                plugin.updatePlayerStaffChatSetting(player, true);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§7You will §anow §7see messages from §6Staff Chat§7!"));
                                return true;
                            }
                        } else {
                            sender.sendMessage("Only players can execute this command!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(noPermission);
                        return true;
                    }
                }
                else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("reloadcfg")) {
                    if (sender.isOp() || plugin.hasAdminPerm(sender)) {
                        if (args.length != 1) {
                            sender.sendMessage("§cUsage: /staffchat reload or /staffchat reloadcfg");
                            return true;
                        }
                        plugin.config.reload();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("TAG") + " " + "§estaffchannel-data.yml §7reloaded!"));
                        return true;

                    } else {
                        sender.sendMessage(noPermission);
                        return true;
                    }
                }
                else {
                    sender.sendMessage("§6StaffChannel §eCommands:");
                    sender.sendMessage("§b/staffchat §3<channel> <message> §8- §7Send a message to staff.");
                    sender.sendMessage("§b/staffchat §3shownicknames §8- §7Toggle nickname visibility.");
                    sender.sendMessage("§b/staffchat §3togglechat §8- §7Toggle staff chat visibility.");
                    sender.sendMessage("§b/staffchat §3reload §8- §7Reload the config file.");
                    sender.sendMessage("§7All Channels: §4ADMIN§8, §5STAFF");
                    return true;
                }
            } else {
                sender.sendMessage(noPermission);
                return true;
            }
        }
        return true;
    }
}
