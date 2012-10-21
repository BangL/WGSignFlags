/*
 * Copyright (C) 2012 BangL <henno.rickowski@googlemail.com>
 *                    mewin <mewin001@hotmail.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.bangl.wgsf;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
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
import org.bukkit.plugin.Plugin;

/**
 *
 * @author BangL <henno.rickowski@googlemail.com>
 * @author mewin <mewin001@hotmail.de>
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

    public static boolean signAllowedAtLocation(WorldGuardPlugin wgp, String signname, Location loc) {

        // Valid world?
        RegionManager rm = wgp.getRegionManager(loc.getWorld());
        if (rm == null) {
            return true;
        }

        Map<ProtectedRegion, Boolean> regionsToCheck = new HashMap<>();
        Set<ProtectedRegion> ignoredRegions = new HashSet<>();

        Iterator<ProtectedRegion> itr = rm.getApplicableRegions(loc).iterator();
        while(itr.hasNext()) {
            ProtectedRegion region = itr.next();
            
            if (ignoredRegions.contains(region)) {
                continue;
            }
            
            Object allowed = signAllowedInRegion(region, signname);
            
            if (allowed != null) {
                ProtectedRegion parent = region.getParent();
                while(parent != null) {
                    ignoredRegions.add(parent);
                    parent = parent.getParent();
                }
                regionsToCheck.put(region, (boolean) allowed);
            }
        }
        
        if (regionsToCheck.size() >= 1) {

            Iterator<Entry<ProtectedRegion, Boolean>> itr2 = regionsToCheck.entrySet().iterator();
            while(itr2.hasNext()) {
                Entry<ProtectedRegion, Boolean> entry = itr2.next();
                ProtectedRegion region = entry.getKey();
                boolean value = entry.getValue();

                if (ignoredRegions.contains(region)) {
                    continue;
                }

                // allow > deny
                if (value) {
                    return true;
                }
            }
            return false;
        } else {

            Object allowed = signAllowedInRegion(rm.getRegion("__global__"), signname);
            if (allowed != null) {
                return (boolean) allowed;
            } else {
                return true;
            }

        }
    }
    
    public static Boolean signAllowedInRegion(ProtectedRegion region, String signname) {

        HashSet<String> allowedSigns = (HashSet<String>)region.getFlag(SignListener.FLAG_SIGNS_ALLOW);
        HashSet<String> blockedSigns = (HashSet<String>)region.getFlag(SignListener.FLAG_SIGNS_BLOCK);
        
        if (allowedSigns != null && allowedSigns.contains(signname)) {

            // Allowed
            return true;

        } else if(blockedSigns != null && blockedSigns.contains(signname)) {

            // Blocked
            return false;

        } else {

            // Never heard about this sign, i dont care.
            return null;

        }
    }
}
