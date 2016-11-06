package net.sothatsit.devathon2016.machine;

import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.model.AnimatedModel;
import net.sothatsit.devathon2016.model.Model;
import net.sothatsit.devathon2016.model.MovingModel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class Machine {

    private static final Random random = new Random();

    private static final float NO_TURN_WINDOW = 20f;
    private static final float TURN_SPEED = 4f;
    private static final double MOVEMENT_SPEED = 1d / 20d;

    private static final Offset LEFT_GUN_OFFSET = new Offset(-2.5, 3.5, -3).multiply(Model.ARMOUR_STAND_BLOCK_SIZE);
    private static final Offset RIGHT_GUN_OFFSET = new Offset(2.5, 3.5, -3).multiply(Model.ARMOUR_STAND_BLOCK_SIZE);

    private static final float ARROW_SPEED = 1f;
    private static final float ARROW_SPREAD = 12f;

    private Player player;

    private Location location;
    private ArmorStand seat;

    private MovingModel leftLeg;
    private MovingModel rightLeg;
    private MovingModel body;
    private AnimatedModel leftArm;
    private AnimatedModel rightArm;

    private float yaw;
    private int time;

    private boolean removed;

    public Machine(Player player, Location location, ArmorStand seat,
                   MovingModel leftLeg, MovingModel rightLeg, MovingModel body,
                   AnimatedModel leftArm, AnimatedModel rightArm) {
        this.player = player;

        this.location = location;
        this.seat = seat;

        this.leftLeg = leftLeg;
        this.rightLeg = rightLeg;
        this.body = body;
        this.leftArm = leftArm;
        this.rightArm = rightArm;

        this.yaw = 0f;

        this.removed = false;

        this.seat.setPassenger(player);
    }

    public void tick() {
        this.time++;

        if(this.seat.getPassenger() != this.player) {
            this.remove();
            return;
        }

        // Rotation
        float playerYaw = this.player.getEyeLocation().getYaw();
        float relativeYaw = (this.yaw - playerYaw + 180f) % 360f;

        while(relativeYaw >= 180f) {
            relativeYaw -= 360f;
        }

        if(relativeYaw < -NO_TURN_WINDOW) {
            this.yaw += TURN_SPEED;
        } else if(relativeYaw > NO_TURN_WINDOW) {
            this.yaw -= TURN_SPEED;
        }

        // Movement
        World world = this.player.getWorld();

        double cos = Math.cos(Math.toRadians(this.yaw));
        double sin = Math.sin(Math.toRadians(this.yaw));

        double dx = MOVEMENT_SPEED * sin;
        double dz = MOVEMENT_SPEED * -cos;

        double x = this.location.getX() + dx;
        double z = this.location.getZ() + dz;
        double y = world.getHighestBlockYAt((int) Math.floor(x), (int) Math.floor(z));

        y += Model.ARMOUR_STAND_BLOCK_SIZE * 3;

        this.location = new Location(this.player.getWorld(), x, y, z, this.yaw, 0f);

        // Shooting
        if(this.time % 10 == 0) {
            Vector direction = new Vector(sin, 0, -cos);

            Offset offset = LEFT_GUN_OFFSET.rotate(cos, sin);
            Location loc = offset.addTo(this.location);

            world.spawnArrow(loc, direction, ARROW_SPEED, ARROW_SPREAD);

            this.leftArm.animate();
        } else if(this.time % 10 == 5) {
            Vector direction = new Vector(sin, 0, -cos);

            Offset offset = RIGHT_GUN_OFFSET.rotate(cos, sin);
            Location loc = offset.addTo(this.location);

            world.spawnArrow(loc, direction, ARROW_SPEED, ARROW_SPREAD);

            this.rightArm.animate();
        }

        // Update the parts
        this.leftLeg.tick();
        this.rightLeg.tick();
        this.body.tick();
        this.leftArm.tick();
        this.rightArm.tick();

        this.leftLeg.moveTo(this.location);
        this.rightLeg.moveTo(this.location);
        this.body.moveTo(this.location);
        this.leftArm.moveTo(this.location);
        this.rightArm.moveTo(this.location);

        // NMS because spigot .teleport does not work with passenger
        ((CraftArmorStand) this.seat).getHandle().setPositionRotation(x, y, z, this.yaw + 180f, 0f);
    }

    public void remove() {
        for(int i=0; i < 10; i++) {
            this.createDamageEffect();
        }

        this.seat.remove();

        this.leftLeg.remove();
        this.rightLeg.remove();
        this.body.remove();
        this.leftArm.remove();
        this.rightArm.remove();

        this.removed = true;
    }

    public void createDamageEffect() {
        World world = this.seat.getWorld();

        double x = random.nextGaussian() * 2 + this.location.getX();
        double y = random.nextGaussian() * 2 + this.location.getY();
        double z = random.nextGaussian() * 2 + this.location.getZ();

        world.createExplosion(x, y, z, 0f);
    }

    public boolean isRemoved() {
        return this.removed;
    }

}
