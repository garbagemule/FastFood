package garbagemule.FastFood.listeners;

import garbagemule.FastFood.FastFood;
import garbagemule.FastFood.FoodHealth;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
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
        
        // If the player left clicks, return
        if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)
            return;
        
        // The event must either have an item or a block
        if (!event.hasItem() && !event.hasBlock())
            return;
        
        // Check if the player has permission
        Player p = event.getPlayer();
        if (!p.hasPermission("fastfood.instanteat"))
            return;
        
        // If they do, handle either cake placement or food consumption.
        if (event.hasBlock() && event.getClickedBlock().getTypeId() == 92)
            onPlayerRightClickCake(p, event);
        else if (event.hasItem())
            onPlayerRightClick(p, event);
    }
    
    private void onPlayerRightClickCake(Player p, PlayerInteractEvent event)
    {
        // If right-clicking with cake, return.
        if (event.hasItem() && event.getItem().getTypeId() == 92)
            return;
        // If cake isn't defined, or if the player can't eat, return
        int health = foodHealth.getHealth(354);
        if (health == 0 || !canEat(p, health))
            return;
        
        // Eat the cake.
        eatCake(event.getClickedBlock());
        
        // Set the health.
        setHealth(p, health);
    }
    
    private void onPlayerRightClick(Player p, PlayerInteractEvent event)
    {
        // If the item has no health value, return
        int typeId = event.getItem().getTypeId();
        int health = foodHealth.getHealth(typeId);
        if (health == 0 || !canEat(p, health))
            return;
        
        // Set the health.
        setHealth(p, health);
        
        // Deny the eat event, and remove the item.
        event.setUseItemInHand(Result.DENY);
        p.getInventory().removeItem(new ItemStack(typeId, 1));
    }
    
    private void setHealth(Player p, int health)
    {
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
    }
    
    private void eatCake(Block cake)
    {
        // Grab the amount of eaten slices.
        byte eaten = cake.getData();
        
        // If this is the last piece, remove the cake.
        if (eaten == 5)
            cake.setTypeId(0);
        else
            cake.setData((byte) (eaten + 1));
    }
    
    /**
     * Check if the player either has full HP and thus won't benefit from healthy food,
     * or will die from eating unhealthy food.
     * @param p The player
     * @param value The health value (can be negative)
     * @return true, if the player can eat the food without problems, false otherwise
     */
    private boolean canEat(Player p, int value)
    {
        if (value > 0 && p.getHealth() >= 20)
            return false;
        if (value < 0 && p.getHealth() + value <= 0)
            return false;
        return true;
    }
}
