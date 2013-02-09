package garbagemule.FastFood;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;

import garbagemule.FastFood.util.Enums;
import garbagemule.util.syml.SymlConfig;

public class FoodHealth
{
    private Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    private SymlConfig config;
    
    public FoodHealth(SymlConfig config)
    {
        this.config = config;
        
        Set<String> keys = config.getKeys();
        if (keys == null)
        {
            FastFood.warning("No keys found in foodhealth.yml.");
            return;
        }
        
        for (String s : keys)
        {
            Material mat = fromString(s);
            if (mat == null)
            {
                FastFood.warning("The key '" + s + "' is invalid.");
                continue;
            }
            
            int value = config.getInt(s, 0);
            if (value == 0)
            {
                FastFood.warning("The value of key '" + s + "' is 0. Skipping...");
                continue;
            }
            
            map.put(mat.getId(), value);
        }
    }
    
    /**
     * Get the health value of a Material from the config-file.
     * @param mat The Material whose health value to get
     * @return The health value if it exists, 0 otherwise.
     */
    public int getHealth(Material mat)
    {
        return getHealth(mat.getId());
    }
    
    /**
     * Get the health value of a type ID from the config-file.
     * @param id The type ID whose health value to get
     * @return The health value if it exists, 0 otherwise.
     */
    public int getHealth(int id)
    {
        Integer health = map.get(id);
        return (health == null) ? 0 : health;
    }
    
    /**
     * Set the health value of a type ID.
     * @param id The type ID whose value to set.
     * @param value A health value.
     */
    public void setHealth(int id, int value)
    {
        setHealth(Material.getMaterial(id), value);
    }
    
    /**
     * Set the health value of a Material.
     * @param mat The Material whose value to set.
     * @param value A health value.
     */
    public void setHealth(Material mat, int value)
    {
        map.put(mat.getId(), value);
        config.set(mat.toString().toLowerCase(), value);
        config.save();
    }
    
    public Material fromString(String s)
    {
        return Enums.getEnumFromString(Material.class, s);
    }
}
