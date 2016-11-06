package net.sothatsit.devathon2016;

import net.sothatsit.devathon2016.machine.MachineManager;
import net.sothatsit.devathon2016.parts.PartManager;
import net.sothatsit.devathon2016.parts.PartsCommand;
import net.sothatsit.devathon2016.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleMachines extends JavaPlugin {

    @Override
    public void onEnable() {
        SelectionManager selectionManager = new SelectionManager();
        PartManager partManager = new PartManager(this);
        MachineManager machineManager = new MachineManager(this, partManager);

        partManager.reloadParts();

        Bukkit.getPluginManager().registerEvents(selectionManager, this);
        Bukkit.getPluginManager().registerEvents(machineManager, this);

        PartsCommand partsCommand = new PartsCommand(this, selectionManager, partManager);

        this.getCommand("bmselect").setExecutor(selectionManager);
        this.getCommand("bmparts").setExecutor(partsCommand);
    }

    @Override
    public void onDisable() {

    }

}