package net.sothatsit.devathon2016.blocks;

import net.sothatsit.devathon2016.util.Pair;

import java.util.List;

public class Schematic {

    private List<Pair<Offset, BlockData>> blocks;

    public Schematic(List<Pair<Offset, BlockData>> blocks) {
        this.blocks = blocks;
    }

    public void place(/* Location */) {
        for(Pair<Offset, BlockData> pair : this.blocks) {

        }
    }

    public static Schematic loadFrom() {
        return null;
    }

}
