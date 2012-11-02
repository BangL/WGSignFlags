/*
 * Copyright (C) 2012 BangL <henno.rickowski@googlemail.com>
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
import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import de.bangl.wgsf.flags.SignFlag;
import de.bangl.wgsf.listener.SignListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author BangL <henno.rickowski@googlemail.com>
 */
public class WGSignFlagsPlugin extends JavaPlugin {

    // Sign flags
    public static final CustomSetFlag FLAG_SIGNS_BLOCK = new CustomSetFlag("signs-block", new SignFlag("sign-block", RegionGroup.ALL));
    public static final CustomSetFlag FLAG_SIGNS_ALLOW = new CustomSetFlag("signs-allow", new SignFlag("sign-allow", RegionGroup.ALL));

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

        // Register custom flags
        this.pluginWGCustomFlags.addCustomFlag(FLAG_SIGNS_BLOCK);
        this.pluginWGCustomFlags.addCustomFlag(FLAG_SIGNS_ALLOW);

        // Register all listeners
        this.listenerSign = new SignListener(this);
        
    }

    @Override
    public void onDisable() {

        // we nullify all vars, cause it could be a server reload and we don't wanna leave trash in our expensive RAM.
        this.pluginWGCustomFlags = null;
        this.pluginWorldGuard = null;
        this.listenerSign = null;

        saveConfig();
    }

}
