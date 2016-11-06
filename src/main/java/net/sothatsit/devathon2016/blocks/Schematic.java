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

    public Schematic() {
        this(new ArrayList<>());
    }

    public Schematic(List<Pair<Offset, BlockData>> blocks) {
        this.blocks = blocks;
    }

    public List<Pair<Offset, BlockData>> getBlocks() {
        return this.blocks;
    }

    public void addBlock(Offset offset, BlockData blockData) {
        if(getBlock(offset) != null) {
            throw new IllegalArgumentException("There already exists a block with that offset");
        }

        this.blocks.add(new Pair<>(offset, blockData));
    }

    public BlockData getBlock(Offset offset) {
        for(Pair<Offset, BlockData> pair : this.blocks) {
            if(pair.getFirst().equals(offset)) {
                return pair.getSecond();
            }
        }
        return null;
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

            blockSection.set("offset", offset.toString());
            blockData.saveTo(blockSection);
        }
    }

    public static Schematic loadFrom(ConfigurationSection section, Logger logger) {
        Schematic schematic = new Schematic();

        for(String key : section.getKeys(false)) {
            if(!section.isConfigurationSection(key)) {
                continue;
            }

            ConfigurationSection blockSection = section.getConfigurationSection(key);

            if(!blockSection.isSet("offset") || !blockSection.isString("offset")) {
                logger.severe("Offset unset at \"" + blockSection.getCurrentPath() + "\"");
                return null;
            }

            Offset offset = Offset.fromString(blockSection.getString("offset"));
            BlockData blockData = BlockData.loadFrom(blockSection);

            if(offset == null) {
                logger.severe("Unable to load offset from \"" + blockSection.getCurrentPath() + "\"");
                return null;
            }

            if(blockData == null) {
                logger.severe("Unable to load block data from \"" + blockSection.getCurrentPath() + "\"");
                return null;
            }

            schematic.addBlock(offset, blockData);
        }

        return schematic;
    }

}
