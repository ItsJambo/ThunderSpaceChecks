package com.destroyer.thunderspacechecks.utils;

import com.destroyer.thunderspacechecks.ThunderSpaceChecks;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class CheckUtils {

    private static final HashMap<UUID, BossBar> checkedPlayers = new HashMap<>();
    private static final HashMap<UUID, Long> checkStartTimes = new HashMap<>();
    private static final HashMap<UUID, UUID> moderatorCheck = new HashMap<>();

    public static void startCheck(ThunderSpaceChecks plugin, Player moderator, Player target) {
        BossBar bossBar = Bukkit.createBossBar(
                GradientParser.parse(plugin.getConfig().getString("bossbar.title", "&cTime left: 5:00")),
                BarColor.RED,
                BarStyle.SOLID
        );
        bossBar.addPlayer(target);
        bossBar.setProgress(1.0);
        checkedPlayers.put(target.getUniqueId(), bossBar);
        checkStartTimes.put(target.getUniqueId(), System.currentTimeMillis());
        moderatorCheck.put(moderator.getUniqueId(), target.getUniqueId());

        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 300, 1));
        target.setGameMode(GameMode.ADVENTURE);
        target.sendTitle(
                GradientParser.parse(plugin.getConfig().getString("screen.title", "&cCHECK")),
                GradientParser.parse(plugin.getConfig().getString("screen.subtitle", "&6Check the instructions in the chat.")),
                10, 100, 10
        );
        target.sendMessage(GradientParser.parse(plugin.getConfig().getString("chat.message", "&cCHECK\n&6You have been called for a cheat check...")));

        Bukkit.broadcast(
                GradientParser.parse(plugin.getConfig().getString("moderator.startMessage", "&e[MOD] Player &c%player% has been started to be checked.").replace("%player%", target.getName())),
                "thunderspacechecks.check"
        );

        new Thread(() -> {
            try {
                Thread.sleep(300000); // Wait 5 minutes
                if (checkedPlayers.containsKey(target.getUniqueId())) {
                    Bukkit.getScheduler().runTask(plugin, () -> autoBan(plugin, target));
                }
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    public static void endCheck(ThunderSpaceChecks plugin, Player moderator, Player target) {
        BossBar bossBar = checkedPlayers.remove(target.getUniqueId());
        if (bossBar != null) bossBar.removeAll();
        checkStartTimes.remove(target.getUniqueId());
        moderatorCheck.remove(moderator.getUniqueId());

        target.removePotionEffect(PotionEffectType.BLINDNESS);
        target.setGameMode(GameMode.SURVIVAL);
        target.sendMessage(GradientParser.parse(plugin.getConfig().getString("chat.finishMessage", "&aYou have passed the check!")));

        moderator.sendMessage(GradientParser.parse(plugin.getConfig().getString("moderator.finishMessage", "&aYou have finished checking &c%player%.").replace("%player%", target.getName())));
    }

    public static void autoBan(ThunderSpaceChecks plugin, Player target) {
        checkedPlayers.remove(target.getUniqueId());
        checkStartTimes.remove(target.getUniqueId());
        target.kickPlayer(GradientParser.parse(plugin.getConfig().getString("ban.message", "&cYou did not respond to the check. You are banned for 30 days.")));
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                plugin.getConfig().getString("ban.command", "ban %player% 30d Did not provide Discord.").replace("%player%", target.getName())
        );
    }

    public static boolean isChecked(Player player) {
        return checkedPlayers.containsKey(player.getUniqueId());
    }

    public static Player getTargetBeingChecked(Player moderator) {
        UUID targetId = moderatorCheck.get(moderator.getUniqueId());
        return targetId != null ? Bukkit.getPlayer(targetId) : null;
    }

    public static void handleChatDuringCheck(ThunderSpaceChecks plugin, Player player, String message) {
        UUID moderatorId = null;
        for (UUID modId : moderatorCheck.keySet()) {
            if (moderatorCheck.get(modId).equals(player.getUniqueId())) {
                moderatorId = modId;
                break;
            }
        }
        if (moderatorId == null) return;

        Player moderator = Bukkit.getPlayer(moderatorId);
        if (moderator != null) {
            String formattedMessage = GradientParser.parse("&cCHECK | &b(" + player.getName() + "): &r" + message);
            player.sendMessage(formattedMessage);
            moderator.sendMessage(formattedMessage);
        }
    }

    public static boolean isChecking(Player moderator) {
        return moderatorCheck.containsKey(moderator.getUniqueId());
    }
}