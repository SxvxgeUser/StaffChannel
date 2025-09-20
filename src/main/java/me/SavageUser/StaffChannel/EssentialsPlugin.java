package me.SavageUser.StaffChannel;

import org.bukkit.entity.Player;
import com.earth2me.essentials.Essentials;

public class EssentialsPlugin {

    private Essentials essentials;
    private Core plugin;

    public EssentialsPlugin(Core plugin) {
        this.plugin = plugin;
        this.essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
    }

    public String getNick(String name) {
        String nickname = essentials.getUser(name).getNickname();
        return (nickname != null && !nickname.isEmpty()) ? nickname : name;
    }

    public String getNick(Player player) {
        return getNick(player.getName());
    }

}
