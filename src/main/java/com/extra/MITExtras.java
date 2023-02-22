package com.extra;

import com.extra.features.gardenNuker;
import com.extra.features.cropNuker;
import com.extra.macros.MacroUtil;
import com.extra.commands.ExtrasCommand;
import com.extra.commands.cropNukerCommand;
import com.extra.commands.NukerCommand;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@Mod(modid = "MITExtras", name = "MITExtras", version = "1.0.0", clientSideOnly = true)
@SideOnly(Side.CLIENT)
public class MITExtras {

    @Mod.Instance("MITExtras")
    public static MITExtras instance;

    public static File modFile = null;

    public static boolean NukerEnabled = false;

    public static boolean cropNuker = false;

    public static Block cropNukerType = Blocks.wheat;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        registerCommands(
                new ExtrasCommand(), new cropNukerCommand(), new NukerCommand()
        );
        registerEvents(
                new MacroUtil(), new cropNuker(), new gardenNuker()
        );
        registerKeybinds(

        );
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modFile = event.getSourceFile();
    }

    private void registerKeybinds(KeyBinding... keybinds) {
        for (KeyBinding keybind : keybinds) {
            ClientRegistry.registerKeyBinding(keybind);
        }
    }

    private void registerEvents(Object... events) {
        for (Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
        }
    }

    private void registerCommands(Command... commands) {
        for (Command command : commands) {
            EssentialAPI.getCommandRegistry().registerCommand(command);
        }
    }
}