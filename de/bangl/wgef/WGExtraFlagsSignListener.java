package de.bangl.wgef;

import com.sk89q.worldguard.protection.flags.SetFlag;
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

    public static final SetFlag<String> FLAG_SIGN_BLOCK = new SetFlag<>("sign-block", null);
    public static final SetFlag<String> FLAG_SIGN_ALLOW = new SetFlag<>("sign-allow", null);
    public static final SetFlag<String> FLAG_SIGN_XALLOW = new SetFlag<>("sign-xallow", null);

    public WGExtraFlagsSignListener(WGExtraFlagsPlugin plugin) {
        this.plugin = plugin;

        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Register custom flags
        plugin.getWGCFP().addCustomFlag(FLAG_SIGN_BLOCK);
        plugin.getWGCFP().addCustomFlag(FLAG_SIGN_ALLOW);
        plugin.getWGCFP().addCustomFlag(FLAG_SIGN_XALLOW);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChangeXXH(final SignChangeEvent event) {
        onSignChangeEvent(event, EventPriority.MONITOR);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChangehXH(final SignChangeEvent event) {
        onSignChangeEvent(event, EventPriority.HIGHEST);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onSignChangeH(final SignChangeEvent event) {
        onSignChangeEvent(event, EventPriority.HIGH);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChangeM(final SignChangeEvent event) {
        onSignChangeEvent(event, EventPriority.NORMAL);
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onSignChangeL(final SignChangeEvent event) {
        onSignChangeEvent(event, EventPriority.LOW);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChangeXL(final SignChangeEvent event) {
        onSignChangeEvent(event, EventPriority.LOWEST);
    }

    public void onSignChangeEvent(final SignChangeEvent event, EventPriority prio) {

        Player player = event.getPlayer();
        RegionManager regionManager = plugin.getWGP().getRegionManager(player.getWorld());
        Location loc = event.getBlock().getLocation();

        for (ProtectedRegion region : regionManager.getRegions().values()) {
            // Is this sign exclusively allowed anywhere in this world?
            if (this.checkFlag(FLAG_SIGN_XALLOW, region, event.getLine(0).toLowerCase())) {

                // There is atleast one region in this world
                // which allows this sign exclusively,
                // let's see if we are the lucky one in it ...
                for (ProtectedRegion applicableRegion : 
                        regionManager.getApplicableRegions(loc)) {
                    if (this.checkFlag(FLAG_SIGN_XALLOW, applicableRegion, event.getLine(0).toLowerCase())) {
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
            if (checkFlag(FLAG_SIGN_BLOCK, applicableRegion, event.getLine(0).toLowerCase())) {
                // Whoops! let's cancel this... 
                String msg = this.plugin.getConfig().getString("You are not allowed to place this kind of sign in this region.");
                player.sendMessage(ChatColor.RED + msg);
                event.setCancelled(true);
                return;
            }
        }

    }

    private Boolean checkFlag(SetFlag<String> flagtype, ProtectedRegion region, String signname) {
        Set<String> items = (Set<String>)region.getFlag(flagtype);
        return items.contains(signname);
    }

}
