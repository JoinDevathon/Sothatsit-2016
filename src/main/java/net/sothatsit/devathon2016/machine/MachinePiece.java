package net.sothatsit.devathon2016.machine;

import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.blocks.Schematic;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MachinePiece {

    private PieceType type;
    private Offset[] offsets;
    private Schematic schematic;

    public MachinePiece(PieceType type, Offset[] offsets, Schematic schematic) {
        this.type = type;
        this.offsets = offsets;
        this.schematic = schematic;
    }

    public PieceType getType() {
        return this.type;
    }

    public Offset getOffset(int index) {
        return this.offsets[index];
    }

    public int getOffsetCount() {
        return this.offsets.length;
    }

    public Schematic getSchematic() {
        return this.schematic;
    }

    public void saveTo(ConfigurationSection section) {
        List<String> offsetStrings = new ArrayList<>();

        for(Offset offset : this.offsets) {
            offsetStrings.add(offset.toString());
        }

        section.set("type", this.type.getName());
        section.set("offsets", offsetStrings);

        this.schematic.saveTo(section);
    }

    public static MachinePiece loadFrom(ConfigurationSection section, Logger logger) {
        if(!section.isSet("type") || !section.isSet("offsets")
                || !section.isString("type") || !section.isList("offsets")) {
            logger.info("Type or Offsets unset at \"" + section.getCurrentPath() + "\"");
            return null;
        }

        PieceType type = PieceType.fromName(section.getString("type"));

        if(type == null) {
            logger.info("Unable to load piece type from \"" + section.getCurrentPath() + "\"");
            return null;
        }

        List<String> offsetStrings = section.getStringList("offsets");
        Offset[] offsets = new Offset[offsetStrings.size()];

        for(int index = 0; index < offsets.length; index++) {
            String offsetString = offsetStrings.get(index);
            Offset offset = Offset.fromString(offsetString);

            if(offset == null) {
                logger.info("Unable to load offset from \"" + section.getCurrentPath() + "\"");
                return null;
            }

            offsets[index] = offset;
        }

        Schematic schematic = Schematic.loadFrom(section, logger);

        if(schematic == null) {
            return null;
        }

        return new MachinePiece(type, offsets, schematic);
    }

}
