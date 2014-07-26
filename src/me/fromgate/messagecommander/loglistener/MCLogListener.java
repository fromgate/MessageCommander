package me.fromgate.messagecommander.loglistener;

import me.fromgate.messagecommander.Filter.Source;
import me.fromgate.messagecommander.FilterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MCLogListener implements Listener {

	@EventHandler
	public void onLog (MCLogEvent event){
		FilterManager.processMessage(null, Source.LOG, event.getMessage());
	}

}
