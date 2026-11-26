package phoupraw.mcmod.client_auto_door.config;

public interface ConfigView {
    <T> T get(ConfigKey<T> key);

    ConfigView DEFAULTS = new ConfigView() {
        @Override
        public <T> T get(ConfigKey<T> key) {
            return key.defaultValue();
        }
    };
}
