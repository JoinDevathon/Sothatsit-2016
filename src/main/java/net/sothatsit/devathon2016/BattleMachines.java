package net.sothatsit.devathon2016;

import net.sothatsit.devathon2016.selection.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleMachines extends JavaPlugin {

    private SelectionManager selectionManager;

    @Override
    public void onEnable() {
        this.selectionManager = new SelectionManager();

        Bukkit.getPluginManager().registerEvents(this.selectionManager, this);

        this.getCommand("bmselect").setExecutor(this.selectionManager);
    }

    @Override
    public void onDisable() {

    }

}