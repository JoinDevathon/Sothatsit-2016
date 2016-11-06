package net.sothatsit.devathon2016.blocks;

import org.bukkit.Location;

public class Offset {

    private final double x;
    private final double y;
    private final double z;

    public Offset(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Location addTo(Location location) {
        return location.clone().add(this.x, this.y, this.z);
    }

    public Offset add(Offset other) {
        return new Offset(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Offset subtract(Offset other) {
        return new Offset(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Offset multiply(double val) {
        return new Offset(this.x * val, this.y * val, this.z * val);
    }

    public Offset add(double x, double y, double z) {
        return new Offset(this.x + x, this.y + y, this.z + z);
    }

    public Offset rotate(double angle) {
        angle = Math.toRadians(angle);

        return this.rotate(Math.cos(angle), Math.sin(angle));
    }

    public Offset rotate(double cos, double sin) {
        double x = this.x * cos - this.z * sin;
        double y = this.y;
        double z = this.z * cos + this.x * sin;

        return new Offset(x, y, z);
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

        double x, y, z;

        try {
            x = Double.valueOf(split[0]);
            y = Double.valueOf(split[1]);
            z = Double.valueOf(split[2]);
        } catch(NumberFormatException exception) {
            return null;
        }

        return new Offset(x, y, z);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.x) ^ Double.hashCode(this.y) ^ Double.hashCode(this.z);
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
