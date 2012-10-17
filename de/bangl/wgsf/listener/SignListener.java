package de.bangl.wgsf.listener;

import com.mewin.WGCustomFlags.flags.CustomSetFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StringFlag;
import de.bangl.wgsf.Utils;
import de.bangl.wgsf.WGSignFlagsPlugin;
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
public class SignListener implements Listener {
    private WGSignFlagsPlugin plugin;

    // Sign flags
    public static final CustomSetFlag FLAG_SIGNS_BLOCK = new CustomSetFlag("signs-block", new StringFlag("sign-block", RegionGroup.ALL));
    public static final CustomSetFlag FLAG_SIGNS_ALLOW = new CustomSetFlag("signs-allow", new StringFlag("sign-allow", RegionGroup.ALL));

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
        
        Set<String> blocked = Utils.getFlag(plugin.getWGP(), FLAG_SIGNS_BLOCK, player, loc);
        Set<String> allowed = Utils.getFlag(plugin.getWGP(), FLAG_SIGNS_ALLOW, player, loc);

        if (blocked != null && blocked.contains(signname)
                && (allowed == null || !allowed.contains(signname))) {
            String msg = this.plugin.getConfig().getString("messages.sign");
            player.sendMessage(ChatColor.RED + msg);
            event.setCancelled(true);
        }
    }
}
