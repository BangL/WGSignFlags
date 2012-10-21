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
package de.bangl.wgsf.listener;

import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import de.bangl.wgsf.Utils;
import de.bangl.wgsf.WGSignFlagsPlugin;
import de.bangl.wgsf.flags.SignFlag;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author BangL <henno.rickowski@googlemail.com>
 * @author mewin <mewin001@hotmail.de>
 */
public class SignListener implements Listener {
    private WGSignFlagsPlugin plugin;

    // Sign flags
    public static final CustomSetFlag FLAG_SIGNS_BLOCK = new CustomSetFlag("signs-block", new SignFlag("sign-block", RegionGroup.ALL));
    public static final CustomSetFlag FLAG_SIGNS_ALLOW = new CustomSetFlag("signs-allow", new SignFlag("sign-allow", RegionGroup.ALL));

    public SignListener(WGSignFlagsPlugin plugin) {
        this.plugin = plugin;

        // Register custom flags
        plugin.getWGCFP().addCustomFlag(FLAG_SIGNS_BLOCK);
        plugin.getWGCFP().addCustomFlag(FLAG_SIGNS_ALLOW);

        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChangeEvent(final SignChangeEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getBlock().getLocation();
        String signname = event.getLine(0).toLowerCase();

        if (!Utils.signAllowedAtLocation(plugin.getWGP(), signname, loc)) {
            String msg = this.plugin.getConfig().getString("messages.blocked");
            player.sendMessage(ChatColor.RED + msg);
            event.setCancelled(true);
        }
    }
}
