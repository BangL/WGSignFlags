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

import de.bangl.wgsf.Utils;
import de.bangl.wgsf.WGSignFlagsPlugin;
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

    public SignListener(WGSignFlagsPlugin plugin) {
        this.plugin = plugin;

        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChangeEvent(final SignChangeEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getBlock().getLocation();
        String signname = event.getLine(0).toLowerCase();

        if (!Utils.signAllowedAtLocation(this.plugin, signname, loc)) {
            String msg = this.plugin.getConfig().getString("messages.blocked");
            player.sendMessage(ChatColor.RED + msg);
            event.setCancelled(true);
        }
    }
}
