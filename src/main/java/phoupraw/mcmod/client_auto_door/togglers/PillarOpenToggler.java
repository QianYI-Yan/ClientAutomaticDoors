package phoupraw.mcmod.client_auto_door.togglers;

import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public class PillarOpenToggler extends OpenToggler {
    private final int down;
    private final int up;
    public PillarOpenToggler(Level world, BlockPos pos, BlockState state) {
        super(world, pos, state);
        BlockPos.MutableBlockPos pos1 = new BlockPos.MutableBlockPos().set(pos);
        while (true) {
            if (world.getBlockState(pos1).getBlock() == state.getBlock()) {
                pos1.move(0, -1, 0);
            } else {
                down = pos1.getY();
                break;
            }
        }
        pos1.set(pos);
        while (true) {
            if (world.getBlockState(pos1).getBlock() == state.getBlock()) {
                pos1.move(0, 1, 0);
            } else {
                up = pos1.getY();
                break;
            }
        }
    }
    @Override
    protected void readSnapshot(Boolean snapshot) {
        super.readSnapshot(snapshot);
        BlockPos.MutableBlockPos pos1 = new BlockPos.MutableBlockPos().set(pos);
        for (int i = down; i <= up; i++) {
            if (i == 0) continue;
            pos1.setY(i);
            BlockState state1 = world.getBlockState(pos1);
            if (state1.hasProperty(DoorBlock.OPEN)) {
                world.setBlock(pos1, state1.setValue(DoorBlock.OPEN, snapshot), 0);
            }
        }
    }
    @Override
    public @NotNull @UnmodifiableView Collection<BlockPos> toggle(Player player, @NotNull TransactionContext transaction) {
        super.toggle(player, transaction);
        return new ObjectArrayList<>(Iterators.transform(BlockPos.betweenClosed(pos.atY(down), pos.atY(up)).iterator(), BlockPos::immutable));
    }
}
