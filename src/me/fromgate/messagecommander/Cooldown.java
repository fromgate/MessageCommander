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

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class Cooldown {
	
	
	public static boolean isCooldown (Player player, String id, long delay){
		long time = System.currentTimeMillis();
		long playerTime = player.hasMetadata("msgfltr-time-"+id) ? player.getMetadata("msgfltr-time-"+id).get(0).asLong() : 0;
		if (playerTime<=time)
			player.setMetadata("msgfltr-time-"+id, new FixedMetadataValue (MessageCommander.getPlugin(), time+delay));
		return playerTime>time;
	}
	
}
