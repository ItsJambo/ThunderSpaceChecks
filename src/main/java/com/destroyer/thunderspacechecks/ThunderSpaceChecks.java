package com.destroyer.thunderspacechecks;

import com.destroyer.thunderspacechecks.commands.CheckCommand;
import com.destroyer.thunderspacechecks.commands.CheckDoneCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.destroyer.thunderspacechecks.listeners.PlayerEventListener;

public final class ThunderSpaceChecks extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(this), this);
        getCommand("check").setExecutor(new CheckCommand(this));
        getCommand("checkdone").setExecutor(new CheckDoneCommand(this));
        getLogger().info("ThunderSpaceChecks is enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ThunderSpaceChecks is disabled!");
    }
}