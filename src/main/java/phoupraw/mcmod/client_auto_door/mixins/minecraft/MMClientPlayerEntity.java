package phoupraw.mcmod.client_auto_door.mixins.minecraft;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;
import phoupraw.mcmod.client_auto_door.events.BlockShapeToggler;
import phoupraw.mcmod.client_auto_door.mixin.minecraft.AEntity;
import phoupraw.mcmod.client_auto_door.mixin.minecraft.APlayerEntity;
import phoupraw.mcmod.client_auto_door.modules.Vanilla;
import phoupraw.mcmod.util.MCUtils;

import java.util.Collection;
import java.util.Map;

@Environment(EnvType.CLIENT)
@ApiStatus.Internal
public interface MMClientPlayerEntity {
    static void openDoor(LocalPlayer self0, MoverType movementType, Vec3 movement, Minecraft client) {
        //Input input = self.input;
        //if (input==null)return;
        if (movementType != MoverType.SELF /*|| self.hasVehicle()*/) return;
        var self = (LocalPlayer & APlayerEntity) self0;
        //ClientPlayerInteractionManager interactor = client.gameMode;
        //if (interactor == null) return;
        var vehicle = (Entity & AEntity) self.getRootVehicle();
        if (!Vanilla.shouldAct(client, self, vehicle)) return;
        double step = Math.max(0, vehicle.invokeCollide(movement.with(Direction.Axis.Y, 0)).y);
        Vec3 movement1 = movement.with(Direction.Axis.Y, Math.max(0, movement.y - step));
        Level world = self.level();
        AABB vehicleBox = vehicle.getBoundingBox().move(0, step, 0);
        try (var t = Transaction.openOuter()) {
            var vehicleShape = Shapes.create(vehicleBox);
            Map<BlockPos, VoxelShape> shapes = new Object2ObjectOpenHashMap<>();
            float height = self.getBbHeight();
            for (var iter = new BlockCollisions<>(world, vehicle, vehicleBox.expandTowards(movement1), false, Pair::of); iter.hasNext(); ) {
                var pair = iter.next();
                VoxelShape shape = pair.second();
                if (Shapes.joinIsNotEmpty(shape, vehicleShape, BooleanOp.AND)) {
                    continue;
                }
                BlockPos pos = pair.first().immutable();
                BlockState state = world.getBlockState(pos);
                BlockShapeToggler toggler = BlockShapeToggler.LOOKUP.find(world, pos, state, null, vehicle);
                if (toggler == null) continue;
                open:
                try (var t2 = t.openNested()) {
                    Collection<BlockPos> positions = toggler.toggle(self, t2);
                    if (positions.isEmpty()) return;
                    for (BlockPos pos1 : positions) {
                        for (Direction direction : SignalGetter.DIRECTIONS) {
                            BlockPos pos2 = pos1.relative(direction);
                            if (!world.getBlockState(pos2).canSurvive(world, pos2)) {
                                break open;
                            }
                        }
                    }
                    t2.commit();
                    shapes.put(pos, shape);
                }
            }
            if (vehicle == self && height > MCUtils.getHeight(self)) {
                return;
            }
            var adjusted = Shapes.create(vehicleBox.expandTowards(vehicle.invokeCollide(movement)));
            for (var shape : shapes.values()) {
                if (!Shapes.joinIsNotEmpty(shape, adjusted, BooleanOp.AND)) {
                    return;
                }
            }
            t.commit();
            for (BlockPos pos : shapes.keySet()) {
                if (!Vanilla.OPENEDS.containsKey(pos)) {
                    Vanilla.OPENEDS.put(pos, ObjectLongPair.of(world.getBlockState(pos), world.getGameTime()));
                }
            }
        }
    }
    
}
