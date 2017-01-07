package me.flyinglawnmower.jd.discord

import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.obj.IChannel

import me.flyinglawnmower.jd.util.Logger
import sx.blah.discord.handle.obj.IGuild

class DiscordMessager(token: String) {
    val discordClient: IDiscordClient = ClientBuilder().withToken(token).login()

    init {
        discordClient.dispatcher.registerListener(this)
    }

    var ready: Boolean = false

    private val messageQueue = arrayOf<DiscordMessage>()

    private data class DiscordMessage(val server: String, val channel: String, val body: String)

    @EventSubscriber
    fun onReadyEvent(event: ReadyEvent) { // This method is called when the ReadyEvent is dispatched
        ready = true
        // empty queue
        emptyMessageQueue()
    }

    fun sendMessage(server: String, channel: String, body: String) {
        val newMessage = DiscordMessage(server, channel, body)
        when (ready) {
            true -> sendMessage(newMessage)
            false -> messageQueue.plusElement(newMessage)
        }
    }

    private fun sendMessage(message: DiscordMessage) {
        Logger.logInfo(message)

        val channel = discordClient.guilds.find {
            it.id == message.server
        }?.channels?.find {
            it.name == message.channel
        }

        when (channel) {
            is IChannel -> channel.sendMessage(message.body)
        }
    }

    private fun emptyMessageQueue() {
        for (message: DiscordMessage in messageQueue) {
            Logger.logInfo(messageQueue)
            sendMessage(message)
        }
    }
}
