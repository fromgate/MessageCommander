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

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Filter {
	Type type;
	Source source;
	String file;
	String messageMask;
	String commandToExecute;  // if empty = remove
	boolean commandAsConsole; // true - console
	String cooldownTime; // if empty - no cooldown Пока игнорируем

	public enum Type {
		REGEX,
		CONTAINS,
		EQUAL,
		START,
		END;

		public static Type getByName(String name){
			for (Type t : Type.values()){
				if (t.name().equalsIgnoreCase(name)) return t;
			}
			return Type.EQUAL;
		}

		public static boolean isValid (String name){
			for (Type t : Type.values()){
				if (t.name().equalsIgnoreCase(name)) return true;
			}
			return false;
		}
	}


	public enum Source {
		ALL,
		CHAT_PLAYER,
		CHAT_CONSOLE,
		CHAT_MESSAGE,
		LOG;

		public static Source getByName(String name){
			for (Source source : Source.values()){
				if (source.name().equalsIgnoreCase(name)) return source;
			}
			return Source.ALL;
		}

		public static boolean isValid (String name){
			for (Source source : Source.values()){
				if (source.name().equalsIgnoreCase(name)) return true;
			}
			return false;
		}

	}

	public Filter (Type type, Source source, String fileName, String messageMask, String commandLine, boolean commandAsConsole, String cooldownTime){
		this.type = type;
		this.messageMask = messageMask;
		this.commandToExecute = commandLine;
		this.commandAsConsole = commandAsConsole;
		this.cooldownTime = cooldownTime;
		this.file = fileName;
		this.source = source;
	}

	public Filter (Type type, Source source, String messageMask, String commandLine,boolean commandAsConsole , String cooldownTime){
		this.type = type;
		this.messageMask = messageMask;
		this.commandToExecute = commandLine;
		this.commandAsConsole = commandAsConsole;
		this.cooldownTime = cooldownTime;
		this.file = "rules";
		this.source = source;
	}

	public Filter(String fileName) {
		this (Type.EQUAL, Source.ALL, fileName, "default message", "", true, "");
	}

	public boolean filter(String message){
		switch (type){
		case CONTAINS:
			return message.toLowerCase().contains(this.messageMask.toLowerCase());
		case END:
			return message.toLowerCase().endsWith(messageMask.toLowerCase());
		case EQUAL:
			return message.equalsIgnoreCase(messageMask);
		case REGEX:
			return message.matches(messageMask);
		case START:
			return message.toLowerCase().startsWith(messageMask.toLowerCase());
		}
		return false;
	}

	public void processMessage(CommandSender sender, Source source, String id, String message) {
		Player player = (sender instanceof Player) ?  (Player) sender : null;
		if (source != this.source && this.source != Source.ALL) return;
		if (!filter(message)) return;
		if (cooldownTime.isEmpty()||player==null||!Cooldown.isCooldown(player, id, MessageCommander.getPlugin().parseTime(cooldownTime))) 
			executeCommand(sender, processPlaceholders (message));
	}


	public void executeCommand (CommandSender original, final String commandLine){
		final CommandSender sender = this.commandAsConsole ? Bukkit.getConsoleSender() : original;
		if (sender == null) return;
		CommandQueue.add(sender, replacePlayerPlaceholders(sender,commandLine));		
	}

	public String replacePlayerPlaceholders(CommandSender sender, String str){
		Player player = (sender != null && sender instanceof Player) ? (Player) sender : null; 
		String result = str;
		if (player!=null) {
			result = result.replace("%player%", player.getName());
			result = result.replace("%player_world%", player.getWorld().getName());
			result = result.replace("%player_x", Integer.toString(player.getLocation().getBlockX()));
			result = result.replace("%player_y", Integer.toString(player.getLocation().getBlockY()));
			result = result.replace("%player_z", Integer.toString(player.getLocation().getBlockZ()));
		}
		return result;
	}


	public String processPlaceholders (String message){
		if (message.isEmpty()) return message;
		String [] ln = message.split(" ");
		String newMessage = this.commandToExecute;
		for (int i = 0; i<ln.length ; i++)
			newMessage = newMessage.replace("%word"+Integer.toString(1+i)+"%", ln[i]);
		return newMessage;
	}

	@Override
	public String toString(){
		return "["+this.file+"] "+this.type.name()+" "+(this.messageMask.isEmpty() ? "N/A" : this.messageMask)+" / "+this.source.name()+
				(this.cooldownTime.isEmpty() ? "" : " ("+this.cooldownTime+")");
	}

	public  String[] getInfo(){
		String[] ln= new String [8];
		ln[1]=type.name();
		ln[2]=file;
		ln[3]=messageMask;
		ln[4]=source.name();
		ln[5]=commandToExecute.isEmpty() ? "N/A" : commandToExecute;
		ln[6]=commandAsConsole ? "CONSOLE" : "PLAYER";
		ln[7]=cooldownTime.isEmpty() ? "N/A" : cooldownTime;
		return ln;
	}





}
