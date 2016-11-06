package net.sothatsit.devathon2016.machine;

public enum PartType {

    LEFT_LEG,
    RIGHT_LEG,
    BODY,
    LEFT_ARM,
    RIGHT_ARM;

    public String getName() {
        return this.name().toLowerCase().replace('_', ' ');
    }

    public static PartType fromName(String name) {
        return PartType.valueOf(name.toUpperCase().replace(' ', '_'));
    }

}
