package net.sothatsit.devathon2016.model;

import net.sothatsit.devathon2016.blocks.BlockData;
import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.blocks.Schematic;
import net.sothatsit.devathon2016.util.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Model {

    public static final double ARMOUR_STAND_BLOCK_SIZE = 0.625;
    public static final double ARMOUR_STAND_BLOCK_Y_OFFSET = ARMOUR_STAND_BLOCK_SIZE - 2;

    private Set<Pair<Offset, ArmorStand>> armorStands;

    private Location location;
    private Offset offset;

    private float yaw;

    public Model(Location location, Offset offset, Set<Pair<Offset, ArmorStand>> armorStands) {
        this.location = location;
        this.offset = offset.multiply(ARMOUR_STAND_BLOCK_SIZE);
        this.armorStands = armorStands;
    }

    public void moveTo(Location location) {
        this.location = location;

        this.recalcPositions();
    }

    public void setOffset(Offset offset) {
        this.offset = offset.multiply(ARMOUR_STAND_BLOCK_SIZE);

        this.recalcPositions();
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;

        this.recalcPositions();
    }

    public void recalcPositions() {
        double cos = Math.cos(Math.toRadians(this.yaw));
        double sin = Math.sin(Math.toRadians(this.yaw));

        Offset rotatedOffset = this.rotateOffset(this.offset, cos, sin);
        Location location = rotatedOffset.addTo(this.location);

        for(Pair<Offset, ArmorStand> pair : this.armorStands) {
            Offset blockOffsetRotated = this.rotateOffset(pair.getFirst(), cos, sin);
            Location position = blockOffsetRotated.addTo(location);

            position.setYaw(this.yaw + 180f);

            pair.getSecond().teleport(position);
        }
    }

    private Offset rotateOffset(Offset offset, double cos, double sin) {
        double x = offset.getX() * cos - offset.getZ() * sin;
        double y = offset.getY();
        double z = offset.getZ() * cos + offset.getX() * sin;

        return new Offset(x, y, z);
    }

    public void remove() {
        for(Pair<Offset, ArmorStand> pair : this.armorStands) {
            pair.getSecond().remove();
        }

        this.armorStands.clear();
    }

    public static Model fromSchematic(Schematic schematic, Location anchorPoint, Offset offset) {
        anchorPoint.setPitch(0f);
        anchorPoint.setYaw(0f);

        World world = anchorPoint.getWorld();
        Location offsetAnchor = offset.addTo(anchorPoint);

        Set<Pair<Offset, ArmorStand>> armorStands = new HashSet<>();

        for(Pair<Offset, BlockData> pair : schematic.getBlocks()) {
            Offset blockOffset = pair.getFirst()
                    .multiply(ARMOUR_STAND_BLOCK_SIZE)
                    .add(0, ARMOUR_STAND_BLOCK_Y_OFFSET, 0);

            BlockData blockData = pair.getSecond();

            Location loc = blockOffset.addTo(offsetAnchor);
            ItemStack block = blockData.createItemStack();

            ArmorStand armorStand = world.spawn(loc, ArmorStand.class);

            armorStand.setHelmet(block);
            armorStand.setVisible(false);
            armorStand.setGravity(false);

            armorStands.add(new Pair<>(blockOffset, armorStand));
        }

        return new Model(anchorPoint, offset, armorStands);
    }

}
