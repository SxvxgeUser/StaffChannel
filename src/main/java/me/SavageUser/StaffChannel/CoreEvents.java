package me.SavageUser.StaffChannel;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CoreEvents implements Listener {

    private Core core;

    public CoreEvents(Core core) {
    this.core = core;

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp() || core.hasAdminPerm(player)|| core.hasStaffPerm(player)) {
            if (Core.StaffConfig.getConfigOption("Player-Settings." + player.getName()) == null) {
                core.StaffConfig.createPlayerSettings(player.getName());
            }

            if (!core.hasStaffChatEnabled(player))  {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', core.StaffConfig.getString("TAG") + " " + "§7You currently have the staff chat §4disabled§7!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', core.StaffConfig.getString("TAG") + " " + "§7Type §e/staffchat togglechat §7to §are-enable §7it!"));
            }
        }

    }



}
