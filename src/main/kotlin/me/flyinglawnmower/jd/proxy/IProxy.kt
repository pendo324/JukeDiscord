package me.flyinglawnmower.jd.proxy

import java.io.File

interface IProxy {
    fun subscribeEvents(configurationFile: File)
    fun registerKeyBindings()
}