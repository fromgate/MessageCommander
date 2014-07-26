package me.fromgate.messagecommander;

import me.fromgate.messagecommander.Filter.Source;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class MCListener implements Listener {
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
	public void onJoin (PlayerJoinEvent event){
		UpdateChecker.updateMsg(event.getPlayer());
	}

	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
	public void onChatCommand(AsyncPlayerChatEvent event){
		if (!MessageCommander.getPlugin().commandChat) return;
		if (!event.getPlayer().hasPermission("chatcommander")) return;
		FilterManager.processMessage(event.getPlayer(), Source.CHAT_PLAYER, event.getMessage());
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onServerCommandEvent(ServerCommandEvent event) {
		if (!MessageCommander.getPlugin().commandConsole) return;
		FilterManager.processMessage(Bukkit.getConsoleSender(), Source.CHAT_CONSOLE, event.getCommand());
	}
	
}
