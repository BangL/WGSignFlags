/*
 * Copyright (C) 2012 BangL <henno.rickowski@googlemail.com>
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
package de.bangl.wgsf.flags;

import com.mewin.WGCustomFlags.flags.CustomFlag;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import org.bukkit.command.CommandSender;

/**
 *
 * @author BangL <henno.rickowski@googlemail.com>
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
        return unmarshal(str);
    }

    @Override
    public String saveToDb(String o) {
        return (String) marshal(o);
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
