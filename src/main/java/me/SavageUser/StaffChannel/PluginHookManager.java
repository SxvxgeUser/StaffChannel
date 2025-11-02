package me.SavageUser.StaffChannel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginHookManager implements Listener {

    private final Core plugin;

    public PluginHookManager(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginDisable(PluginEnableEvent event) {

        String hookedPluginName = event.getPlugin().getDescription().getName();

        if(plugin.getDependenciesList().contains(hookedPluginName)){

        }

    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        String hookedPluginName = event.getPlugin().getDescription().getName();

        if(plugin.getDependenciesList().contains(hookedPluginName)){
            plugin.logger.info("Dependency " + hookedPluginName + " has been disabled, disabling StaffChannel");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

    }
}
