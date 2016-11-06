package net.sothatsit.devathon2016;

import net.sothatsit.devathon2016.machine.MachineManager;
import net.sothatsit.devathon2016.machine.MachinesCommand;
import net.sothatsit.devathon2016.parts.PartManager;
import net.sothatsit.devathon2016.parts.PartsCommand;
import net.sothatsit.devathon2016.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleMachines extends JavaPlugin {

    private MachineManager machineManager;

    @Override
    public void onEnable() {
        SelectionManager selectionManager = new SelectionManager();
        PartManager partManager = new PartManager(this);
        this.machineManager = new MachineManager(this, partManager);

        partManager.reloadParts();

        Bukkit.getPluginManager().registerEvents(selectionManager, this);
        Bukkit.getPluginManager().registerEvents(this.machineManager, this);

        PartsCommand partsCommand = new PartsCommand(this, selectionManager, partManager);
        MachinesCommand machinesCommand = new MachinesCommand(this.machineManager);

        this.getCommand("bmselect").setExecutor(selectionManager);
        this.getCommand("bmparts").setExecutor(partsCommand);
        this.getCommand("bmachines").setExecutor(machinesCommand);
    }

    @Override
    public void onDisable() {
        this.machineManager.removeAll();
    }

}