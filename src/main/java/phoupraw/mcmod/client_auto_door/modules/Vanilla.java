package phoupraw.mcmod.client_auto_door.modules;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import phoupraw.mcmod.client_auto_door.config.CADConfigs;
import phoupraw.mcmod.client_auto_door.config.Configs;
import phoupraw.mcmod.client_auto_door.events.BlockShapeToggler;
import phoupraw.mcmod.client_auto_door.togglers.DoorToggler;
import phoupraw.mcmod.client_auto_door.togglers.OpenToggler;
import phoupraw.mcmod.util.MCUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

@ApiStatus.NonExtendable
public interface Vanilla {
    Comparator<Vec3i> COMPARATOR = Comparator
      .comparingInt(Vec3i::getY)
      .thenComparingInt(Vec3i::getX)
      .thenComparingInt(Vec3i::getZ);
    Map<BlockPos, ObjectLongPair<BlockState>> OPENEDS = new Object2ObjectRBTreeMap<>(COMPARATOR);
    static boolean shouldAct(Minecraft client, LocalPlayer player, Entity vehicle) {
        if (!Configs.getOrCreateGlobal(CADConfigs.PATH).get(CADConfigs.ON)) return false;
        if (client.player != player) return false;//防止自由相机
        if (player.isShiftKeyDown() || vehicle.noPhysics) return false;
        if (FabricLoader.getInstance().isModLoaded("litematica") && (player.getMainHandItem().getItem() == Items.STICK || player.getOffhandItem().getItem() == Items.STICK)) {
            return false;
        }
        if (FabricLoader.getInstance().isModLoaded("worldedit") && (player.getMainHandItem().getItem() == Items.WOODEN_AXE)) {
            return false;
        }
        return true;
    }
    //FIXME 在一竖列活板门上跳会关门导致趴下
    @ApiStatus.Internal
    static void close(ClientLevel world) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) return;
        var vehicle = player.getRootVehicle();
        if (!shouldAct(client, player, vehicle)) return;
        var interactor = client.gameMode;
        if (interactor == null) return;
        Long2ObjectMap<Map<BlockPos, BlockState>> timeGrouped = new Long2ObjectArrayMap<>(3);
        for (var entry : OPENEDS.entrySet()) {
            long time = entry.getValue().secondLong();
            var openeds = timeGrouped.get(time);
            if (openeds == null) {
                openeds = new Object2ObjectRBTreeMap<>(COMPARATOR);
                timeGrouped.put(time, openeds);
            }
            openeds.put(entry.getKey(), entry.getValue().first());
        }
        float height = player.getBbHeight();
        for (var timeEntry : timeGrouped.long2ObjectEntrySet()) {
            long elapsed = world.getGameTime() - timeEntry.getLongKey();
            if (elapsed <= 0) continue;
            try (var t = Transaction.openOuter()) {
                Collection<BlockPos> removed = new ObjectOpenHashSet<>();
                for (var iter = timeEntry.getValue().entrySet().iterator(); iter.hasNext(); ) {
                    var entry = iter.next();
                    BlockPos pos = entry.getKey();
                    BlockState state = world.getBlockState(pos);
                    if (state != entry.getValue()) {
                        if (elapsed > 10) {
                            iter.remove();
                        }
                        continue;
                    }
                    BlockShapeToggler toggler = BlockShapeToggler.LOOKUP.find(world, pos, state, null, vehicle);
                    if (toggler == null || toggler.toggle(player, t).isEmpty()) {
                        continue;
                    }
                    removed.add(pos);
                }
                if (vehicle == player && height > MCUtils.getHeight(player)) {
                    continue;
                }
                OPENEDS.keySet().removeAll(removed);
                t.commit();
            }
        }
    }
    @ApiStatus.Internal
    static @Nullable BlockShapeToggler findTrapdoorAndFencGate(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Entity context) {
        return ((state.is(BlockTags.TRAPDOORS, s -> true) && state.getBlock() != Blocks.IRON_TRAPDOOR) || state.is(BlockTags.FENCE_GATES, s -> true)) && state.hasProperty(DoorBlock.OPEN) ? new OpenToggler(world, pos, state) : null;
    }
    @ApiStatus.Internal
    static @Nullable BlockShapeToggler findDoor(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Entity context) {
        return state.is(BlockTags.DOORS, s -> true) && state.getBlock() != Blocks.IRON_DOOR && state.hasProperty(DoorBlock.OPEN) && state.hasProperty(DoorBlock.HALF) ? new DoorToggler(world, pos, state) : null;
    }
}
