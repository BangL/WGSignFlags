package de.bangl.wgef;

import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author BangL
 */
public class WGExtraFlagsSignListener implements Listener {
    private WGExtraFlagsPlugin plugin;

    public static final CustomSetFlag FLAG_SIGNS_BLOCK = new CustomSetFlag("signs-block", new StringFlag("sign-block", RegionGroup.ALL));
    public static final CustomSetFlag FLAG_SIGNS_ALLOW = new CustomSetFlag("signs-allow", new StringFlag("sign-allow", RegionGroup.ALL));

    public WGExtraFlagsSignListener(WGExtraFlagsPlugin plugin) {
        this.plugin = plugin;

        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Register custom flags
        plugin.getWGCFP().addCustomFlag(FLAG_SIGNS_BLOCK);
        plugin.getWGCFP().addCustomFlag(FLAG_SIGNS_ALLOW);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChangeEvent(final SignChangeEvent event) {

        Player player = event.getPlayer();
        RegionManager regionManager = plugin.getWGP().getRegionManager(player.getWorld());
        Location loc = event.getBlock().getLocation();

        if (((Set<String>)regionManager.getApplicableRegions(loc).getFlag(FLAG_SIGNS_BLOCK, plugin.getWGP().wrapPlayer(player))).contains(event.getLine(0).toLowerCase())) {
            if (!((Set<String>)regionManager.getApplicableRegions(loc).getFlag(FLAG_SIGNS_ALLOW, plugin.getWGP().wrapPlayer(player))).contains(event.getLine(0).toLowerCase())) {
                // Looks like we are not in an allowed region, so let's see if we are on a block list... 

                // We are not on a blocked list, 
                event.setCancelled(true);
                String msg = this.plugin.getConfig().getString("You are not allowed to place this kind of sign in this region.");
                player.sendMessage(ChatColor.RED + msg);
            }
        }

    }

}
