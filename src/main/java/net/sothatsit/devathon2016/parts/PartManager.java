package net.sothatsit.devathon2016.parts;

import net.sothatsit.devathon2016.BattleMachines;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Logger;

public class PartManager {

    private BattleMachines plugin;

    private Random random = new Random();
    private Set<MachinePart> parts = new HashSet<>();

    public PartManager(BattleMachines plugin) {
        this.plugin = plugin;
    }

    public MachinePart getRandomPart(PartType type) {
        List<MachinePart> parts = this.getParts(type);

        if(parts.size() == 0) {
            return null;
        }

        return parts.get(random.nextInt(parts.size()));
    }

    public List<MachinePart> getParts(PartType type) {
        List<MachinePart> parts = new ArrayList<>();

        for(MachinePart part : this.parts) {
            if(part.getType() == type) {
                parts.add(part);
            }
        }

        return parts;
    }

    public Set<MachinePart> getParts() {
        return this.parts;
    }

    public void addPart(MachinePart part) {
        this.parts.add(part);
    }

    public void saveParts() {
        FileConfiguration config = this.plugin.getConfig();

        for(String key : config.getKeys(false)) {
            config.set(key, null);
        }

        this.saveParts(config);

        this.plugin.saveConfig();
    }

    public void saveParts(ConfigurationSection section) {
        int partNo = 0;

        for(MachinePart part : this.parts) {
            ConfigurationSection partSection = section.createSection("part" + partNo);

            part.saveTo(partSection);

            partNo++;
        }
    }

    public void reloadParts() {
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();

        this.reloadParts(this.plugin.getConfig(), this.plugin.getLogger());
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
