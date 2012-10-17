package de.bangl.wgsf;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author BangL
 */
public final class Utils {
    
    public static WGCustomFlagsPlugin getWGCustomFlags(WGSignFlagsPlugin plugin) {
        Plugin wgcf = plugin.getServer().getPluginManager().getPlugin("WGCustomFlags");
        if (wgcf == null || !(wgcf instanceof WGCustomFlagsPlugin)) {
            return null;
        }
        return (WGCustomFlagsPlugin)wgcf;
    }

    public static WorldGuardPlugin getWorldGuard(WGSignFlagsPlugin plugin) {
        Plugin wg = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        if (wg == null || !(wg instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)wg;
    }

    public static void loadConfig(WGSignFlagsPlugin plugin) {
        plugin.getConfig().addDefault("messages.blocked", "You are not allowed to place this kind of sign in this region.");
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public static HashSet<String> getFlag(WorldGuardPlugin wgp, CustomSetFlag flag, Player player, Location loc) {
        LocalPlayer wgPlayer = wgp.wrapPlayer(player);
        ApplicableRegionSet regions = wgp.getRegionManager(loc.getWorld())
                .getApplicableRegions(loc);
        Object result = null;
        try {
            result = (HashSet<String>)regions.getFlag(flag, wgPlayer);
        } catch(Exception e) {
            result = new HashSet<String>();
        }
        if (result != null) {
            return (HashSet<String>)result;
        } else {
            return new HashSet<String>();
        }
    }
}
