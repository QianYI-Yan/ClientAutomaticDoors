package phoupraw.mcmod.client_auto_door.config;

import com.mojang.serialization.Codec;
import phoupraw.mcmod.client_auto_door.ClientAutomaticDoors;

import java.nio.file.Path;

public interface CADConfigs {
    Path PATH = Path.of(ClientAutomaticDoors.ID + ".json");
    ConfigKey<Boolean> ON = new SimpleConfigKey<>("on", Codec.BOOL, true);
}
