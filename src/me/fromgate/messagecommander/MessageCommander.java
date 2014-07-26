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

import me.fromgate.messagecommander.loglistener.MCLogMan;

import org.bukkit.plugin.java.JavaPlugin;

public class MessageCommander extends JavaPlugin {

	boolean checkUpdates;
	boolean connected;
	boolean commandChat;
	boolean commandConsole;
	boolean commandPlugins;
	boolean commandLog;

	private static MessageCommander instance;
	public static MessageCommander getPlugin(){
		return instance;
	}

	@Override
	public void onEnable() {
		reloadCfg();
		instance = this;
		if (commandConsole||commandChat) getServer().getPluginManager().registerEvents(new MCListener(), this);
		PLListener.initProtocolLib();
		if (commandPlugins) PLListener.initPacketListener();
		if (!PLListener.isConnected()) getLogger().info("ProtocolLib not found");
		FilterManager.init();
		if (commandLog) MCLogMan.init();
		UpdateChecker.init(this, "MessageCommander", "83172", "messagecomander", checkUpdates);
	}

	private void reloadCfg(){
		checkUpdates = getConfig().getBoolean("original.check-updates",true);
		getConfig().set("original.check-updates",checkUpdates);
		commandChat = getConfig().getBoolean("source.chat-input",false);
		getConfig().set("source.chat-input",commandChat);
		commandLog = getConfig().getBoolean("source.log",false);
		getConfig().set("source.log",commandLog);
		commandConsole = getConfig().getBoolean("source.console-input",false);
		getConfig().set("source.console-input",commandConsole);
		commandPlugins = getConfig().getBoolean("source.chat-screen",true);
		getConfig().set("source.chat-screen",commandPlugins);
		saveConfig();
	}

    public Long parseTime(String time){
        int hh = 0; // часы
        int mm = 0; // минуты
        int ss = 0; // секунды
        int tt = 0; // тики
        int ms = 0; // миллисекунды
        if (isInteger(time)){
            ss = Integer.parseInt(time);
        } else if (time.matches("^[0-5][0-9]:[0-5][0-9]$")){
            String [] ln = time.split(":");
            if (isInteger(ln[0])) mm = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) ss = Integer.parseInt(ln[1]);
        } else if (time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")){
            String [] ln = time.split(":");
            if (isInteger(ln[0])) hh = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) mm = Integer.parseInt(ln[1]);
            if (isInteger(ln[2])) ss = Integer.parseInt(ln[2]);
        } else if (time.matches("^\\d+ms")) {
        	ms = Integer.parseInt(time.replace("ms", ""));
        } else if (time.matches("^\\d+h")) {
        	hh = Integer.parseInt(time.replace("h", ""));
        } else if (time.matches("^\\d+m$")) {
        	mm = Integer.parseInt(time.replace("m", ""));
        } else if (time.matches("^\\d+s$")) {
        	ss = Integer.parseInt(time.replace("s", ""));
        } else if (time.matches("^\\d+t$")) {
        	tt = Integer.parseInt(time.replace("t", ""));
        }
        return (hh*3600000L)+(mm*60000L)+(ss*1000L)+(tt*50L)+ms;
    }
    
    public boolean isInteger (String str){
        return (str.matches("[0-9]+[0-9]*"));
    }

}
