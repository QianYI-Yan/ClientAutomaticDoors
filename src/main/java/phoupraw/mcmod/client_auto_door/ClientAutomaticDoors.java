package phoupraw.mcmod.client_auto_door;

import net.fabricmc.api.ModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;

public final class ClientAutomaticDoors implements ModInitializer {
    public static final String ID = "client_auto_door";
    @ApiStatus.Internal
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String NAME_KEY = "modmenu.nameTranslation." + ID;
    public static @NotNull MutableComponent name() {
        return Component.translatable(NAME_KEY);
    }
    static void loadClass(Class<?> cls) {
        try {
            MethodHandles.lookup().ensureInitialized(cls);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
    @Override
    public void onInitialize() {
        
    }
}
