package net.sothatsit.devathon2016.blocks;

public class Offset {

    private int x;
    private int y;
    private int z;

    public Offset(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public void saveTo() {}

    public static Offset loadFrom() {
        return null;
    }

}
