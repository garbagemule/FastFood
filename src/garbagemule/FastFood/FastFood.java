package garbagemule.FastFood;

import java.io.File;

import garbagemule.FastFood.util.Files;
import garbagemule.FastFood.listeners.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class FastFood extends JavaPlugin
{
    private Configuration config, healthConfig;
    private FoodHealth health;
    private double hungerMultiplier;
    private boolean affectHunger;
    
    public void onEnable()
    {
        // Health collection.
        setupHealthConfig();
        
        // Config-file.
        setupConfig();
        
        // Listeners.
        new FFEntityListener(this);
        new FFPlayerListener(this, health);
        
        // Commands
        CommandExecutor commandExecutor = new FFCommands(this); 
        getCommand("ff").setExecutor(commandExecutor); 
        getCommand("fastfood").setExecutor(commandExecutor);
        
        info("v" + getDescription().getVersion() + " enabled.");
    }

    public void onDisable()
    {
        info("v" + getDescription().getVersion() + " disabled.");
    }
    
    private void setupHealthConfig()
    {
        healthConfig = getConfigFromFile(getDataFolder(), "foodhealth.yml");
        
        health = new FoodHealth(healthConfig);
    }
    
    private void setupConfig()
    {
        config = getConfigFromFile(getDataFolder(), "config.yml");
        
        affectHunger     = config.getBoolean("settings.affect-hunger", false);
        hungerMultiplier = config.getDouble("settings.hunger-multiplier", 0D);
    }
    
    private Configuration getConfigFromFile(File dir, String filename)
    {
        if (!dir.exists()) dir.mkdir();
        
        Files.extract(filename, dir);
        File file = new File(dir, filename);
        
        Configuration config = new Configuration(file);
        config.load();
        config.setHeader(getHeader(filename));
        config.save();
        return config;
    }
    
    private String getHeader(String filename)
    {
        if (filename.equals("config.yml"))
            return "# FastFood v" + getDescription().getVersion() + " config file\n" +
            "#\n" + 
            "# This file contains all the settings that FastFood uses. All of them can be\n" + 
            "# altered in-game by typing the following command.\n" + 
            "#     /ff settings <setting> <value>\n" + 
            "#";
        else if (filename.equals("foodhealth.yml"))
            return "# FastFood v" + getDescription().getVersion() + " food health file\n" +
            "#\n" +
            "# Use this file to specify the MATERIAL TYPES of the items you want to allow\n" +
            "# players to instant-eat, and how much each item should heal.\n" +
            "#     Material types: http://jd.bukkit.org/apidocs/org/bukkit/Material.html\n" +
            "#\n" +
            "# Items can be added/deleted/modified in-game with the following command.\n" +
            "# Setting a material to 0 removes it from the file. Tou can use data values\n" +
            "# in the command, but the material type will be used in this file.\n" +
            "#     /ff sethealth <material> <value>\n" +
            "#";
        else return "# FastFood v" + getDescription().getVersion() + "\n";
    }
    
    public boolean affectHunger()
    {
        return affectHunger;
    }
    
    public double getHungerMultiplier()
    {
        return hungerMultiplier;
    }
    
    public Configuration getConfig()
    {
        return config;
    }
    
    public FoodHealth getFoodHealth()
    {
        return health;
    }
    
    public static void info(String msg) { Bukkit.getServer().getLogger().info("[FastFood] " + msg); }
    public static void warning(String msg) { Bukkit.getServer().getLogger().warning("[FastFood] " + msg); }
    
    public void tell(CommandSender p, String msg)
    {
        p.sendMessage(ChatColor.YELLOW + "[FastFood] " + ChatColor.WHITE + msg);
    }
}
