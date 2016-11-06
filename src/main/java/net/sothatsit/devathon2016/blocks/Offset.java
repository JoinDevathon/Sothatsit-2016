package net.sothatsit.devathon2016.blocks;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

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

    public Location addTo(Location location) {
        return location.add(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return this.x + " " + this.y + " " + this.z;
    }

    public static Offset fromString(String string) {
        String[] split = string.split(" ");

        if(split.length != 3) {
            return null;
        }

        int x, y, z;

        try {
            x = Integer.valueOf(split[0]);
            y = Integer.valueOf(split[1]);
            z = Integer.valueOf(split[2]);
        } catch(NumberFormatException exception) {
            return null;
        }

        return new Offset(x, y, z);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.x) ^ Integer.hashCode(this.y) ^ Integer.hashCode(this.z);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Offset) {
            Offset other = (Offset) obj;

            return other.x == this.x && other.y == this.y && other.z == this.z;
        } else {
            return false;
        }
    }

}
