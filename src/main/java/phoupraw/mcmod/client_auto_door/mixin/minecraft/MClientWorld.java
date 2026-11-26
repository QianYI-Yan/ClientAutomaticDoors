package phoupraw.mcmod.client_auto_door.mixin.minecraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
abstract class MClientWorld {

}
