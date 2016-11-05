package net.sothatsit.devathon2016.blocks;

import net.sothatsit.devathon2016.Pair;

import java.util.List;

public class Schematic {

    private Offset anchorPoint;
    private List<Pair<Offset, BlockData>> blocks;

    public Schematic(Offset anchorPoint, List<Pair<Offset, BlockData>> blocks) {
        this.anchorPoint = anchorPoint;
        this.blocks = blocks;
    }

    public static Schematic loadFrom() {
        return null;
    }

}
