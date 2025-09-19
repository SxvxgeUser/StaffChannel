package me.SavageUser.StaffChannel;

import com.earth2me.essentials.Essentials;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class EssentialsHook implements Listener {


    private Essentials Essentials;

    private Plugin plugin;

    public EssentialsHook(final Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = Event.Priority.Normal)
    public void onPluginEnable(final PluginEnableEvent event)
    {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        final Plugin essentialsPlugin = pluginManager.getPlugin("Essentials");

        if (essentialsPlugin != null && essentialsPlugin.isEnabled() && essentialsPlugin instanceof Essentials)
        {
            Essentials = (Essentials)essentialsPlugin;

        }
    }

    @EventHandler(priority = Event.Priority.Normal)
    public void onPluginDisable(PluginDisableEvent event)
    {
        if (Essentials != null)
        {
            if (event.getPlugin().getDescription().getName().equals("Essentials"))
            {
                Essentials = null;
            }
        }
    }

    public Essentials getEssentials()
    {
        return Essentials;
    }

}
