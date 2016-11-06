package net.sothatsit.devathon2016.machine;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MachinesCommand implements CommandExecutor {

    private MachineManager machineManager;

    public MachinesCommand(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!label.equalsIgnoreCase("bmachines")) {
            return false;
        }

        if(args.length != 0) {
            this.send(sender, "&cInvalid Arguments, valid: /bmachines");
            return true;
        }

        if(!(sender instanceof Player)) {
            this.send(sender, "&cSorry, you must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;

        if(this.machineManager.createMachine(player)) {
            this.send(sender, "&aWelcome to your new Battle Machine!");
        } else {
            this.send(sender, "&cYou are already in a Battle Machine");
        }
        return true;
    }

    private void send(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
