package net.sothatsit.devathon2016.machine;

import net.sothatsit.devathon2016.model.AnimatedModel;
import net.sothatsit.devathon2016.model.MovingModel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Random;

public class Machine {

    private static final Random random = new Random();

    private Player player;

    private Location location;
    private ArmorStand seat;

    private MovingModel leftLeg;
    private MovingModel rightLeg;
    private MovingModel body;
    private AnimatedModel leftArm;
    private AnimatedModel rightArm;

    private float yaw;

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

    public void setYaw(float yaw) {
        this.yaw = yaw % 360;

        this.leftLeg.setYaw(this.yaw);
        this.rightLeg.setYaw(this.yaw);
        this.body.setYaw(this.yaw);
        this.leftArm.setYaw(this.yaw);
        this.rightArm.setYaw(this.yaw);

        Location seatLocation = this.seat.getLocation();

        seatLocation.setYaw(this.yaw);

        this.seat.teleport(seatLocation);
    }

    public void tick() {
        if(this.seat.getPassenger() != this.player) {
            this.remove();
            return;
        }

        this.leftLeg.tick();
        this.rightLeg.tick();
        this.body.tick();
        this.leftArm.tick();
        this.rightArm.tick();

        this.setYaw(this.yaw + 5f);
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
