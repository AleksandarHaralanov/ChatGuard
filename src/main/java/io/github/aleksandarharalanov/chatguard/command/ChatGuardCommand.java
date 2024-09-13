package io.github.aleksandarharalanov.chatguard.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.*;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.getFilter;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.resetFilter;
import static io.github.aleksandarharalanov.chatguard.util.AboutUtil.about;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logInfo;

public class ChatGuardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("chatguard") ||
                command.getName().equalsIgnoreCase("cg")) {
            switch (args.length) {
                case 0:
                    helpCommand(sender);
                    break;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "about":
                            about(sender, getInstance());
                            break;
                        case "filter":
                            helpFilterCommand(sender);
                            break;
                        default:
                            helpCommand(sender);
                            break;
                    }
                    break;
                case 2:
                    if (args[0].equalsIgnoreCase("filter")) {
                        switch (args[1].toLowerCase()) {
                            case "reload":
                                reloadCommand(sender);
                                break;
                            case "toggle":
                                toggleCommand(sender);
                                break;
                            default:
                                helpFilterCommand(sender);
                                break;
                        }
                    } else {
                        helpCommand(sender);
                    }
                    break;
                case 3:
                    if (args[0].equalsIgnoreCase("filter")) {
                        switch (args[1].toLowerCase()) {
                            case "add":
                            case "remove":
                                String message = args[2].toLowerCase();
                                filterCommand(sender, message, args[1].equalsIgnoreCase("add"));
                                break;
                            default:
                                helpFilterCommand(sender);
                                break;
                        }
                    } else {
                        helpCommand(sender);
                    }
                    break;
                default:
                    if (args[0].equalsIgnoreCase("filter")) {
                        switch (args[1].toLowerCase()) {
                            case "add":
                            case "remove":
                                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).toLowerCase();
                                filterCommand(sender, message, args[1].equalsIgnoreCase("add"));
                                break;
                            default:
                                helpFilterCommand(sender);
                                break;
                        }
                    } else {
                        helpCommand(sender);
                    }
                    break;
            }
        }

        return true;
    }

    private static void helpCommand(CommandSender sender) {
        String[] messages = {
                "&bChatGuard commands:",
                "&e/cg &7- Displays this message.",
                "&e/cg about &7- See ChatGuard information.",
                "&e/cg filter &7- Manage ChatGuard filter. (Staff)"
        };

        for (String message : messages) {
            if (sender instanceof Player) {
                sender.sendMessage(translate(message));
            } else {
                logInfo(message.replaceAll("&.", ""));
            }
        }
    }

    private static void helpFilterCommand(CommandSender sender) {
        String[] messages = {
                "&bChatGuard filter commands:",
                "&e/cg filter <args...> &7- Manage ChatGuard filter.",
                "&bArguments:",
                "&e<reload> &7- Reload ChatGuard config.",
                "&e<toggle> &7- Toggle the ChatGuard filter.",
                "&e<add | remove> <message> &7- Modify filter messages."
        };

        for (String message : messages) {
            if (sender instanceof Player) {
                sender.sendMessage(translate(message));
            } else {
                logInfo(message.replaceAll("&.", ""));
            }
        }
    }
    
    private static void reloadCommand(CommandSender sender) {
        if (!hasPermission(sender, "chatguard.config", "You don't have permission to reload the ChatGuard config.")) {
            return;
        }

        getConfig().loadConfig();
        resetFilter();

        if (sender instanceof Player) {
            sender.sendMessage(translate("&aChatGuard config reloaded."));
        }
    }

    private static void toggleCommand(CommandSender sender) {
        if (!hasPermission(sender, "chatguard.config", "You don't have permission to change the ChatGuard config.")) {
            return;
        }

        boolean current = getConfig().getBoolean("chatguard.toggle", true);
        getConfig().setProperty("chatguard.toggle", !current);
        getConfig().saveConfig();
        resetFilter();

        String result;
        if (!current) {
            result = "&aON";
        } else {
            result = "&cOFF";
        }

        if (sender instanceof Player) {
            sender.sendMessage(translate(String.format("&7ChatGuard filter toggled: %s", result)));
        } else {
            logInfo(String.format("ChatGuard filter toggled: %s", result).replaceAll("&.", ""));
        }
    }

    private static void filterCommand(CommandSender sender, String message, boolean check) {
        if (!hasPermission(sender, "chatguard.config", "You don't have permission to change the ChatGuard config.")) {
            return;
        }

        if (check) {
            if (getFilter().contains(message)) {
                if (sender instanceof Player) {
                    sender.sendMessage(translate(String.format("&e%s &cis already filtered.", message)));
                } else {
                    logInfo(String.format("%s is already filtered.", message));
                }
            } else {
                getFilter().add(message);
                getConfig().setProperty("chatguard.filter", getFilter());
                getConfig().saveConfig();
                resetFilter();
                if (sender instanceof Player) {
                    sender.sendMessage(translate(String.format("&e%s &aadded to the filter.", message)));
                } else {
                    logInfo(String.format("%s added to the filter.", message));
                }
            }
        } else {
            if (!getFilter().contains(message)) {
                if (sender instanceof Player) {
                    sender.sendMessage(translate(String.format("&e%s &cisn't filtered.", message)));
                } else {
                    logInfo(String.format("%s isn't filtered.", message));
                }
            } else {
                getFilter().remove(message);
                getConfig().setProperty("chatguard.filter", getFilter());
                getConfig().saveConfig();
                resetFilter();
                if (sender instanceof Player) {
                    sender.sendMessage(translate(String.format("&e%s &aremoved from the filter.", message)));
                } else {
                    logInfo(String.format("%s removed from the filter.", message));
                }
            }
        }
    }
}
