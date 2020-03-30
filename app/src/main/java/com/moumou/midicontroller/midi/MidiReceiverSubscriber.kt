package com.moumou.midicontroller.midi

interface MidiReceiverSubscriber {
    fun handle(byteArray: ByteArray)
}
