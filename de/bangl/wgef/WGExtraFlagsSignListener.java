package de.bangl.wgef;

import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
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
    public static final CustomSetFlag FLAG_SIGNS_XALLOW = new CustomSetFlag("signs-xallow", new StringFlag("sign-xallow", RegionGroup.ALL));

    public WGExtraFlagsSignListener(WGExtraFlagsPlugin plugin) {
        this.plugin = plugin;

        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Register custom flags
        plugin.getWGCFP().addCustomFlag(FLAG_SIGNS_BLOCK);
        plugin.getWGCFP().addCustomFlag(FLAG_SIGNS_ALLOW);
        plugin.getWGCFP().addCustomFlag(FLAG_SIGNS_XALLOW);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChangeEvent(final SignChangeEvent event) {

        Player player = event.getPlayer();
        RegionManager regionManager = plugin.getWGP().getRegionManager(player.getWorld());
        Location loc = event.getBlock().getLocation();

        for (ProtectedRegion region : regionManager.getRegions().values()) {
            // Is this sign exclusively allowed anywhere in this world?
            if (this.checkFlag(FLAG_SIGNS_XALLOW, region, event.getLine(0).toLowerCase())) {

                // There is atleast one region in this world
                // which allows this sign exclusively,
                // let's see if we are the lucky one in it ...
                for (ProtectedRegion applicableRegion : 
                        regionManager.getApplicableRegions(loc)) {
                    if (this.checkFlag(FLAG_SIGNS_XALLOW, applicableRegion, event.getLine(0).toLowerCase())) {
                        // We are in the lucky region! let's allow it...
                        return;
                    }
                }
                // Looks like we are not in the allowed region, so let's cancel this... 
                event.setCancelled(true);
                String msg = this.plugin.getConfig().getString("You are not allowed to place this kind of sign in this region.");
                player.sendMessage(ChatColor.RED + msg);
                return;
            }
        }

        // This was not an exclusively allowed sign, so let's check if we are blacklisted...
        for (ProtectedRegion applicableRegion : 
                regionManager.getApplicableRegions(loc)) {
            if (checkFlag(FLAG_SIGNS_BLOCK, applicableRegion, event.getLine(0).toLowerCase())) {
                // Whoops! let's cancel this... 
                String msg = this.plugin.getConfig().getString("You are not allowed to place this kind of sign in this region.");
                player.sendMessage(ChatColor.RED + msg);
                event.setCancelled(true);
                return;
            }
        }

    }

    private Boolean checkFlag(CustomSetFlag flagtype, ProtectedRegion region, String signname) {
        Object flag = region.getFlag(flagtype);
        if (flag instanceof StringFlag) {
          return ((Set<String>)(StringFlag)region.getFlag(flagtype)).contains(signname);
        }
        return null;
    }

}
