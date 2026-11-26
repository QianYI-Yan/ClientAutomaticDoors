package phoupraw.mcmod.client_auto_door.events;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface BlockShapeToggler {
    @UnmodifiableView
    @NotNull Collection<BlockPos> toggle(Player player, @NotNull TransactionContext transaction);
    Lookup LOOKUP = new Lookup();

    @ApiStatus.Internal
    final class Lookup {
        private final List<Fallback> fallbacks = new ArrayList<>();

        @FunctionalInterface
        public interface Fallback {
            @Nullable BlockShapeToggler find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Entity context);
        }

        public void registerFallback(Fallback fallback) {
            fallbacks.add(fallback);
        }

        public @Nullable BlockShapeToggler find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Entity context) {
            for (Fallback fallback : fallbacks) {
                BlockShapeToggler toggler = fallback.find(world, pos, state, blockEntity, context);
                if (toggler != null) return toggler;
            }
            return null;
        }
    }
}
