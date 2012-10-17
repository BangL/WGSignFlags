package de.bangl.wgef;

import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StringFlag;
import java.util.HashSet;
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
        Location loc = event.getBlock().getLocation();

        HashSet<String> blocked = Utils.getFlag(plugin.getWGP(), FLAG_SIGNS_BLOCK, player, loc);
        HashSet<String> allowed = Utils.getFlag(plugin.getWGP(), FLAG_SIGNS_ALLOW, player, loc);

        if (blocked.contains(event.getLine(0).toLowerCase())) {
            if (!allowed.contains(event.getLine(0).toLowerCase())) {
                // Looks like we are not in an allowed region, so let's see if we are on a block list... 

                // We are not on a blocked list, 
                event.setCancelled(true);
                String msg = this.plugin.getConfig().getString("messages.sign");
                player.sendMessage(ChatColor.RED + msg);
            }
        }
    }
}
