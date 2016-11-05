package net.sothatsit.devathon2016.blocks;

import net.sothatsit.devathon2016.util.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Schematic {

    private List<Pair<Offset, BlockData>> blocks;

    public Schematic(List<Pair<Offset, BlockData>> blocks) {
        this.blocks = blocks;
    }

    public void place(Location location) {
        for(Pair<Offset, BlockData> pair : this.blocks) {
            Offset offset = pair.getFirst();

            Location loc = offset.addTo(location);
            Block block = loc.getBlock();

            block.breakNaturally();

            pair.getSecond().applyTo(block);
        }
    }

    public void remove(Location location) {
        for(Pair<Offset, BlockData> pair : this.blocks) {
            Offset offset = pair.getFirst();

            Location loc = offset.addTo(location);
            Block block = loc.getBlock();

            block.setType(Material.AIR);
        }
    }

    public void saveTo(ConfigurationSection section) {
        for(int index = 0; index < this.blocks.size(); index++) {
            Pair<Offset, BlockData> pair = this.blocks.get(index);

            Offset offset = pair.getFirst();
            BlockData blockData = pair.getSecond();

            ConfigurationSection blockSection = section.createSection("index" + index);

            offset.saveTo(blockSection);
            blockData.saveTo(blockSection);
        }
    }

    public static Schematic loadFrom(ConfigurationSection section, Logger logger) {
        List<Pair<Offset, BlockData>> blocks = new ArrayList<>();

        for(String key : section.getKeys(false)) {
            if(!section.isConfigurationSection(key)) {
                continue;
            }

            ConfigurationSection blockSection = section.getConfigurationSection(key);

            Offset offset = Offset.loadFrom(blockSection);
            BlockData blockData = BlockData.loadFrom(blockSection);

            if(offset == null || blockData == null) {
                logger.severe("Unable to load offset and/or block data from \"" + blockSection.getCurrentPath() + "\"");
                return null;
            }
        }

        if(blocks.size() == 0) {
            logger.severe("Empty schematic at \"" + section.getCurrentPath() + "\"");
            return null;
        }

        return null;
    }

}
