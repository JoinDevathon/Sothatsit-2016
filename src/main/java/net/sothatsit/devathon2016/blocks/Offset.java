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

    public void saveTo(ConfigurationSection section) {
        section.set("x", this.x);
        section.set("y", this.y);
        section.set("z", this.z);
    }

    public static Offset loadFrom(ConfigurationSection section) {
        if(!section.isSet("x") || !section.isSet("y") || !section.isSet("z")
                || !section.isInt("x") || !section.isInt("y") || !section.isInt("z")) {
            return null;
        }

        int x = section.getInt("x");
        int y = section.getInt("y");
        int z = section.getInt("z");

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
