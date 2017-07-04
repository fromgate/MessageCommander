# MessageCommander
_A Minecraft (bukkit) plugin_

This plugin executes a command when defined message found in chat-screen (and other source: chat-input, console-input, server log). It allows you to link commands to action performed by another plugin (when this plugin send to player a specified message).

This plugin is created according to requests at [bukkit.org](http://forums.bukkit.org/threads/messagecommander-chatlistener.293827) and [rubukkit.org](https://dev.bukkit.org/projects/message-commander)

## Download
Latest version available on BukkitDev:  
https://dev.bukkit.org/projects/message-commander


## Features
* Execute command as player or server console when required message found in chat-screen;
* Cooldown for executing the rule command;
* Regex (and other types of string comparison) supported;
* Parse commandline and use parsed words from the original message in your command to execute.

## Why I need this plugin?
* If you use a plugin and going to add new actions to it. For example plugin types a congratulation message but it not supports a reward. You can use this plugin to execute command and give money to your player.
* If you need to turn chat message to commands. For example you need to type "please teleport me home" instead of "/home" :)

## How to use MessageCommander
MessageCommander uses multiple rule definition files. It's a "YAML" fie (`*.yml`) located in "MessageCommander/rules" folder. You can edit (or create new) this files manually. Every rule based on some parameters:

* **type**— defines the method which will used to determine text in message (before display it on screen). There are five types:
  * `EQUAL` — case insensitive compare (Example: "Aaaa Bbbb" will be equal to "aaaa bbbb")
  * `CONTAINS` — find substring in input-message
  * `START` — check if message starts with provided text
  * `END` — check if message ends with provided text
  * `REGEX` — using regular expression to find matches
* **source** — you can link any rule to any source:
  * `ALL` — any source
  * `CHAT_PLAYER` — Message typed by player in chat
  * `CHAT_CONSOLE` — Message (command) typed in server console
  * `CHAT_MESSAGE` — Message received by player in chat (message could be sent by player, plugin, mod...)
  * `LOG` — message in log (by plugin). Don't forget this message will include plugin name prefix (`[MessageCommander]`, for example).
* **message-mask** — defines input mask, that will used to find matches in original message
* **command-line** — defines the command line that will executed. Use placeholder %player% to point to players name.
* **execute-as-console** — if "true" command (provided in command-line) will executed by server console.
* **cooldown-time** — time defined in format similar to time format used in ReActions plugin. Cooldown used to set up time-limit executing commands using this rule.

## Example:
```
test:
  type: EQUAL 
  source: ALL
  message-mask: help me
  command-line: say %player% need %word1%!
  execute-as-console: ture
  cooldown-time: 3s
```

## Dependencies
This plugin requires [ProtocoLib](https://www.spigotmc.org/resources/protocollib.1997/) installed on your server.

## Commands and Permissions
There's no command or permissions yet.

## Update checker
MessageCommander includes a update checker that use your server internet connection. Update checker will every hour check the dev.bukkit.org to find new released version of plugin and you can easy disable it: just set parameter "check-updates" to "false" in config.yml.
