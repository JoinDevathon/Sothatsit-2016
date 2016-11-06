package net.sothatsit.devathon2016.machine;

public enum PieceType {

    LEFT_LEG,
    RIGHT_LEG,
    BODY,
    LEFT_ARM,
    RIGHT_ARM;

    public String getName() {
        return this.name().toLowerCase().replace('_', ' ');
    }

    public static PieceType fromName(String name) {
        return PieceType.valueOf(name.toUpperCase().replace(' ', '_'));
    }

}
