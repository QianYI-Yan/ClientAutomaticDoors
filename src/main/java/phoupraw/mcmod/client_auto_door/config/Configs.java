package phoupraw.mcmod.client_auto_door.config;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class Configs {
    private static final Map<Path, Config> GLOBALS = new HashMap<>();

    private Configs() {
    }

    public static Config getOrCreateGlobal(Path subpath) {
        return GLOBALS.computeIfAbsent(subpath, p -> new Config(FabricLoader.getInstance().getConfigDir().resolve(p), ConfigView.DEFAULTS));
    }

    public static void saveAll() {
        GLOBALS.values().forEach(Config::save);
    }
}
