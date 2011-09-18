package garbagemule.FastFood.listeners;

import garbagemule.FastFood.FastFood;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
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
        pm.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, this, Priority.Normal, plugin);
    }
    
    public void onEntityRegainHealth(EntityRegainHealthEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;
        
        Player p = (Player) event.getEntity();
        if (!p.hasPermission("fastfood.autoregain"))
            event.setCancelled(true);
    }
}
