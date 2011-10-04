package garbagemule.FastFood.listeners;

import garbagemule.FastFood.FastFood;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.plugin.PluginManager;

public class FFEntityListener extends EntityListener
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
        pm.registerEvent(Event.Type.ENTITY_DAMAGE,        this, Priority.Normal, plugin);
        pm.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, this, Priority.Normal, plugin);
    }
    
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getCause() != DamageCause.STARVATION || !(event.getEntity() instanceof Player))
            return;
        
        Player p = (Player) event.getEntity();
        if (p.hasPermission("fastfood.nostarve"))
            event.setCancelled(true);
    }
    
    public void onEntityRegainHealth(EntityRegainHealthEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        Player p = (Player) event.getEntity();
        if (event.getRegainReason() == RegainReason.SATIATED && !p.hasPermission("fastfood.autoregain"))
            event.setCancelled(true);
    }
}
