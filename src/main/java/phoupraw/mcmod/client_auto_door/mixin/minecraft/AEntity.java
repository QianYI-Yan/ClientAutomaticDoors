package phoupraw.mcmod.client_auto_door.mixin.minecraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface AEntity {
    @Invoker("collide")
    Vec3 invokeCollide(Vec3 movement);
}
