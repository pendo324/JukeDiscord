package me.flyinglawnmower.jd.util

import net.minecraftforge.fml.common.FMLLog
import org.apache.logging.log4j.Level

object Logger {
    fun log(level: Level, body: Any) {
        FMLLog.log(Constants.MOD_NAME, level, body.toString())
    }

    fun logDebug(body: Any) {
        log(Level.DEBUG, body)
    }

    fun logAll(body: Any) {
        log(Level.ALL, body)
    }

    fun logError(body: Any) {
        log(Level.ERROR, body)
    }

    fun logInfo(body: Any) {
        log(Level.INFO, body)
    }
}
