package com.github.sedovalx.gradle.aspectj

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.IMessageHolder
import org.gradle.api.logging.Logger
import java.util.concurrent.CopyOnWriteArrayList

class MsgHolder(private val logger: Logger) : IMessageHolder {
    /**
     * All messages seen so far.
     */
    @Transient
    private val messages = CopyOnWriteArrayList<IMessage>()

    override fun hasAnyMessage(kind: IMessage.Kind, greater: Boolean): Boolean {
        return getMessages(kind, greater).isNotEmpty()
    }

    override fun numMessages(kind: IMessage.Kind, greater: Boolean): Int {
        return getMessages(kind, greater).size
    }

    override fun getMessages(kind: IMessage.Kind, greater: Boolean): Array<IMessage> {
        return this.messages.filter { it.kind == kind || greater && IMessage.Kind.COMPARATOR.compare(it.kind, kind) > 0 }.toTypedArray()
    }

    override fun getUnmodifiableListView(): List<IMessage> {
        throw UnsupportedOperationException()
    }

    override fun clearMessages() {
        throw UnsupportedOperationException()
    }

    override fun handleMessage(msg: IMessage): Boolean {
        if (msg.kind == IMessage.ERROR
                || msg.kind == IMessage.FAIL
                || msg.kind == IMessage.ABORT) {
            logger.error(msg.message)
        } else if (msg.kind == IMessage.WARNING) {
            logger.warn(msg.message)
        } else {
            logger.debug(msg.message)
        }
        this.messages.add(msg)
        return true
    }

    override fun isIgnoring(kind: IMessage.Kind): Boolean {
        return false
    }

    override fun dontIgnore(kind: IMessage.Kind?) {
        assert(kind != null)
    }

    override fun ignore(kind: IMessage.Kind?) {
        assert(kind != null)
    }
}