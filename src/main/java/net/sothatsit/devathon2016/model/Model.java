package net.sothatsit.devathon2016.model;

import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.util.Pair;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Set;

public class Model {

    private Set<Pair<Offset, ArmorStand>> armorStands;

    private Location location;
    private Offset offset;

    public Model(Location location, Offset offset, Set<Pair<Offset, ArmorStand>> armorStands) {
        this.location = location;
        this.offset = offset;
        this.armorStands = armorStands;
    }

    public void moveTo(Location location) {
        this.location = location;

        this.recalcPositions();
    }

    public void setOffset(Offset offset) {
        this.offset = offset;

        this.recalcPositions();
    }

    public void recalcPositions() {
        Location location = this.offset.addTo(this.location);

        for(Pair<Offset, ArmorStand> pair : this.armorStands) {
            Location position = pair.getFirst().addTo(location);

            pair.getSecond().teleport(position);
        }
    }

    public void destroy() {
        for(Pair<Offset, ArmorStand> pair : this.armorStands) {
            pair.getSecond().remove();
        }

        this.armorStands.clear();
    }

}
