package me.kafein.bukkit.listener;

import me.kafein.bukkit.tag.BukkitTagController;
import me.kafein.common.SuperCombat;
import me.kafein.common.config.ConfigKeys;
import me.kafein.common.tag.Tag;
import me.kafein.common.tag.untag.UntagReason;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;
import java.util.UUID;

public class BukkitDeathListener implements Listener {

    private final SuperCombat plugin = SuperCombat.getInstance();

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        if (player.hasMetadata("NPC")) return;
        UUID uuid = player.getUniqueId();

        Optional<Tag> optionalTag = plugin.getTagManager().getTag(uuid);
        if (!optionalTag.isPresent()) return;
        Tag tag = optionalTag.get();

        if (ConfigKeys.Settings.DEATH_UNTAGGING_ENEMY.getValue()) plugin.getTagManager().unTagPlayer(tag.getOtherUserUUID(), UntagReason.ENEMY_DEATH);
        if (ConfigKeys.Settings.DEATH_UNTAGGING_SELF.getValue()) plugin.getTagManager().unTagPlayer(uuid, UntagReason.SELF_DEATH);

    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent event) {

        Player attacker = event.getEntity().getKiller();
        if (attacker == null) return;
        if (!BukkitTagController.isPlayer(attacker)) return;

        if (ConfigKeys.Settings.DEATH_UNTAGGING_ENEMY.getValue()) plugin.getTagManager().unTagPlayer(attacker.getUniqueId(), UntagReason.ENEMY_DEATH);

    }

}
