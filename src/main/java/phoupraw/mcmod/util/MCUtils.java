package phoupraw.mcmod.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import phoupraw.mcmod.client_auto_door.mixin.minecraft.APlayerEntity;

@ApiStatus.NonExtendable
public interface MCUtils {
    static @NotNull BlockHitResult getHitResult(Level world, BlockPos pos, BlockState state, Player player) {
        Vec3 centerPos = Vec3.atCenterOf(pos);
        Vec3 eyePos = player.getEyePosition();
        Vec3 end = eyePos.lerp(centerPos, 2);
        BlockHitResult hitResult = world.clipWithInteractionOverride(eyePos, end, pos, state.getInteractionShape(world, pos), state);
        if (hitResult == null) {
            hitResult = new BlockHitResult(centerPos, Direction.UP, pos, false);
        }
        return hitResult;
    }
    static float getHeight(LocalPlayer self0) {
        var self = (LocalPlayer & APlayerEntity) self0;
        Pose pose;
        if (self.invokeCanPlayerFitWithinBlocksAndEntitiesWhen(Pose.SWIMMING)) {
            Pose pose0;
            if (self.isFallFlying()) {
                pose0 = Pose.FALL_FLYING;
            } else if (self.isSleeping()) {
                pose0 = Pose.SLEEPING;
            } else if (self.isSwimming()) {
                pose0 = Pose.SWIMMING;
            } else if (self.isAutoSpinAttack()) {
                pose0 = Pose.SPIN_ATTACK;
            } else if (self.isShiftKeyDown() && !self.getAbilities().flying) {
                pose0 = Pose.CROUCHING;
            } else {
                pose0 = Pose.STANDING;
            }
            if (self.isSpectator() || self.isPassenger() || self.invokeCanPlayerFitWithinBlocksAndEntitiesWhen(pose0)) {
                pose = pose0;
            } else if (self.invokeCanPlayerFitWithinBlocksAndEntitiesWhen(Pose.CROUCHING)) {
                pose = Pose.CROUCHING;
            } else {
                pose = Pose.SWIMMING;
            }
        } else {
            pose = self.getPose();
        }
        float height1 = self.getDimensions(pose).height();
        return height1;
    }
}
