package net.sothatsit.devathon2016.machine;

import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.blocks.Schematic;

public class MachinePiece {

    private Offset[] offsets;
    private Schematic schematic;

    public MachinePiece(Offset[] offsets, Schematic schematic) {
        this.offsets = offsets;
        this.schematic = schematic;
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

    public static MachinePiece loadFrom() {
        return null;
    }

}
