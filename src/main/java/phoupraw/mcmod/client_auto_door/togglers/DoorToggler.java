package phoupraw.mcmod.client_auto_door.togglers;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Set;

public class DoorToggler extends OpenToggler {
    public DoorToggler(Level world, BlockPos pos, BlockState state) {
        super(world, pos, state);
    }
    @Override
    protected void readSnapshot(Boolean snapshot) {
        super.readSnapshot(snapshot);
        BlockState newState = getBlockState();
        Direction direction = newState.getValue(DoorBlock.HALF).getDirectionToOther();
        BlockPos pos1 = pos.relative(direction);
        BlockState state1 = world.getBlockState(pos1);
        if (state1.hasProperty(DoorBlock.OPEN)) {
            world.setBlock(pos1, state1.setValue(DoorBlock.OPEN, snapshot), 0);
        }
    }
    @Override
    public @NotNull @UnmodifiableView Collection<BlockPos> toggle(Player player, @NotNull TransactionContext transaction) {
        super.toggle(player, transaction);
        return Set.of(pos, pos.relative(state.getValue(DoorBlock.HALF).getDirectionToOther()));
    }
}
