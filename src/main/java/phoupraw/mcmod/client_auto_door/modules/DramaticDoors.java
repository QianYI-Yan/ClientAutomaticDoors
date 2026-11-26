package phoupraw.mcmod.client_auto_door.modules;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import phoupraw.mcmod.client_auto_door.events.BlockShapeToggler;
import phoupraw.mcmod.client_auto_door.togglers.OpenToggler;
import phoupraw.mcmod.client_auto_door.togglers.PillarOpenToggler;

@ApiStatus.NonExtendable
public interface DramaticDoors {
    String MOD_ID = "dramaticdoors";
    TagKey<Block> TALL_WOODEN_DOORS = TagKey.create(Registries.BLOCK, id("tall_wooden_doors"));
    static @NotNull Identifier id(String tall_wooden_doors) {
        return Identifier.fromNamespaceAndPath(MOD_ID, tall_wooden_doors);
    }
    TagKey<Block> MOB_INTERACTABLE_TALL_DOORS = TagKey.create(Registries.BLOCK, id("mob_interactable_tall_doors"));
    TagKey<Block> SHORT_WOODEN_DOORS = TagKey.create(Registries.BLOCK, id("short_wooden_doors"));
    TagKey<Block> MOB_INTERACTABLE_SHORT_DOORS = TagKey.create(Registries.BLOCK, id("mob_interactable_short_doors"));
    @ApiStatus.Internal
    static @Nullable BlockShapeToggler findShortDoor(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Entity context) {
        return (state.is(SHORT_WOODEN_DOORS, s -> true) || state.is(MOB_INTERACTABLE_SHORT_DOORS, s -> true)) && state.hasProperty(DoorBlock.OPEN) ? new OpenToggler(world, pos, state) : null;
    }
    @ApiStatus.Internal
    static @Nullable BlockShapeToggler findTallDoor(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Entity context) {
        return (state.is(TALL_WOODEN_DOORS, s -> true) || state.is(MOB_INTERACTABLE_TALL_DOORS, s -> true)) && state.hasProperty(DoorBlock.OPEN) ? new PillarOpenToggler(world, pos, state) : null;
    }
}
