package me.flyinglawnmower.jd.handler

import me.flyinglawnmower.jd.util.Constants
import me.flyinglawnmower.jd.util.Logger
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.common.config.ConfigCategory
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.config.Property
import java.io.File

class ConfigurationHandler {
    lateinit var _configuration: Configuration

    var _initializing: Boolean = false

    private var _discordToken: Property? = null
    private var _discordServer: Property? = null
    private var _discordChannel: Property? = null

    val discordToken: String?
        get() = _discordToken?.string
    val discordServer: String?
        get() = _discordServer?.string
    val discordChannel: String?
        get() = _discordChannel?.string

    private object Holder {
        val INSTANCE = ConfigurationHandler()
    }

    companion object {
        val instance: ConfigurationHandler by lazy { Holder.INSTANCE }
    }

    fun init(suggestedConfigFile: File) {
        _configuration = Configuration(suggestedConfigFile)
        _initializing = true
        load()
        _initializing = false
    }

    fun getConfigCategory(): ConfigCategory? {
        return _configuration.getCategory(Configuration.CATEGORY_GENERAL)
    }

    fun isConfigValid(): Boolean = discordChannel != "" && discordToken != "" && discordServer != ""

    @SubscribeEvent
    fun onConfigurationChangedEvent(event: ConfigChangedEvent.OnConfigChangedEvent) {
        Logger.logInfo("Settings changed.")
        if (event.modID.toLowerCase() == Constants.MOD_ID) {
            load()
        }
    }

    fun load() {
        _discordToken = _configuration.get(Configuration.CATEGORY_GENERAL, "Discord Token", "", "Discord token")
        _discordServer = _configuration.get(Configuration.CATEGORY_GENERAL, "Discord Server ID", "", "Discord Server ID")
        _discordChannel = _configuration.get(Configuration.CATEGORY_GENERAL, "Discord Channel Name", "", "Discord Channel Name")

        if (_configuration.hasChanged()) {
            _configuration.save()
        }
    }
}