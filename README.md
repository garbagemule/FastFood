== FastFood ==

Bukkit plugin that allows server hosts to enable permissions-based pre-1.8 food handling features, i.e. instant eating/healing. The health values can be fully customized (increase or decrease health), and non-food items can even be added, so if you want to hurt your players when they try to eat arrows or swords, you can do so!

== Permissions ==

The following permissions can be set using a permissions plugin such as PermissionsBukkit or PermissionsEx. The default values for the permissions allow only ops to change the settings, and all players will use the 1.8+ food handling, unless they get the `fastfood.instanteat` permission.

    - Permission         - Description
    fastfood.instanteat  Allows players to instant eat foods.
    fastfood.autoregain  Allows players to auto-regain health with a full food bar.
    fastfood.admin       Access to the FastFood commands.