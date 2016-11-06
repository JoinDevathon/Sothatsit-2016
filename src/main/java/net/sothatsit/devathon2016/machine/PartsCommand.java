package net.sothatsit.devathon2016.machine;

import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.blocks.Schematic;
import net.sothatsit.devathon2016.selection.Selection;
import net.sothatsit.devathon2016.selection.SelectionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class PartsCommand implements CommandExecutor {

    private SelectionManager selectionManager;
    private PartManager partManager;

    public PartsCommand(SelectionManager selectionManager, PartManager partManager) {
        this.selectionManager = selectionManager;
        this.partManager = partManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!label.equalsIgnoreCase("bmparts")) {
            return false;
        }

        if(args.length != 1) {
            this.send(sender, "&cInvalid Arguments, valid: " + this.getValidCommand());
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            this.partManager.reloadParts();

            this.send(sender, "&aReloaded the parts config.");
            return true;
        }

        // TEST COMMAND
        if(args[0].equalsIgnoreCase("place")) {
            Random random = new Random();
            MachinePart[] parts = this.partManager.getParts().toArray(new MachinePart[0]);

            if(parts.length == 0) {
                this.send(sender, "&cThere are no parts");
                return true;
            }

            if(!(sender instanceof Player)) {
                this.send(sender, "&cSorry, you must be a player to run this command.");
                return true;
            }

            Player player = (Player) sender;

            MachinePart part = parts[random.nextInt(parts.length)];

            part.getSchematic().place(player.getLocation());

            this.send(sender, "&aPlaced a " + part.getType().getName() + " part");
            return true;
        }

        PartType type = PartType.fromName(args[0]);

        if(type == null) {
            this.send(sender, "&cInvalid part type, valid: " + this.getValidCommand());
            return true;
        }

        if(!(sender instanceof Player)) {
            this.send(sender, "&cSorry, you must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;
        Selection selection = this.selectionManager.getSelection(player);

        if(!selection.isComplete()) {
            this.send(sender, "&cYou must make a selection using /bmselect");
            return true;
        }

        Offset[] offsets = new Offset[] { new Offset(0, 0, 0) };
        Schematic schematic = selection.generateSchematic(player.getLocation());

        MachinePart part = new MachinePart(type, offsets, schematic);

        this.partManager.addPart(part);
        this.partManager.saveParts();

        this.send(sender, "&aRegistered a new " + type.getName() + " machine part!");
        return true;
    }

    private void send(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String getValidCommand() {
        StringBuilder builder = new StringBuilder("/bmparts <reload:place");

        for(PartType type : PartType.values()) {
            builder.append(':').append(type.getName());
        }

        return builder.append('>').toString();
    }

}
