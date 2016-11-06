package net.sothatsit.devathon2016.selection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SelectionManager implements Listener, CommandExecutor{

    private Map<UUID, Selection> playerSelections = new HashMap<>();

    public Selection getSelection(Player player) {
        return this.playerSelections.get(player.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!label.equalsIgnoreCase("bmselect")) {
            return false;
        }

        if(args.length != 1 || (!args[0].equalsIgnoreCase("1") && !args[0].equalsIgnoreCase("2"))) {
            this.send(sender, "&cInvalid Arguments, expected \"/bmselect 1\" or \"/bmselect 2\"");
            return true;
        }

        if(!(sender instanceof Player)) {
            this.send(sender, "&cSorry, you must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;
        Selection selection = this.getSelection(player);

        Block target = player.getTargetBlock((Set<Material>) null, 50);

        if(target == null || target.getType() == Material.AIR) {
            this.send(sender, "&cPlease look at a block within 50 blocks of you that you wish to select");
            return true;
        }

        if(args[0].equalsIgnoreCase("1")) {
            selection.setPos1(target.getLocation());
        } else {
            selection.setPos2(target.getLocation());
        }

        this.send(sender, "&aSet Pos" + args[0]);
        return true;
    }

    private void send(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Selection selection = new Selection(player);

        this.playerSelections.put(player.getUniqueId(), selection);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.playerSelections.remove(player.getUniqueId());
    }

}
