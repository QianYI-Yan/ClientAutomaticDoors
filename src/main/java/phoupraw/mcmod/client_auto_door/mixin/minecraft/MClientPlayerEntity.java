package phoupraw.mcmod.client_auto_door.mixin.minecraft;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phoupraw.mcmod.client_auto_door.mixins.minecraft.MMClientPlayerEntity;

@Environment(EnvType.CLIENT)
@Mixin(value = LocalPlayer.class, priority = 10000)
abstract class MClientPlayerEntity extends AbstractClientPlayer {
    @Shadow
    @Final
    protected Minecraft minecraft;
    public MClientPlayerEntity(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }
    @Inject(method = "move", at = @At("HEAD"))
    private void openDoor(MoverType movementType, Vec3 movement, CallbackInfo ci) {
        MMClientPlayerEntity.openDoor((LocalPlayer) (Object) this, movementType, movement, minecraft);
    }
}
