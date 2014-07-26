package me.fromgate.messagecommander;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

public class CommandQueue {
	
	
	private static Set<CommandLine> commands = new HashSet<CommandLine>(); 
	public static void add (CommandSender sender, String commandLine){
		commands.add(new CommandLine (sender,commandLine));
	}
	
	public static void execute(){
		for (CommandLine cl : commands)
			cl.execute();
		commands.clear();
	}
	
	
	
	

	

}
