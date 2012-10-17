package de.bangl.wgef;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author BangL
 */
public class WGExtraFlagsPlugin extends JavaPlugin {

    // Plugins
    private WGCustomFlagsPlugin pluginWGCustomFlags;
    private WorldGuardPlugin pluginWorldGuard;

    // Listeners
    private WGExtraFlagsSignListener signListener;

    public WGCustomFlagsPlugin getWGCFP() {
        return pluginWGCustomFlags;
    }

    public WorldGuardPlugin getWGP() {
        return pluginWorldGuard;
    }

    @Override
    public void onEnable() {

        // Load config
        Utils.loadConfig(this);

        // Init WorldGuard
        pluginWorldGuard = Utils.getWorldGuard(this);
        //TODO: Check for null

        // Init Custom Flags
        pluginWGCustomFlags = Utils.getWGCustomFlags(this);
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
}
