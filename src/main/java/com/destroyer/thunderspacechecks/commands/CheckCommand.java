package com.destroyer.thunderspacechecks.commands;

import com.destroyer.thunderspacechecks.ThunderSpaceChecks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class CheckCommand implements org.bukkit.command.CommandExecutor {

    private final ThunderSpaceChecks plugin;

    public CheckCommand(ThunderSpaceChecks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }
        Player moderator = (Player) sender;
        if (!moderator.hasPermission("thunderspacechecks.check")) {
            moderator.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length != 1) {
            moderator.sendMessage(ChatColor.RED + "Usage: /check <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            moderator.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        if (moderator.equals(target)) {
            moderator.sendMessage(ChatColor.RED + "You cannot check yourself!");
            return true;
        }

        CheckUtils.startCheck(plugin, moderator, target);
        return true;
    }
}