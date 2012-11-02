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
import com.mewin.util.Util;
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

    public static boolean signAllowedAtLocation(WGSignFlagsPlugin plugin, String signname, Location loc) {
        return Util.flagAllowedAtLocation(plugin.getWGP(), signname, loc, WGSignFlagsPlugin.FLAG_SIGNS_ALLOW, WGSignFlagsPlugin.FLAG_SIGNS_BLOCK, null);
    }
}
