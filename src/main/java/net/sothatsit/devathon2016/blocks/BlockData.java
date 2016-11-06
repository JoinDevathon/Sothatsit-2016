package net.sothatsit.devathon2016.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public class BlockData {

    private Material type;
    private int data;

    public BlockData(Block block) {
        this(block.getType(), block.getData());
    }

    public BlockData(Material type, int data) {
        this.type = type;
        this.data = data;
    }

    public Material getType() {
        return this.type;
    }

    public int getData() {
        return this.data;
    }

    public void applyTo(Block block) {
        block.setType(this.type);
        block.setData((byte) this.data);
    }

    public void saveTo(ConfigurationSection section) {
        section.set("type", this.type.name().toLowerCase());
        section.set("data", this.data);
    }

    public static BlockData loadFrom(ConfigurationSection section) {
        if(!section.isSet("type") || !section.isSet("data") || !section.isString("type") || !section.isInt("data")) {
            return null;
        }

        Material type = Material.valueOf(section.getString("type").toUpperCase());
        int data = section.getInt("data");

        return new BlockData(type, data);
    }

}
