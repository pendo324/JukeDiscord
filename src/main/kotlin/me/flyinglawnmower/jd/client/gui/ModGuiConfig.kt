package me.flyinglawnmower.jd.client.gui

import me.flyinglawnmower.jd.handler.ConfigurationHandler
import me.flyinglawnmower.jd.util.Constants
import net.minecraftforge.fml.client.config.GuiConfig
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.ConfigElement

class ModGuiConfig(parentScreen: GuiScreen) : GuiConfig(parentScreen,
        ConfigElement(ConfigurationHandler.instance.getConfigCategory()).childElements,
        Constants.MOD_ID,
        false,
        false,
        GuiConfig.getAbridgedConfigPath("")) {
}
