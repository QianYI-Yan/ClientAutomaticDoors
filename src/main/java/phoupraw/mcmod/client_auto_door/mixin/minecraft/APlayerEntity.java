package phoupraw.mcmod.client_auto_door.mixin.minecraft;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface APlayerEntity {
    @Invoker("updatePlayerPose")
    void invokeUpdatePlayerPose();
    @Invoker("canPlayerFitWithinBlocksAndEntitiesWhen")
    boolean invokeCanPlayerFitWithinBlocksAndEntitiesWhen(Pose pose);
}
