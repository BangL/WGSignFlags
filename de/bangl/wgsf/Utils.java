package de.bangl.wgsf;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.bangl.wgsf.listener.SignListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author BangL, mewin
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
            result = new HashSet<>();
        }
        if (result != null) {
            return (HashSet<String>)result;
        } else {
            return new HashSet<>();
        }
    }
    
    public static boolean signAllowedAtLocation(WorldGuardPlugin wgp, String signname, Location loc)
    {
        RegionManager rm = wgp.getRegionManager(loc.getWorld());
        if (rm == null)
        {
            return true;
        }
        ApplicableRegionSet regions = rm.getApplicableRegions(loc);
        Iterator<ProtectedRegion> itr = regions.iterator();
        Map<ProtectedRegion, Boolean> regionsToCheck = new HashMap<>();
        Set<ProtectedRegion> ignoredRegions = new HashSet<>();
        
        while(itr.hasNext())
        {
            ProtectedRegion region = itr.next();
            
            if (ignoredRegions.contains(region))
            {
                continue;
            }
            
            Object allowed = signAllowedInRegion(region, signname);
            
            if (allowed != null)
            {
                ProtectedRegion parent = region.getParent();
                
                while(parent != null)
                {
                    ignoredRegions.add(parent);
                    
                    parent = parent.getParent();
                }
                
                regionsToCheck.put(region, (boolean) allowed);
            }
        }
        
        if (regionsToCheck.size() >= 1)
        {
            Iterator<Entry<ProtectedRegion, Boolean>> itr2 = regionsToCheck.entrySet().iterator();
            
            while(itr2.hasNext())
            {
                Entry<ProtectedRegion, Boolean> entry = itr2.next();
                
                ProtectedRegion region = entry.getKey();
                boolean value = entry.getValue();
                
                if (ignoredRegions.contains(region))
                {
                    continue;
                }
                
                if (value) // allow > deny
                {
                    return true;
                }
            }
            
            return false;
        }
        else
        {
            Object allowed = signAllowedInRegion(rm.getRegion("__global__"), signname);
            
            if (allowed != null)
            {
                return (boolean) allowed;
            }
            else
            {
                return true;
            }
        }
    }
    
    public static Object signAllowedInRegion(ProtectedRegion region, String signname)
    {
        HashSet<String> allowedSigns = (HashSet<String>) region.getFlag(SignListener.FLAG_SIGNS_ALLOW);
        HashSet<String> blockedSigns = (HashSet<String>) region.getFlag(SignListener.FLAG_SIGNS_BLOCK);
        
        if (allowedSigns != null && allowedSigns.contains(signname))
        {
            return true;
        }
        else if(blockedSigns != null && blockedSigns.contains(signname))
        {
            return false;
        }
        else
        {
            return null;
        }
    }
}
