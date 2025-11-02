package me.SavageUser.StaffChannel;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdmin implements CommandExecutor {
    private final Core plugin;

    public CommandAdmin(Core plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("adminchat").setExecutor(this);
    }

    String noPermission = "§cI'm sorry, Dave. I'm afraid I can't do that.";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean senderIsPlayer = sender instanceof Player;
        Player player = senderIsPlayer ? (Player) sender : null;

        if (cmd.getName().equalsIgnoreCase("adminchat") || cmd.getName().equalsIgnoreCase("ac")) {
            boolean canUseAny = sender.isOp() || (senderIsPlayer && (plugin.hasAdminPerm(player)));

            if (!canUseAny) {
                sender.sendMessage(noPermission);
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("§cUsage: /adminchat <player>");
                return true;
            }

            String msg = "";
            for (int i = 0; i < args.length; i++) {
                msg = msg + args[i] + " ";
            }
            msg = ChatColor.translateAlternateColorCodes('&', msg);

            if (senderIsPlayer) {
                if (plugin.StaffConfig.getConfigOption("Player-Settings." + player.getName() + ".See-Staff-Chat").equals(true)) {
                    plugin.sendAdminChannelMessage(player, msg);
                } else {
                    player.sendMessage("§6SC§8: §7You currently have the admin/staff chat §4disabled§7!");
                    player.sendMessage("§6SC§8: §7Type §e/staffchat togglechat §7to §are-enable §7it!");
                }
            } else {
                plugin.sendAdminChannelMessage(sender, msg);
            }
            return true;
        }
        return true;
    }
}
