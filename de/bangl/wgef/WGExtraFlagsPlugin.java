package de.bangl.wgef;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.SimpleInjector;
import com.sk89q.wepif.PermissionsResolverManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.commands.ProtectionCommands;
import com.sk89q.worldguard.bukkit.commands.ToggleCommands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author BangL
 */
public class WGExtraFlagsPlugin extends JavaPlugin {

    /**
     * Manager for commands. This automatically handles nested commands,
     * permissions checking, and a number of other fancy command things.
     * We just set it up and register commands against it.
     */
    private final CommandsManager<CommandSender> commands;

    // Plugins
    private WGCustomFlagsPlugin pluginWGCustomFlags;
    private WorldGuardPlugin pluginWorldGuard;

    // Listeners
    private WGExtraFlagsSignListener signListener;

    public WGExtraFlagsPlugin() {
        final WGExtraFlagsPlugin plugin = this;
        commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender player, String perm) {
                return plugin.getWGP().hasPermission(player, perm);
            }
        };
    }
    
    public WGCustomFlagsPlugin getWGCFP() {
        return pluginWGCustomFlags;
    }

    public WorldGuardPlugin getWGP() {
        return pluginWorldGuard;
    }

    @Override
    public void onEnable() {

        // Set the proper command injector
        commands.setInjector(new SimpleInjector(this));

        // Register command classes
        final CommandsManagerRegistration reg = new CommandsManagerRegistration(this, commands);
        reg.register(ToggleCommands.class);
        reg.register(ProtectionCommands.class);

        // Load config
        Utils.loadConfig(this);

        // Init WorldGuard
        pluginWorldGuard = Utils.getWorldGuard(this);
        //TODO: Check for null

        // Init Custom Flags
        pluginWGCustomFlags = Utils.getWGCustomFlags(this);
        //TODO: Check for null

        // Register all listeners
        this.signListener = new WGExtraFlagsSignListener(this);
        
    }

    @Override
    public void onDisable() {
        // we nullify all vars, cause it could be a server reload and we don't wanna leave trash in our expensive RAM.
        this.pluginWGCustomFlags = null;
        this.pluginWorldGuard = null;
        this.signListener = null;
    }

}
