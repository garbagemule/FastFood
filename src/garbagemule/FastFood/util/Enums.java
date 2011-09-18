package garbagemule.FastFood.util;

public class Enums
{
    /**
     * Get the num value of a string, def if it doesn't exist.
     */
    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string, T def)
    {
        if (c != null && string != null)
        {
            try
            {
                return Enum.valueOf(c, string.trim().toUpperCase());
            }
            catch (IllegalArgumentException ex) { }
        }
        return def;
    }
    
    /**
     * Get the enum value of a string, null if it doesn't exist.
     */
    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string)
    {
        if(c != null && string != null)
        {
            try
            {
                return Enum.valueOf(c, string.trim().toUpperCase());
            }
            catch(IllegalArgumentException ex) { }
        }
        return null;
    }
}