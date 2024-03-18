package com.ashkiano.autoweeklyrewards;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;

public class AutoWeeklyRewards extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        Metrics metrics = new Metrics(this, 21253);
        this.getLogger().info("Thank you for using the AutoWeeklyRewards plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                String playerName = event.getPlayer().getName();
                Calendar now = Calendar.getInstance();
                int currentWeek = now.get(Calendar.WEEK_OF_YEAR);
                int lastClaimedWeek = getConfig().getInt("rewards." + playerName + ".week", -1);

                if (currentWeek != lastClaimedWeek) {
                    String rewardCommand = getConfig().getString("reward-command").replace("%player%", playerName);
                    getServer().dispatchCommand(getServer().getConsoleSender(), rewardCommand);
                    event.getPlayer().sendMessage("You have claimed your weekly reward.");

                    getConfig().set("rewards." + playerName + ".week", currentWeek);
                    saveConfig();
                }
            }
        }.runTaskLater(this, 1200L);
    }
}
