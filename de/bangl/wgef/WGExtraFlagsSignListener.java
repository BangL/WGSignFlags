package de.bangl.wgef;

import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StringFlag;
import java.util.HashSet;
import org.bukkit.ChatColor;
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
        LocalPlayer wgPlayer = plugin.getWGP().wrapPlayer(player);
        
        ApplicableRegionSet regions = plugin.getWGP().getRegionManager(player.getWorld())
                .getApplicableRegions(event.getBlock().getLocation());
        
        HashSet<String> blocked = (HashSet<String>)regions.getFlag(FLAG_SIGNS_BLOCK, wgPlayer);
        HashSet<String> allowed = (HashSet<String>)regions.getFlag(FLAG_SIGNS_ALLOW, wgPlayer);

        if (!blocked.equals(null) && (blocked.contains(event.getLine(0).toLowerCase()))) {
            if (allowed.equals(null) || !allowed.contains(event.getLine(0).toLowerCase())) {
                // Looks like we are not in an allowed region, so let's see if we are on a block list... 

                // We are not on a blocked list, 
                event.setCancelled(true);
                String msg = this.plugin.getConfig().getString("messages.sign");
                player.sendMessage(ChatColor.RED + msg);
            }
        }
    }
}
