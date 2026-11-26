package phoupraw.mcmod.client_auto_door.togglers;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import phoupraw.mcmod.client_auto_door.events.BlockShapeToggler;
import phoupraw.mcmod.util.MCUtils;

import java.util.Collection;
import java.util.Set;

public class OpenToggler extends SnapshotParticipant<Boolean> implements BlockShapeToggler {
    protected final Level world;
    protected final BlockPos pos;
    protected final BlockState state;
    public OpenToggler(Level world, BlockPos pos, BlockState state) {
        this.world = world;
        this.pos = pos;
        this.state = state;
    }
    @Override
    protected Boolean createSnapshot() {
        return getBlockState().getValue(DoorBlock.OPEN);
    }
    @Override
    protected void readSnapshot(Boolean snapshot) {
        world.setBlock(pos, getBlockState().setValue(DoorBlock.OPEN, snapshot), 0);
    }
    protected BlockState getBlockState() {
        return world.getBlockState(pos);
    }
    @Override
    public @NotNull @UnmodifiableView Collection<BlockPos> toggle(Player player, @NotNull TransactionContext transaction) {
        updateSnapshots(transaction);
        readSnapshot(!createSnapshot());
        return Set.of(pos);
    }
    @Override
    protected void onFinalCommit() {
        super.onFinalCommit();
        BlockState newState = getBlockState();
        if (state.getValue(DoorBlock.OPEN) == newState.getValue(DoorBlock.OPEN)) {
            return;
        }
        readSnapshot(state.getValue(DoorBlock.OPEN));
        var interactor = Minecraft.getInstance().gameMode;
        if (interactor == null) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        interactor.useItemOn(player, InteractionHand.MAIN_HAND, MCUtils.getHitResult(world, pos, state, player));
    }
}
