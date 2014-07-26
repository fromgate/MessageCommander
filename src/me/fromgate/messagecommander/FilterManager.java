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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import me.fromgate.messagecommander.Filter.Source;
import me.fromgate.messagecommander.Filter.Type;

public class FilterManager {

	private static Map<String,Filter> filters;

	public static void init(){
		filters = new HashMap<String,Filter>();
		load();
		if (filters.isEmpty()){
			filters.put("test", new Filter (Filter.Type.EQUAL, Source.ALL, "help me", "say %player% need help!", false, "3s"));
			save();
		}

	}

	public static void load(){
		filters.clear();
		File dir = new File (MessageCommander.getPlugin().getDataFolder()+File.separator+"rules");
		if (!dir.exists()) {
			dir.mkdirs();
			return;
		}

		for (File f : dir.listFiles()){
			if (!f.getName().endsWith(".yml")) continue;
			YamlConfiguration cfg = new YamlConfiguration();
			try {
				cfg.load(f);
			} catch (Exception e) {	
				e.printStackTrace();
				return;
			}
			for (String key : cfg.getKeys(false)){
				String typeStr = cfg.getString(key+".type","EQUAL");
				Filter.Type type = Filter.Type.getByName(typeStr);
				String sourceStr= cfg.getString(key+".source","ALL");
				Filter.Source source = Filter.Source.getByName(sourceStr);
				String messageMask = cfg.getString(key+".message-mask","undefined mask");
				String replaceMask = cfg.getString(key+".command-line","");
				String cooldownTime = cfg.getString(key+".cooldown-time","");
				boolean consoleCommand = cfg.getBoolean(key+".execute-as-console",false);
				Filter filter = new Filter (type, source, f.getName().replace(".yml", ""), messageMask, replaceMask, consoleCommand, cooldownTime);
				filters.put(key, filter);
			}
		}
	}


	public static void save(){
		Set<String> fileNames = new HashSet<String> ();
		for (Filter filter : filters.values())
			fileNames.add(filter.file);
		File dir = new File (MessageCommander.getPlugin().getDataFolder()+File.separator+"rules");
		if (!dir.exists()) dir.mkdirs();

		for (File f : dir.listFiles())
			if (f.getName().endsWith(".yml")) f.delete(); 

		for (String fileName : fileNames){
			File f = new File (MessageCommander.getPlugin().getDataFolder()+File.separator+"rules"+File.separator+fileName+".yml");
			YamlConfiguration cfg = new YamlConfiguration();
			for (String key: filters.keySet()){
				Filter filter= filters.get(key);	
				if (!filter.file.equalsIgnoreCase(fileName)) continue;
				cfg.set(key+".type", filter.type.name());
				cfg.set(key+".source",filter.source.name());
				cfg.set(key+".message-mask", filter.messageMask);
				cfg.set(key+".command-line", filter.commandToExecute);
				cfg.set(key+".execute-as-console",filter.commandAsConsole);
				cfg.set(key+".cooldown-time", filter.cooldownTime);
			}
			try {
				cfg.save(f);
			}  catch (Exception e){
				e.printStackTrace();
			}
		}
	}


	public static void processMessage (CommandSender player, Source source, String message){
		for (String key : filters.keySet()){
			Filter filter = filters.get(key);
			filter.processMessage(player, source, key, message);
		}
		CommandQueue.execute();

	}

	public static boolean isExist(String id) {
		return filters.containsKey(id);
	}

	public static boolean addFilter(String id, String fileName) {
		if (filters.containsKey(id)) return false;
		Filter filter = new Filter (fileName);
		filters.put(id, filter);
		save();
		return true;
	}

	public static boolean setType(String id, String value) {
		if (!Type.isValid(value)) return false;
		Type type = Type.getByName(value);
		filters.get(id).type = type;
		save();
		return true;
	}

	public static boolean setGroup(String id, String value) {
		if (value.isEmpty()) return false;
		filters.get(id).file = value;
		save();
		return true;
	}

	public static boolean setInputMask(String id, String value) {
		filters.get(id).messageMask = value.isEmpty() ? "default message" : value; 
		save();
		return true;
	}

	public static boolean setOutputMask(String id, String value) {
		filters.get(id).commandToExecute = value;
		save();
		return true;
	}

	public static boolean setCooldown(String id, String value) {
		if (MessageCommander.getPlugin().parseTime(value)==0) filters.get(id).cooldownTime = ""; 
		else filters.get(id).cooldownTime = value;
		save();
		return true;
	}

	public static void removeFilter(String id) {
		if (!filters.containsKey(id)) return; 
		filters.remove(id);
		save();
	}

	public static List<String> getList(String mask){
		List<String> list = new ArrayList<String>();
		for (String key : filters.keySet()){
			if (mask.isEmpty()||(!mask.isEmpty()&&key.contains(mask))) 
				list.add("&6"+key+"&a : &e"+filters.get(key).toString());
		}
		return list;
	}


	public static String[] getInfo (String id){
		if (!isExist(id)) return null;
		String[] ln = filters.get(id).getInfo();
		ln[0] = id;
		return ln;
	}

}
