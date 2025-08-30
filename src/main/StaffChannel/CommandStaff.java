package StaffChannel;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStaff implements CommandExecutor {

    private final Core plugin;

    public CommandStaff(Core plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("staffchat").setExecutor(this);
    }

    String noPermission = "§cI'm sorry, Dave. I'm afraid I can't do that.";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("staffchat") || cmd.getName().equalsIgnoreCase("sc")) {

            if (sender.isOp() || sender.hasPermission("StaffChannel.Admin") || sender.hasPermission("StaffChannel.Staff")) {
                if (args.length == 0 || args.length == 1) {
                    sender.sendMessage("§cUsage: /staffchat <channel> <message>");
                    sender.sendMessage("§cAll Channels: §bADMIN§c, §bSTAFF");
                    return true;
                }

                String msg = "";
                for (int i = 1; i < args.length; ++i) {
                    msg = msg + args[i] + " ";
                }
                msg = ChatColor.translateAlternateColorCodes('&', msg);

                if (args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("adminchat") || args[0].equalsIgnoreCase("administrator")) {
                    if (sender.isOp() || sender.hasPermission("StaffChannel.Admin")) {
                        plugin.sendAdminChannelMessage(sender, msg);
                    }
                    else {
                        sender.sendMessage("§cYou do not have access to the ADMIN channel!");
                        return true;
                    }
                }

                else if (args[0].equalsIgnoreCase("staff") || args[0].equalsIgnoreCase("staffchat")) {
                    if (sender.isOp() || sender.hasPermission("StaffChannel.Staff")) {
                        plugin.sendStaffChannelMessage(sender, msg);
                    }
                    else {
                        sender.sendMessage("§cYou do not have access to the STAFF channel!");
                        return true;
                    }
                }
                else {
                    sender.sendMessage("§cUnrecognized channel!");
                    sender.sendMessage("§cAll Channels: §bADMIN§c, §bSTAFF");
                    return true;
                }
            }
            else {
                sender.sendMessage(noPermission); //No permission.
                return true;
            }
        }
        return true;
    }
}
