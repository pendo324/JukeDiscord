package me.flyinglawnmower.jd.proxy

import me.flyinglawnmower.jd.discord.DiscordMessager
import me.flyinglawnmower.jd.handler.ConfigurationHandler
import me.flyinglawnmower.jd.util.Constants
import me.flyinglawnmower.jd.util.Logger
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard
import java.io.File

class ClientProxy : IProxy {
    val mc: Minecraft = Minecraft.getMinecraft()
    var isEnabled: Boolean = false
    var toggle: KeyBinding? = null

    var messager: DiscordMessager? = null

    val discordToken = ConfigurationHandler.instance.discordToken
    val discordServer = ConfigurationHandler.instance.discordServer
    val discordChannel = ConfigurationHandler.instance.discordChannel

    override fun registerKeyBindings() {
        toggle = KeyBinding("Toggle JukeDiscord", Keyboard.KEY_HOME, "JukeDiscord")
        ClientRegistry.registerKeyBinding(toggle)
    }

    override fun subscribeEvents(configurationFile: File) {
        ConfigurationHandler.instance.init(configurationFile)
        if (ConfigurationHandler.instance.isConfigValid()) {
            enable()
        }
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(ConfigurationHandler.instance)
    }

    fun enable() {
        isEnabled = true
        if (messager == null) {
            messager = DiscordMessager(ConfigurationHandler.instance.discordToken as String)
        }
    }

    fun isSnitchMessage(message: ITextComponent): Boolean {
        val match = message.last().unformattedText.contains(Regex("""^\*.*(entered snitch at)|\*.*(logged out in snitch)|\*.*(logged in to snitch)"""))
        Logger.logInfo("matched?: " + match)
        return match
    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (toggle!!.isPressed) {
            if (!isEnabled) {
                if (ConfigurationHandler.instance.isConfigValid()) {
                    mc.thePlayer.addChatMessage(TextComponentString(TextFormatting.DARK_AQUA.toString() + "[JukeDiscord] " + TextFormatting.GRAY + "JukeDiscord Enabled"))
                    enable()
                } else {
                    Logger.logInfo("Missing configuration information. Unable to send Discord message.")
                    mc.thePlayer.addChatMessage(TextComponentString(TextFormatting.DARK_AQUA.toString() + "[JukeDiscord] " + TextFormatting.GRAY + "To use JukeDiscord, make sure all of the plugin settings are correct"))
                }
            } else if (isEnabled) {
                mc.thePlayer.addChatMessage(TextComponentString(TextFormatting.DARK_AQUA.toString() + "[JukeDiscord] " + TextFormatting.GRAY + "JukeDiscord Disabled"))
                isEnabled = false
            }
        }
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (isSnitchMessage(event.message)) {
            if (ConfigurationHandler.instance.isConfigValid() && isEnabled) {
                messager?.sendMessage(ConfigurationHandler.instance.discordServer as String, ConfigurationHandler.instance.discordChannel as String, event.message.unformattedText)
            } else {
                Logger.logInfo("Missing configuration information. Unable to send Discord message.")
            }
        }
    }

    @SubscribeEvent
    fun onConfigurationChangedEvent(event: ConfigChangedEvent.PostConfigChangedEvent) {
        if (ConfigurationHandler.instance.isConfigValid()) {
            mc.thePlayer.addChatMessage(TextComponentString(TextFormatting.DARK_AQUA.toString() + "[JukeDiscord] " + TextFormatting.GRAY + "JukeDiscord Enabled"))
            enable()
        }
    }
}