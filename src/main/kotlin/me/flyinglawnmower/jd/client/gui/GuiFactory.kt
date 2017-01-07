package me.flyinglawnmower.jd.client.gui

import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

import kotlin.collections.Set

class GuiFactory : IModGuiFactory {
    override fun initialize(minecraftInstance: Minecraft) {

    }

    override fun mainConfigGuiClass() : Class<out GuiScreen> {
        return ModGuiConfig::class.java
    }

    override fun runtimeGuiCategories() : Set<IModGuiFactory.RuntimeOptionCategoryElement>? {
        return null
    }

    override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement) : IModGuiFactory.RuntimeOptionGuiHandler? {
        return null
    }
}