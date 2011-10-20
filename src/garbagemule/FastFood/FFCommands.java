package garbagemule.FastFood;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class FFCommands implements CommandExecutor
{
    private FastFood plugin;
    private FoodHealth health;
    private Configuration config;
    
    public FFCommands(FastFood plugin)
    {
        this.plugin = plugin;
        this.health = plugin.getFoodHealth();
        this.config = plugin.getFFConfig();
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = sender instanceof Player ? (Player) sender : null;
        if (player != null && !player.hasPermission("fastfood.admin"))
        {
            plugin.tell(sender, "You do not have permission to use this command.");
            return false;
        }

        String arg1 = args.length > 0 ? args[0].toLowerCase() : null;
        String arg2 = args.length > 1 ? args[1].toLowerCase() : null;
        String arg3 = args.length > 2 ? args[2].toLowerCase() : null;
        
        if (arg1 == null)
        {
            plugin.tell(sender, "Usage:");
            plugin.tell(sender, "- /ff sethealth <material> <value>");
            plugin.tell(sender, "- /ff gethealth <material>");
            plugin.tell(sender, "- /ff settings <setting> <value>");
            return true;
        }
        
        if (arg1.equals("sethealth"))
        {
            // Require specific format.
            if (arg2 == null || arg3 == null)
            {
                plugin.tell(sender, "Usage: /ff sethealth <material> <value>");
                return true;
            }
            
            // Check that the material is valid.
            Material mat = getMaterial(arg2);
            if (mat == null)
            {
                plugin.tell(sender, "Item '" + arg2 + "' could not be found.");
                return true;
            }
            
            // Check that the value is valid
            if (!arg3.matches("(-)?[0-9]+"))
            {
                plugin.tell(sender, "Value must be an integer.");
                return true;
            }
            
            // Set the value.
            int value = Integer.parseInt(arg3);
            health.setHealth(mat, value);
            
            // Inform of change.
            if (value == 0)
                plugin.tell(sender, "Item '" + mat + "' removed.");
            else if (value > 0)
                plugin.tell(sender, "Item '" + mat + "' now heals " + value + " points when eaten.");
            else
                plugin.tell(sender, "Item '" + mat + "' now damages players by " + value + " points when eaten.");
            return true;
        }
        
        else if (arg1.equals("gethealth"))
        {
            // Require specific format.
            if (arg2 == null)
            {
                plugin.tell(sender, "Usage: /ff gethealth <material>");
                return true;
            }
            
            // Check that the material is valid.
            Material mat = getMaterial(arg2);
            if (mat == null)
            {
                plugin.tell(sender, "Item '" + arg2 + "' could not be found.");
                return true;
            }
            
            plugin.tell(sender, arg2 + ": " + health.getHealth(mat));
            return true;
        }
        
        else if (arg1.equals("settings"))
        {
            // Require specific format.
            if (arg2 == null || arg3 == null)
            {
                plugin.tell(sender, "Usage: /ff settings <setting> <value>");
                return true;
            }
            
            // Check that there is a setting with that name.
            FFSetting setting = FFSetting.fromString(arg2.replaceAll("-", ""));
            if (setting == null)
            {
                plugin.tell(sender, "There is no setting named '" + arg2 + "'.");
                return true;
            }
            
            // Check that the value is valid.
            if (!setting.valid(arg3))
            {
                plugin.tell(sender, "'" + arg3 + "' is not a valid setting for '" + arg2 + "'.");
                return true;
            }
            
            // Change the setting and save the config-file.
            config.setProperty("settings." + setting.getName(), setting.cast(arg3));
            config.save();
            
            // Inform of change.
            plugin.tell(sender, "Setting changed - " + setting.getName() + ": " + arg3);
            return true;
        }
        
        else if (arg1.equals("food"))
        {
            int food = Integer.parseInt(arg2);
            
            Player p = (Player) sender;
            p.setFoodLevel(food);
            return true;
        }
        
        else if (arg1.equals("health"))
        {
            int health = Integer.parseInt(arg2);
            
            Player p = (Player) sender;
            p.setHealth(health);
            return true;
        }
        
        return false;
    }
    
    private Material getMaterial(String s)
    {
        if (s.matches("[0-9]+"))
            return Material.getMaterial(Integer.parseInt(s));
        
        return Material.getMaterial(s.toUpperCase());
    }
}
