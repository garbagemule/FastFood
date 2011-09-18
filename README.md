
## FastFood

Bukkit plugin that allows server hosts to enable permissions-based pre-1.8 food handling features, i.e. instant eating/healing. The health values can be fully customized (increase or decrease health), and non-food items can even be added, so if you want to hurt your players when they try to eat arrows or swords, you can do so!

## Permissions

The following permissions can be set using a permissions plugin such as PermissionsBukkit or PermissionsEx. The default values for the permissions allow only ops to change the settings, and all players will use the 1.8+ food handling, unless they get the `fastfood.instanteat` permission.

    Permission            Default     Description
    -----------------------------------------------------------------------------------------------
    fastfood.instanteat   false       Allows players to instant eat foods.
    fastfood.autoregain   true        Allows players to auto-regain health with a full food bar.
    fastfood.admin        op          Access to the FastFood commands.

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