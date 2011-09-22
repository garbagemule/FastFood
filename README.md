Bukkit plugin that allows server hosts to enable permissions-based pre-1.8 food handling features, i.e. instant eating/healing. The health values can be fully customized (increase or decrease health), and non-food items can even be added, so if you want to hurt your players when they try to eat arrows or swords, you can do so!

## Foods and health values (foodhealth.yml)

The `foodhealth.yml` file contains all the food items that can be instantly eaten, and how much health they heal (or damage). The names MUST be the (http://jd.bukkit.org/apidocs/org/bukkit/Material.html)[Material type names] of the items. That is, if you want a player to regain 8 health points (4 hearts) when eating a "steak", you must use the Material type name of steak, which is "cooked_beef", so in the `foodhealth.yml` file, you would simply write `cooked_beef: 8`.

## Permissions

The following permissions can be set using a permissions plugin such as PermissionsBukkit or PermissionsEx. The default values for the permissions allow only ops to change the settings, and all players will use the 1.8+ food handling, unless they get the `fastfood.instanteat` permission.

    Permission            Default     Description
    -----------------------------------------------------------------------------------------------
    fastfood.nostarve     false       With this, players won't take damage from starvation.
    fastfood.instanteat   false       Allows players to instant eat foods.
    fastfood.autoregain   true        Allows players to auto-regain health with a full food bar.
    fastfood.admin        op          Access to the FastFood commands.

Giving players `fastfood.nostarve: true`, `fastfood.instanteat: true` and `fastfood.autoregain: false` will make the food and health system work like pre-1.8, i.e. health can only be regained from eating food, and the food bar doesn't do anything.

## Commands

The following commands can be used in-game to change the health values and settings of FastFood. To use them, players must be either ops or have the `fastfood.admin` permission.

    Command                            Description
    -----------------------------------------------------------------------------------------------
    /ff gethealth <material>           See the health of a certain material.
    /ff sethealth <material> <value>   Set the health value of <material> to be <value>. The value
                                       must be an integer, but can be positive or negative.
                                       Setting the value to 0 disables the item.
    /ff settings <setting> <value>     Change the <setting> in the config-file to <value>, if the
                                       value is valid for the specific setting.

## Settings (config.yml)

Below are the FastFood settings, as found in the `config.yml` file. They can be changed in-game with the commands listed above, granted the player typing the command has the `fastfood.admin` permission.

    Setting                      Description
    -----------------------------------------------------------------------------------------------
    affect-hunger: true|false    If true, instant eating food also affects the food bar, i.e. when
                                 a player with the 'fastfood.instanteat' permission eats a food
                                 and either loses or gains health, they will also have their food
                                 bar de- or increased.
    hunger-multiplier: <value>   The value is multiplied by the health of the food eaten, so if
                                 the multiplier is 0.5 and the health value is 4, the food bar
                                 will increase by 2.

