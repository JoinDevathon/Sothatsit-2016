package net.sothatsit.devathon2016.selection;

import net.sothatsit.devathon2016.blocks.BlockData;
import net.sothatsit.devathon2016.blocks.Offset;
import net.sothatsit.devathon2016.blocks.Schematic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Selection {

    private Player player;
    private Location pos1;
    private Location pos2;

    public Selection(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getPos1() {
        return this.pos1;
    }

    public Location getPos2() {
        return this.pos2;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public boolean isComplete() {
        return this.pos1 != null && this.pos2 != null;
    }

    public int getVolume() {
        Vector dif = this.pos2.toVector().subtract(this.pos1.toVector());

        int width = (int) Math.ceil(Math.abs(dif.getX()));
        int height = (int) Math.ceil(Math.abs(dif.getY()));
        int depth = (int) Math.ceil(Math.abs(dif.getZ()));

        return width * height * depth;
    }

    public Schematic generateSchematic(Location anchorPoint) {
        if(!isComplete()) {
            throw new IllegalStateException("Cannot generate schematic from incomplete Selection");
        }

        int fromX = Math.min(this.pos1.getBlockX(), this.pos2.getBlockX()) - anchorPoint.getBlockX();
        int fromY = Math.min(this.pos1.getBlockY(), this.pos2.getBlockY()) - anchorPoint.getBlockY();
        int fromZ = Math.min(this.pos1.getBlockZ(), this.pos2.getBlockZ()) - anchorPoint.getBlockZ();

        int toX = Math.max(this.pos1.getBlockX(), this.pos2.getBlockX()) - anchorPoint.getBlockX();
        int toY = Math.max(this.pos1.getBlockY(), this.pos2.getBlockY()) - anchorPoint.getBlockY();
        int toZ = Math.max(this.pos1.getBlockZ(), this.pos2.getBlockZ()) - anchorPoint.getBlockZ();

        Schematic schematic = new Schematic();

        for(int x = fromX; x <= toX; x++) {
            for(int y = fromY; y <= toY; y++) {
                for(int z = fromZ; z <= toZ; z++) {
                    Location loc = anchorPoint.clone().add(x, y, z);
                    Block block = loc.getBlock();

                    if(block.getType() == Material.AIR) {
                        continue;
                    }

                    Offset offset = new Offset(x, y, z);
                    BlockData blockData = new BlockData(block);

                    schematic.addBlock(offset, blockData);
                }
            }
        }

        return schematic;
    }

}
