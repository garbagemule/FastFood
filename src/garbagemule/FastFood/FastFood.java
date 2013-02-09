package garbagemule.FastFood;

import java.io.File;

import garbagemule.FastFood.util.Files;
import garbagemule.FastFood.listeners.*;
import garbagemule.util.syml.SymlConfig;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class FastFood extends JavaPlugin
{
    private SymlConfig config, healthConfig;
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
    
    private SymlConfig getConfigFromFile(File dir, String filename)
    {
        if (!dir.exists()) dir.mkdir();
        
        Files.extract(filename, dir);
        File file = new File(dir, filename);
        
        SymlConfig config = new SymlConfig(file);
        config.load();
        return config;
    }
    
    public boolean affectHunger()
    {
        return affectHunger;
    }
    
    public double getHungerMultiplier()
    {
        return hungerMultiplier;
    }
    
    public SymlConfig getFFConfig()
    {
        return config;
    }
    
    public FoodHealth getFoodHealth()
    {
        return health;
    }
    
    public void tell(CommandSender p, String msg)
    {
        p.sendMessage(ChatColor.YELLOW + "[FastFood] " + ChatColor.WHITE + msg);
    }
    
    public static void info(String msg) { Bukkit.getServer().getLogger().info("[FastFood] " + msg); }
    public static void warning(String msg) { Bukkit.getServer().getLogger().warning("[FastFood] " + msg); }
}
