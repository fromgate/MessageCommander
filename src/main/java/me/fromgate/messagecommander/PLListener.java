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
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PLListener {
	
	private static boolean connected =false;
	public static boolean isConnected(){
		return connected;
	}
	

	public static void initProtocolLib(){
		try{
			if (Bukkit.getPluginManager().getPlugin("ProtocolLib")!=null){
				connected = true;
			} 
		} catch (Throwable e){
			connected = false;
		}
		
	}
	
	
	private static String jsonToString (JSONObject source){
		String result = "";
		for (Object key : source.keySet()){
			Object value = source.get(key);
			if (value instanceof String){
				if ((key instanceof String)&&(!((String)key).equalsIgnoreCase("text"))) continue;
				result = result+(String) value;
			} else if (value instanceof JSONObject){
				result = result + jsonToString ((JSONObject) value);
			} else if (value instanceof JSONArray){
				result = result + jsonToString ((JSONArray) value);
			} 
		}
		return result;
	}
	
	private static String jsonToString (JSONArray source){
		String result = "";
            for (Object value : source) {
                if (value instanceof String){
                    result = result+(String) value;
                } else if (value instanceof JSONObject){
                    result = result + jsonToString ((JSONObject) value);
                } else if (value instanceof JSONArray){
                    result = result + jsonToString ((JSONArray) value);
                }
            }
		return result;
	}

	private static String jsonToString(String json){
		JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
		if (jsonObject == null||json.isEmpty()) return json;
		JSONArray array = (JSONArray) jsonObject.get("extra");
		if (array == null||array.isEmpty()) return json;
		return jsonToString (array);
	}
	
	private static String textToString(String message){
		String text = message;
		if (text.matches("^\\{\"text\":\".*\"\\}")) {
			text = text.replaceAll("^\\{\"text\":\"", "");
			text = text.replaceAll("\"\\}$", "");
		}
		return ChatColor.stripColor(text);
	}
	
	public static void initPacketListener(){
		if (!connected) return;
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(MessageCommander.getPlugin(), PacketType.Play.Server.CHAT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						String message = "";
						try {
							String jsonMessage = event.getPacket().getChatComponents().getValues().get(0).getJson();
							if (jsonMessage!=null) message = jsonToString(jsonMessage);
						} catch (Throwable ignore){
						}
						if (message.isEmpty()&&event.getPacket().getStrings().size()>0) {
							String jsonMessage = event.getPacket().getStrings().read(0);
							if (jsonMessage!=null) message = textToString(jsonMessage);	
						}
						if (message.isEmpty()) return;
						FilterManager.processMessage(event.getPlayer(), Source.CHAT_MESSAGE, message);
					}
				});
	}
	
	
}
