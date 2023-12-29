package com.tcortega.powertweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class DelayCommand {
    private static int delay = 150;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("delay")
                .then(argument("amount", integer(0, 1500))
                        .executes(ctx -> {
                            var oldDelay = delay;
                            delay = ctx.getArgument("amount", Integer.class);

                            ctx.getSource().sendFeedback(Text.literal("Set delay from " + oldDelay + " to " + delay));

                            return 1;
                        })));
    }

    public static int getDelay() {
        return delay;
    }
}
