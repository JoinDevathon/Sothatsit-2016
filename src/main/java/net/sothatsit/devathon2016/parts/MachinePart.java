package net.sothatsit.devathon2016.parts;

import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.blocks.Schematic;
import net.sothatsit.devathon2016.model.Model;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MachinePart {

    private PartType type;
    private Offset[] offsets;
    private Schematic schematic;

    public MachinePart(PartType type, Offset[] offsets, Schematic schematic) {
        this.type = type;
        this.offsets = offsets;
        this.schematic = schematic;
    }

    public PartType getType() {
        return this.type;
    }

    public Offset[] getOffsets() {
        return this.offsets;
    }

    public Schematic getSchematic() {
        return this.schematic;
    }

    public Model generateModel(Location anchorPoint) {
        Offset offset = (this.offsets.length == 0 ? new Offset(0, 0, 0) : this.offsets[0]);

        return Model.fromSchematic(this.schematic, anchorPoint, offset);
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

    public static MachinePart loadFrom(ConfigurationSection section, Logger logger) {
        if(!section.isSet("type") || !section.isSet("offsets")
                || !section.isString("type") || !section.isList("offsets")) {
            logger.severe("Type or Offsets unset at \"" + section.getCurrentPath() + "\"");
            return null;
        }

        PartType type = PartType.fromName(section.getString("type"));

        if(type == null) {
            logger.severe("Unable to load piece type from \"" + section.getCurrentPath() + "\"");
            return null;
        }

        List<String> offsetStrings = section.getStringList("offsets");
        Offset[] offsets = new Offset[offsetStrings.size()];

        for(int index = 0; index < offsets.length; index++) {
            String offsetString = offsetStrings.get(index);
            Offset offset = Offset.fromString(offsetString);

            if(offset == null) {
                logger.severe("Unable to load offset from \"" + section.getCurrentPath() + "\"");
                return null;
            }

            offsets[index] = offset;
        }

        Schematic schematic = Schematic.loadFrom(section, logger);

        if(schematic == null) {
            return null;
        }

        return new MachinePart(type, offsets, schematic);
    }

}
