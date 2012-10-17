package de.bangl.wgef;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author BangL
 */
public class WGExtraFlagsPlugin extends JavaPlugin {

    private WGCustomFlagsPlugin pluginWGCustomFlags;
    private WorldGuardPlugin pluginWorldGuard;

    // Listeners
    private WGExtraFlagsSignListener signListener;

    @Override
    public void onEnable() {

        // Load config
        loadConfig();

        // Init WorldGuard
        pluginWorldGuard = getWorldGuard();
        //TODO: Check for null

        // Init Custom Flags
        pluginWGCustomFlags = getWGCustomFlags();
        //TODO: Check for null

        // Register all listeners
        this.signListener = new WGExtraFlagsSignListener(this);

    }

    @Override
    public void onDisable() {

        // we nullify all vars, cause it could be a server reload and we don't wanna leave trash in our expensive RAM.
        this.pluginWGCustomFlags = null;
        this.pluginWorldGuard = null;
        this.signListener = null;

    }

    private void loadConfig() {
        getConfig().addDefault("messages.sign", "You are not allowed to place this kind of sign in this region.");
        getConfig().addDefault("messages.command", "You are not allowed to execute this command in this region.");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private WGCustomFlagsPlugin getWGCustomFlags() {
        Plugin wgcf;
        wgcf = this.getServer().getPluginManager().getPlugin("WGCustomFlags");
        if (wgcf == null || !(wgcf instanceof WGCustomFlagsPlugin)) {
            return null;
        }
        return (WGCustomFlagsPlugin)wgcf;
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin wg;
        wg = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (wg == null || !(wg instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)wg;
    }

    public WGCustomFlagsPlugin getWGCFP() {
        return pluginWGCustomFlags;
    }

    public WorldGuardPlugin getWGP() {
        return pluginWorldGuard;
    }

}
