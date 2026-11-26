package phoupraw.mcmod.client_auto_door.config;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import java.util.Locale;

/**
 * 内置的最小等价命令（仅全局开关）：
 * /trilevel_config global client_auto_door.json get on
 * /trilevel_config global client_auto_door.json set on 0|1|true|false
 */
public final class TrilevelConfigCommand {
    private TrilevelConfigCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
          dispatcher.register(lit("trilevel_config").then(lit("global").then(config()))));
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> config() {
        return lit(CADConfigs.PATH.toString())
          .then(lit("get").then(lit(CADConfigs.ON.name()).executes(ctx -> {
              boolean value = Configs.getOrCreateGlobal(CADConfigs.PATH).get(CADConfigs.ON);
              ctx.getSource().sendFeedback(Component.literal(CADConfigs.ON.name() + " = " + value));
              return 1;
          })))
          .then(lit("set").then(word("value").executes(ctx -> {
              String raw = StringArgumentType.getString(ctx, "value");
              Boolean value = switch (raw.toLowerCase(Locale.ROOT)) {
                  case "0", "false" -> Boolean.FALSE;
                  case "1", "true" -> Boolean.TRUE;
                  default -> null;
              };
              if (value == null) {
                  ctx.getSource().sendError(Component.literal("Expected 0/1/true/false, got " + raw));
                  return 0;
              }
              Configs.getOrCreateGlobal(CADConfigs.PATH).set(CADConfigs.ON, value);
              ctx.getSource().sendFeedback(Component.literal(CADConfigs.ON.name() + " = " + value));
              return 1;
          })));
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> lit(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static RequiredArgumentBuilder<FabricClientCommandSource, String> word(String name) {
        return RequiredArgumentBuilder.argument(name, StringArgumentType.word());
    }
}
