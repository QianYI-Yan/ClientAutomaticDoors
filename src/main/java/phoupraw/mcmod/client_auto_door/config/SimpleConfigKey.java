package phoupraw.mcmod.client_auto_door.config;

import com.mojang.serialization.Codec;

public record SimpleConfigKey<T>(String name, Codec<T> codec, T defaultValue) implements ConfigKey<T> {
}
