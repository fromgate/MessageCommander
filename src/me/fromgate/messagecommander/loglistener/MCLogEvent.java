package me.fromgate.messagecommander.loglistener;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCLogEvent extends Event implements Cancellable {

	private String message;
	
	public MCLogEvent (String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	                                            
	public static HandlerList getHandlerList() {
	  return handlers;                          
	}                                           

}
