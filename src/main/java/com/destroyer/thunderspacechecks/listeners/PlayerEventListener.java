package com.destroyer.thunderspacechecks.listeners;

import com.destroyer.thunderspacechecks.ThunderSpaceChecks;
import com.destroyer.thunderspacechecks.utils.CheckUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;

public class PlayerEventListener implements Listener {

    private final ThunderSpaceChecks plugin;

    public PlayerEventListener(ThunderSpaceChecks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (CheckUtils.isChecked(player)) {
            CheckUtils.autoBan(plugin, player);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (CheckUtils.isChecked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (CheckUtils.isChecked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (CheckUtils.isChecked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (CheckUtils.isChecked(player)) {
            event.setCancelled(true);
            CheckUtils.handleChatDuringCheck(plugin, player, event.getMessage());
        }
    }
}