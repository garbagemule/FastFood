package garbagemule.FastFood.listeners;

import garbagemule.FastFood.FastFood;
import garbagemule.FastFood.FoodHealth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.herocraftonline.dev.heroes.hero.Hero;

public class FFPlayerListener extends PlayerListener
{
    private FastFood plugin;
    private FoodHealth foodHealth;
    
    public FFPlayerListener(FastFood plugin, FoodHealth foodHealth)
    {
        this.plugin     = plugin;
        this.foodHealth = foodHealth;
        registerEvents();
    }
    
    private void registerEvents()
    {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_INTERACT, this, Priority.Normal, plugin);
    }
    
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Action a = event.getAction();
        if (!event.hasItem() || a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)
            return;

        // If the food doesn't have a value, return.
        int typeId = event.getItem().getTypeId();
        int health = foodHealth.getHealth(typeId);
        if (health == 0)
            return;

        // If the player has no permission, return.
        Player p = event.getPlayer();
        if (!p.hasPermission("fastfood.instanteat"))
            return;

        // Don't allow the player to eat if health is full, or if it will kill them!
        if ((health > 0 && p.getHealth() == 20) || (health < 0 && p.getHealth() + health <= 0))
            return;

        // Set health.
        int newHealth = Math.min(20, p.getHealth() + health);
        p.setHealth(newHealth);
        
        // If Heroes is enabled, play nice with it.
        if (plugin.getHeroManager() != null)
        {
            Hero hero = plugin.getHeroManager().getHero(p);
            hero.setHealth(newHealth * hero.getMaxHealth() / 20);
        }

        // Set hunger
        if (plugin.affectHunger())
        {
            double hunger = plugin.getHungerMultiplier() * health;
            hunger = hunger > 0D ? Math.max(1D, hunger) : Math.min(-1D, hunger);
            p.setFoodLevel(p.getFoodLevel() + (int) hunger);
        }

        // Deny the eat event, and remove the item.
        event.setUseItemInHand(Result.DENY);
        p.getInventory().removeItem(new ItemStack(typeId, 1));
    }
}
