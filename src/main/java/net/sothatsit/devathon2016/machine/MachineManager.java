package net.sothatsit.devathon2016.machine;

import net.sothatsit.devathon2016.BattleMachines;
import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.model.AnimatedModel;
import net.sothatsit.devathon2016.model.Model;
import net.sothatsit.devathon2016.model.MovingModel;
import net.sothatsit.devathon2016.parts.MachinePart;
import net.sothatsit.devathon2016.parts.PartManager;
import net.sothatsit.devathon2016.parts.PartType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MachineManager implements Listener, Runnable {

    private static final int MOVING_ANIMATION_SPEED = 10;
    private static final int ANIMATED_ANIMATION_SPEED = 5;

    private PartManager partManager;
    private Map<UUID, Machine> machines = new HashMap<>();

    public MachineManager(BattleMachines plugin, PartManager partManager) {
        this.partManager = partManager;

        Bukkit.getScheduler().runTaskTimer(plugin, this, 1, 1);
    }

    @Override
    public void run() {
        Iterator<Machine> iterator = this.machines.values().iterator();

        while(iterator.hasNext()) {
            Machine machine = iterator.next();

            if(machine.isRemoved()) {
                iterator.remove();
            } else {
                machine.tick();
            }
        }
    }

    public Machine getMachine(Player player) {
        return this.machines.get(player.getUniqueId());
    }

    public boolean createMachine(Player player) {
        if(this.getMachine(player) != null) {
            return false;
        }

        Location location = player.getLocation().add(0, Model.ARMOUR_STAND_BLOCK_SIZE * 3, 0);

        Location seatLocation = location.clone().subtract(0, Model.ARMOUR_STAND_BLOCK_Y_OFFSET, 0);
        ArmorStand seat = player.getWorld().spawn(seatLocation, ArmorStand.class);

        seat.setVisible(false);
        seat.setInvulnerable(true);
        seat.setMarker(true);

        MovingModel leftLeg = this.generateMovingModel(location, PartType.LEFT_LEG);
        MovingModel rightLeg = this.generateMovingModel(location, PartType.RIGHT_LEG);
        MovingModel body = this.generateMovingModel(location, PartType.BODY);
        AnimatedModel leftArm = this.generateAnimatedModel(location, PartType.LEFT_ARM);
        AnimatedModel rightArm = this.generateAnimatedModel(location, PartType.RIGHT_ARM);

        Machine machine = new Machine(player, location, seat, leftLeg, rightLeg, body, leftArm, rightArm);

        this.machines.put(player.getUniqueId(), machine);

        return true;
    }

    private MovingModel generateMovingModel(Location anchorPoint, PartType partType) {
        MachinePart part = this.partManager.getRandomPart(partType);

        Model model = part.generateModel(anchorPoint);
        Offset[] offsets = part.getOffsets();

        return new MovingModel(model, offsets, MOVING_ANIMATION_SPEED);
    }

    private AnimatedModel generateAnimatedModel(Location anchorPoint, PartType partType) {
        MachinePart part = this.partManager.getRandomPart(partType);

        Model model = part.generateModel(anchorPoint);
        Offset[] offsets = part.getOffsets();

        return new AnimatedModel(model, offsets, ANIMATED_ANIMATION_SPEED);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.createMachine(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Machine machine = this.getMachine(event.getPlayer());

        if(machine != null) {
            machine.remove();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Machine machine = this.getMachine(event.getEntity());

        if(machine != null) {
            machine.remove();
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        Machine machine = this.getMachine(player);

        if(machine != null) {
            machine.createDamageEffect();
        }
    }

}
