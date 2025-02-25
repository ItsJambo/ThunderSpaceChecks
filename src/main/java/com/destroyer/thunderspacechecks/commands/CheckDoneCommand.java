package com.destroyer.thunderspacechecks.commands;

import com.destroyer.thunderspacechecks.ThunderSpaceChecks;
import com.destroyer.thunderspacechecks.utils.CheckUtils;
import com.destroyer.thunderspacechecks.utils.GradientParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckDoneCommand implements org.bukkit.command.CommandExecutor {

    private final ThunderSpaceChecks plugin;

    public CheckDoneCommand(ThunderSpaceChecks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(GradientParser.parse("&#FF1C39[✘] Only players can use this command!"));
            return true;
        }
        Player moderator = (Player) sender;
        if (!moderator.hasPermission("thunderspacechecks.check")) {
            moderator.sendMessage(GradientParser.parse("&#FF1C39[✘] You don't have permission to use this command!"));
            return true;
        }

        if (!CheckUtils.isChecking(moderator)) {
            moderator.sendMessage(GradientParser.parse("&#FF1C39[✘] You are not checking anyone!"));
            return true;
        }

        Player target = CheckUtils.getTargetBeingChecked(moderator);
        if (target == null) {
            moderator.sendMessage(GradientParser.parse("&#FF1C39[✘] The player has left the game!"));
            return true;
        }

        CheckUtils.endCheck(plugin, moderator, target);
        return true;
    }
}