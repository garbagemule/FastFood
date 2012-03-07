package garbagemule.FastFood.listeners;

import garbagemule.FastFood.FastFood;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.plugin.PluginManager;

public class FFEntityListener implements Listener
{
    private FastFood plugin;
    
    public FFEntityListener(FastFood plugin)
    {
        this.plugin = plugin;
        registerEvents();
    }
    
    private void registerEvents()
    {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getCause() != DamageCause.STARVATION || !(event.getEntity() instanceof Player))
            return;
        
        Player p = (Player) event.getEntity();
        if (p.hasPermission("fastfood.nostarve"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        Player p = (Player) event.getEntity();
        if (event.getRegainReason() == RegainReason.SATIATED && !p.hasPermission("fastfood.autoregain"))
            event.setCancelled(true);
    }
}
