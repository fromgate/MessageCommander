package me.fromgate.messagecommander.loglistener;

import java.util.logging.Logger;

import me.fromgate.messagecommander.MessageCommander;

import org.bukkit.Bukkit;

public class MCLogMan {
	
	public static void init(){
		Logger log = Bukkit.getLogger();
		log.addHandler(new MCHandler());
		Bukkit.getPluginManager().registerEvents(new MCLogListener(), MessageCommander.getPlugin());
	}
}
