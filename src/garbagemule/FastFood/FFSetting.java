package garbagemule.FastFood;

import garbagemule.FastFood.util.Enums;

import java.util.regex.Pattern;

public enum FFSetting
{
    AFFECTHUNGER("true|false|yes|no", "affect-hunger")
    {
        public Object cast(String s)
        {
            return castBoolean(s);
        }
    },
    HUNGERMULTIPLIER("-?[0-9]+(\\.[0-9]+)?", "hunger-multiplier")
    {
        public Object cast(String s)
        {
            return castDouble(s);
        }
    };
    
    private Pattern pattern;
    private String  settingName;
    
    /**
     * Constructor
     * @param pattern The pattern to match the valid setting values.
     * @param settingName The config-name of the setting.
     */
    private FFSetting(String pattern, String settingName)
    {
        this.pattern     = Pattern.compile(pattern);
        this.settingName = settingName;
    }
    
    /**
     * Cast the input String to the correct data type.
     * @param s The input String
     * @return An Object of a specific datatype.
     */
    public abstract Object cast(String s);
    
    /**
     * Get the setting name of this FFSetting
     * @return The setting name
     */
    public String getName()
    {
        return settingName;
    }
    
    /**
     * Check if the input String is a valid setting value.
     * @param s A setting value
     * @return true, if the value is valid, false otherwise.
     */
    public boolean valid(String s)
    {
        return pattern.matcher(s).matches();
    }
    
    /**
     * Get an FFSetting of a String-representation.
     * @param s The String-representation of the FFSetting
     * @return An FFSetting, or null
     */
    public static FFSetting fromString(String s)
    {
        return Enums.getEnumFromString(FFSetting.class, s);
    }
    
    
    
    /*************************************************************************
     * 
     *          CASTS
     * 
     *************************************************************************/
    
    public static boolean castBoolean(String s)
    {
        return Boolean.parseBoolean(s);
    }
    
    public static double castDouble(String s)
    {
        return Double.parseDouble(s);
    }
}
