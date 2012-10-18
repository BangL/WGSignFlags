package de.bangl.wgsf.flags;

import com.mewin.WGCustomFlags.flags.CustomFlag;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import org.bukkit.command.CommandSender;

/**
 *
 * @author BangL
 */
public class SignFlag extends CustomFlag<String>{

    public SignFlag(String name, RegionGroup rg)
    {
        super(name, rg);
    }

    public SignFlag(String name)
    {
        super(name);
    }

    @Override
    public String loadFromDb(String str) {
        return str;
    }

    @Override
    public String saveToDb(String o) {
        return o;
    }

    @Override
    public String parseInput(WorldGuardPlugin plugin, CommandSender sender, String input) throws InvalidFlagFormat {

        input = input.trim();
        if(!input.startsWith("[")) {
            input = "[" + input;
        }
        if(!input.endsWith("]")) {
            input = input + "]";
        }
        
        return input.toLowerCase();
    }

    @Override
    public String unmarshal(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else {
            return null;
        }
    }

    @Override
    public Object marshal(String o) {
        return o;
    }

}
