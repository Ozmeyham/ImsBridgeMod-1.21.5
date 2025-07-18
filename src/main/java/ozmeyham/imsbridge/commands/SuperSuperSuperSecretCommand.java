package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;


public class SuperSuperSuperSecretCommand {
    public static void superSuperSuperSecretCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("specificmessagesenttospecificpeoplefrominfamousimcguild")
                .executes(ctx -> {
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.networkHandler.sendChatMessage("/msg Minemon206 big balls 123");
                    }
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
