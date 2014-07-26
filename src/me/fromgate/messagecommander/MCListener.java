/*  
 *  MessageCommander (Minecraft bukkit plugin)
 *  (c)2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/bukkit-plugins/facechat/
 *    
 *  This file is part of FaceChat.
 *  
 *  MessageCommander is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  MessageCommander is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with MessageCommander.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

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
