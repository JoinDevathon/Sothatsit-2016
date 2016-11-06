package net.sothatsit.devathon2016.model;

import net.sothatsit.devathon2016.blocks.Offset;

public class AnimatedModel {

    private Model model;

    private Offset[] offsets;
    private int offsetIndex;

    private Offset lastOffset;
    private Offset offset;

    private int ticksPerOffset;
    private int ticksSpent;

    public AnimatedModel(Model model, Offset[] offsets, int ticksPerOffset) {
        if(offsets.length == 0) {
            throw new IllegalArgumentException("Offsets cannot be empty");
        }

        this.model = model;

        this.offsets = offsets;
        this.offsetIndex = offsets.length;

        this.lastOffset = offsets[0];
        this.offset = offsets[0];

        this.ticksPerOffset = ticksPerOffset;
        this.ticksSpent = 0;
    }

    public void animate() {
        this.offsetIndex = 0;
        this.ticksSpent = 0;
    }

    public void tick() {
        if(this.offsetIndex >= this.offsets.length) {
            return;
        }

        this.ticksSpent++;

        if(this.ticksSpent >= this.ticksPerOffset) {
            this.offsetIndex++;

            if(this.offsetIndex < this.offsets.length) {
                this.lastOffset = this.offset;
                this.offset = this.offsets[this.offsetIndex];

                this.ticksSpent = 0;
            }
        }

        double progress = (double) this.ticksSpent / (double) this.ticksPerOffset;

        Offset offset = this.offset.subtract(this.lastOffset).multiply(progress).add(this.lastOffset);

        this.model.setOffset(offset);
    }

    public void remove() {
        this.model.remove();
    }

}
