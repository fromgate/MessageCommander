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

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import org.bukkit.Bukkit;

public class MCHandler extends StreamHandler {
	
    @Override
    public void publish(LogRecord record) {
    	MCLogEvent event = new MCLogEvent (record.getMessage());
    	Bukkit.getPluginManager().callEvent(event);
    	super.publish(record);
    }
    
    @Override
    public void flush() {
        super.flush();
    }
 
    @Override
    public void close() throws SecurityException {
        super.close();
    }

}
