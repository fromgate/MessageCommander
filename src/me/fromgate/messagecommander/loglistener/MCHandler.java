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
