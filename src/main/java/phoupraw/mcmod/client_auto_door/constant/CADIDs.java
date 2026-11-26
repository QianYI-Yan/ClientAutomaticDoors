package phoupraw.mcmod.client_auto_door.constant;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import phoupraw.mcmod.client_auto_door.ClientAutomaticDoors;

@ApiStatus.NonExtendable
public interface CADIDs {
    static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(ClientAutomaticDoors.ID, path);
    }
}
