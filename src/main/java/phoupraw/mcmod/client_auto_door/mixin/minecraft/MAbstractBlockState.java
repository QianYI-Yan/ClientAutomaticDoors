package phoupraw.mcmod.client_auto_door.mixin.minecraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(BlockBehaviour.BlockStateBase.class)
abstract class MAbstractBlockState {
}
