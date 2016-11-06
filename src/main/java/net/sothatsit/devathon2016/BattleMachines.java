package net.sothatsit.devathon2016;

import net.sothatsit.devathon2016.parts.PartManager;
import net.sothatsit.devathon2016.parts.PartsCommand;
import net.sothatsit.devathon2016.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleMachines extends JavaPlugin {

    private SelectionManager selectionManager;
    private PartManager partManager;

    @Override
    public void onEnable() {
        this.selectionManager = new SelectionManager();
        this.partManager = new PartManager(this);

        this.partManager.reloadParts();

        PartsCommand partsCommand = new PartsCommand(this.selectionManager, this.partManager);

        Bukkit.getPluginManager().registerEvents(this.selectionManager, this);

        this.getCommand("bmselect").setExecutor(this.selectionManager);
        this.getCommand("bmparts").setExecutor(partsCommand);
    }

    @Override
    public void onDisable() {

    }

}