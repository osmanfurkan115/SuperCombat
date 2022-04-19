package me.kafein.bukkit.listener;

import me.kafein.common.SuperCombatTag;
import me.kafein.common.config.ConfigKeys;
import me.kafein.common.tag.Tag;
import me.kafein.common.tag.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.Optional;

public class DamageListener implements Listener {

    private final TagManager tagManager = SuperCombatTag.getInstance().getTagManager();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        Entity entity = e.getEntity();
        Entity damager = e.getDamager();

        if (!(entity instanceof Player) && !(damager instanceof Player)) return;
        if (!(entity instanceof Player) && !ConfigKeys.MOB_TAGGING.getKey()) return;

        tagManager.getTagMap().values().forEach(tag -> Bukkit.broadcastMessage(tag.getUserName() + " : " + tag.getDuration()));

        if (entity instanceof Player) {
            Player player = (Player) entity;
            Optional<Tag> optionalTag = tagManager.getTag(player.getUniqueId());
            Tag tag = optionalTag.orElseGet(() -> new Tag(player.getName(), player.getUniqueId()));
            tag.setDuration(ConfigKeys.TAG_DURATION.getKey());
            tag.setOtherUser(damager.getName(), damager.getUniqueId());
            tagManager.addTag(tag);
        }

        if (damager instanceof Player) {
            Player player = (Player) damager;
            Optional<Tag> optionalTag = tagManager.getTag(player.getUniqueId());
            Tag tag = optionalTag.orElseGet(() -> new Tag(player.getName(), player.getUniqueId()));
            tag.setDuration(ConfigKeys.TAG_DURATION.getKey());
            tag.setOtherUser(damager.getName(), damager.getUniqueId());
            tagManager.addTag(tag);
        }

    }

}