package io.wispforest.owo;

import io.wispforest.owo.command.debug.OwoDebugCommands;
import io.wispforest.owo.itemgroup.json.GroupTabLoader;
import io.wispforest.owo.moddata.ModDataLoader;
import io.wispforest.owo.ops.LootOps;
import io.wispforest.owo.text.InsertingTextContent;
import io.wispforest.owo.ui.parsing.UIModelLoader;
import io.wispforest.owo.ui.util.UISounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import static io.wispforest.owo.ops.TextOps.withColor;

public class Owo implements ModInitializer {

    /**
     * Whether oωo debug is enabled, this defaults to {@code true} in a development environment.
     * To override that behaviour, add the {@code -Dowo.forceDisableDebug=true} java argument
     */
    public static final boolean DEBUG;
    public static final Logger LOGGER = LogManager.getLogger("owo");
    private static MinecraftServer SERVER;

    public static final Text PREFIX = Text.literal("[").formatted(Formatting.GRAY)
            .append(withColor("o", 0x3955e5))
            .append(withColor("ω", 0x13a6f0))
            .append(withColor("o", 0x3955e5))
            .append(Text.literal("] ").formatted(Formatting.GRAY));

    static {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            DEBUG = !Boolean.getBoolean("owo.forceDisableDebug");
        } else {
            DEBUG = Boolean.getBoolean("owo.debug");
        }
    }

    @Override
    @ApiStatus.Internal
    public void onInitialize() {
        ModDataLoader.load(new GroupTabLoader());
        LootOps.registerListener();
        InsertingTextContent.init();

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new UIModelLoader());

        ServerLifecycleEvents.SERVER_STARTING.register(server -> SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER = null);

        Registry.register(Registry.SOUND_EVENT, UISounds.UI_INTERACTION.getId(), UISounds.UI_INTERACTION);

        if (!DEBUG) return;

        OwoDebugCommands.register();
    }

    public static void debugWarn(Logger logger, String message) {
        if (!DEBUG) return;
        logger.warn(message);
    }

    public static void debugWarn(Logger logger, String message, Object... params) {
        if (!DEBUG) return;
        logger.warn(message, params);
    }

    public static MinecraftServer currentServer() {
        return SERVER;
    }

}