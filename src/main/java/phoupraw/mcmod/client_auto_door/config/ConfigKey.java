package phoupraw.mcmod.client_auto_door.config;

import com.mojang.serialization.Codec;

public interface ConfigKey<T> {
    String name();
    Codec<T> codec();
    T defaultValue();
}
