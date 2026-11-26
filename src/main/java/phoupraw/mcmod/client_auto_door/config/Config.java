package phoupraw.mcmod.client_auto_door.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import phoupraw.mcmod.client_auto_door.ClientAutomaticDoors;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * 单层 JSON 配置。未设置的值回退到 {@link #back}，从而形成“默认值 → 全局 → 当前存档”的覆盖链。
 */
public final class Config implements ConfigView {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path file;
    private final ConfigView back;
    private JsonObject values = new JsonObject();
    private boolean modified;

    public Config(Path file, ConfigView back) {
        this.file = file;
        this.back = back;
        load();
    }

    public void load() {
        JsonObject loaded = new JsonObject();
        if (Files.isRegularFile(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                JsonElement element = JsonParser.parseReader(reader);
                if (element.isJsonObject()) {
                    loaded = element.getAsJsonObject();
                }
            } catch (IOException | RuntimeException e) {
                ClientAutomaticDoors.LOGGER.warn("Failed to load config {}", file, e);
            }
        }
        values = loaded;
        modified = false;
    }

    public void save() {
        if (!modified) return;
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(file)) {
                GSON.toJson(values, writer);
            }
            modified = false;
        } catch (IOException e) {
            ClientAutomaticDoors.LOGGER.warn("Failed to save config {}", file, e);
        }
    }

    @Override
    public <T> T get(ConfigKey<T> key) {
        JsonElement element = values.get(key.name());
        if (element != null) {
            Optional<T> parsed = key.codec().parse(JsonOps.INSTANCE, element).result();
            if (parsed.isPresent()) {
                return parsed.get();
            }
            ClientAutomaticDoors.LOGGER.warn("Failed to parse {} in {}", key.name(), file);
        }
        return back.get(key);
    }

    public <T> void set(ConfigKey<T> key, T value) {
        DataResult<JsonElement> encoded = key.codec().encodeStart(JsonOps.INSTANCE, value);
        Optional<JsonElement> json = encoded.result();
        if (json.isPresent()) {
            values.add(key.name(), json.get());
        } else {
            values.remove(key.name());
        }
        modified = true;
        save();
    }
}
