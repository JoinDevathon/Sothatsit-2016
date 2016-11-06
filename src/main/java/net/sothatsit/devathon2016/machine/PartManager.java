package net.sothatsit.devathon2016.machine;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class PartManager implements CommandExecutor {

    private Set<MachinePart> parts = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    public void saveParts(ConfigurationSection section) {
        int partNo = 0;

        for(MachinePart part : this.parts) {
            ConfigurationSection partSection = section.createSection("part" + partNo);

            part.saveTo(partSection);

            partNo++;
        }
    }

    public void reloadParts(ConfigurationSection section, Logger logger) {
        long start = System.nanoTime();

        logger.info("Reloading Machine Parts...");

        this.parts.clear();
        
        for(String key : section.getKeys(false)) {
            if(!section.isConfigurationSection(key)) {
                continue;
            }

            ConfigurationSection partSection = section.getConfigurationSection(key);

            MachinePart part = MachinePart.loadFrom(partSection, logger);

            if(part == null) {
                continue;
            }

            this.parts.add(part);
        }

        double time = roundTo2DP((System.nanoTime() - start) / 1e6);

        logger.info("Loaded " + this.parts.size() + " Machine Parts (" + time + "ms)");
    }

    private double roundTo2DP(double num) {
        return Math.round(num * 100d) / 100d;
    }

}
