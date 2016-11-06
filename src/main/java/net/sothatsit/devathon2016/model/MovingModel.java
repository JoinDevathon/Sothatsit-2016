package net.sothatsit.devathon2016.model;

import net.sothatsit.devathon2016.blocks.Offset;

public class MovingModel {

    private Model model;

    private Offset[] offsets;
    private int offsetIndex;

    private Offset lastOffset;
    private Offset offset;

    private int ticksPerOffset;
    private int ticksSpent;

    public MovingModel(Model model, Offset[] offsets, int ticksPerOffset) {
        if(offsets.length == 0) {
            throw new IllegalArgumentException("Offsets cannot be empty");
        }

        this.model = model;

        this.offsets = offsets;
        this.offsetIndex = (offsets.length > 1 ? 1 : 0);

        this.lastOffset = offsets[0];
        this.offset = offsets[this.offsetIndex];

        this.ticksPerOffset = ticksPerOffset;
        this.ticksSpent = 0;
    }

    public void tick() {
        this.ticksSpent++;

        if(this.ticksSpent >= this.ticksPerOffset) {
            this.offsetIndex++;
            this.offsetIndex %= this.offsets.length;

            this.lastOffset = this.offset;
            this.offset = this.offsets[this.offsetIndex];

            this.ticksSpent = 0;
        }

        double progress = (double) this.ticksSpent / (double) this.ticksPerOffset;

        Offset offset = this.offset.subtract(this.lastOffset).multiply(progress).add(this.lastOffset);

        this.model.setOffset(offset);
    }

    public void setYaw(float yaw) {
        this.model.setYaw(yaw);
    }

    public void remove() {
        this.model.remove();
    }

}
