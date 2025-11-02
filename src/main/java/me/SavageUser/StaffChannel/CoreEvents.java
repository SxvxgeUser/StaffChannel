package me.SavageUser.StaffChannel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CoreEvents implements Listener {

    private Core plugin;

    public CoreEvents(Core plugin) {
        this.plugin = plugin;

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp() || plugin.hasAdminPerm(player)|| plugin.hasStaffPerm(player)) {
            if (Core.StaffConfig.getConfigOption("Player-Settings." + player.getName()) == null) {
                plugin.StaffConfig.createPlayerSettings(player.getName());
            }

            if (!plugin.hasStaffChatEnabled(player))  {
                player.sendMessage("§6SC§8: §7You currently have the admin/staff chat §4disabled§7!");
                player.sendMessage("§6SC§8: §7Type §e/staffchat togglechat §7to §are-enable §7it!");
            }
        }

    }
}
