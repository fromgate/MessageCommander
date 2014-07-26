package me.fromgate.messagecommander;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CommandLine {
	String commandLine;
	CommandSender sender;

	public CommandLine(CommandSender sender, String commandLine){
		this.sender = sender;
		this.commandLine = commandLine;
	}

	public void execute(){
		final CommandSender sender = this.sender;
		final String commandLine = this.commandLine;
		Bukkit.getScheduler().runTaskLater(MessageCommander.getPlugin(), new Runnable(){
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, commandLine);
			}
		}, 1); 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commandLine == null) ? 0 : commandLine.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandLine other = (CommandLine) obj;
		if (commandLine == null) {
			if (other.commandLine != null)
				return false;
		} else if (!commandLine.equals(other.commandLine))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}



}