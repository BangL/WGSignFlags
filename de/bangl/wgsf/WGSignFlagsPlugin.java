package de.bangl.wgsf;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.bangl.wgsf.listener.SignListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author BangL
 */
public class WGSignFlagsPlugin extends JavaPlugin {

    // Plugins
    private WGCustomFlagsPlugin pluginWGCustomFlags;
    private WorldGuardPlugin pluginWorldGuard;

    // Listeners
    private SignListener listenerSign;

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
        this.pluginWorldGuard = Utils.getWorldGuard(this);
        //TODO: Check for null

        // Init and register custom flags
        this.pluginWGCustomFlags = Utils.getWGCustomFlags(this);

        // Register all listeners
        this.listenerSign = new SignListener(this);
        
    }

    @Override
    public void onDisable() {
        // we nullify all vars, cause it could be a server reload and we don't wanna leave trash in our expensive RAM.
        this.pluginWGCustomFlags = null;
        this.pluginWorldGuard = null;
        this.listenerSign = null;
    }

}
