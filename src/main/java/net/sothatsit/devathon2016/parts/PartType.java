package net.sothatsit.devathon2016.parts;

public enum PartType {

    LEFT_LEG,
    RIGHT_LEG,
    BODY,
    LEFT_ARM,
    RIGHT_ARM;

    public String getName() {
        return this.name().toLowerCase();
    }

    public static PartType fromName(String name) {
        for(PartType type : PartType.values()) {
            if(type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
