package garbagemule.FastFood;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.material.MaterialData;
import org.bukkit.Material;

import garbagemule.FastFood.util.Enums;
import garbagemule.util.syml.SymlConfig;

public class FoodHealth {
    private static final int ENCHANTED_GOLDEN_APPLE_ID = Integer.MIN_VALUE;
    private static final int PUFFER_FISH = 580;
    private static final int COD_FISH = 581;
    private static final int CLOWN_FISH = 582;
    private static final int SALMON_FISH = 583;
    private static final int COOKED_SALMON_FISH = 584;
    private static final int COOKED_COD_FISH = 585;
    private Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    private SymlConfig config;
    
    public FoodHealth(SymlConfig config) {
        this.config = config;
        
        Set<String> keys = config.getKeys();
        if (keys == null) {
            FastFood.warning("No keys found in foodhealth.yml.");
            return;
        }
        
        for (String s : keys) {
            Material mat = fromString(s);
            
            
            int id = 0;
            
            // Map the value to the material ID
            if (mat != null) {
                id = mat.getId();
                
                
            } else if(s.equalsIgnoreCase("COD_FISH")){
            id =COD_FISH;
            } else if(s.equalsIgnoreCase("SALMON_FISH")){
            id=SALMON_FISH;
            } else if(s.equalsIgnoreCase("CLOWN_FISH")){
            id=CLOWN_FISH;
            } else if(s.equalsIgnoreCase("PUFFER_FISH")){
            id=PUFFER_FISH;
            }else if(s.equalsIgnoreCase("COOKED_SALMON_FISH")){
            id=COOKED_SALMON_FISH;
            }else if(s.equalsIgnoreCase("COOKED_COD_FISH")){
            id=COOKED_COD_FISH;
            }else {
                // Enchanted golden apples are special, handle with care
                if (s.equals("enchanted_golden_apple")) {
                    id = ENCHANTED_GOLDEN_APPLE_ID;
                } else {
                    FastFood.warning("The key '" + s + "' is invalid.");
                    continue;
                }
            }
            
            // Grab the value out of the config
            int value = config.getInt(s, 0);
            if (value == 0) {
                FastFood.warning("The value of key '" + s + "' is 0. Skipping...");
                continue;
            }
            
            map.put(id, value);
        }
    }
    
    /**
     * Get the health value of a Material from the config-file.
     * @param mat The Material whose health value to get
     * @return The health value if it exists, 0 otherwise.
     */
    public int getHealth(Material mat) {
        return getHealth(mat.getId());
    }
    
    /**
     * Get the health value of a type ID from the config-file.
     * @param id The type ID whose health value to get
     * @return The health value if it exists, 0 otherwise.
     */
    public int getHealth(int id) {
        Integer health = map.get(id);
        return (health == null) ? 0 : health;
    }
    
    /**
     * Get the special value for the enchanted golden apple.
     * @return The health value if it exists, 0 otherwise.
     */
    public int getEnchantedGoldenApple() {
        return getHealth(ENCHANTED_GOLDEN_APPLE_ID);
    }
    
    /**
     * Set the health value of a type ID.
     * @param id The type ID whose value to set.
     * @param value A health value.
     */
    public void setHealth(int id, int value) {
        setHealth(Material.getMaterial(id), value);
    }
    
    /**
     * Set the health value of a Material.
     * @param mat The Material whose value to set.
     * @param value A health value.
     */
    public void setHealth(Material mat, int value) {
        map.put(mat.getId(), value);
        config.set(mat.toString().toLowerCase(), value);
        config.save();
    }
    
    public Material fromString(String s) {
        return Enums.getEnumFromString(Material.class, s);
    }
}
