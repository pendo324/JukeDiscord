package me.flyinglawnmower.jd

import me.flyinglawnmower.jd.proxy.IProxy
import me.flyinglawnmower.jd.util.Constants
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, guiFactory = Constants.GUI_FACTORY_PATH)
class JukeAlert {
    companion object {
        @SidedProxy(clientSide = Constants.CLIENT_PROXY_PATH, serverSide = Constants.SERVER_PROXY_PATH)
        var proxy: IProxy? = null
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        proxy?.subscribeEvents(event.suggestedConfigurationFile)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy?.registerKeyBindings()
    }
}

