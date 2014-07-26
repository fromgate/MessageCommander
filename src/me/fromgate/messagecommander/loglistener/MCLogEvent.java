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
