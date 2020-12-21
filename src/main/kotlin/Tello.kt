package main.kotlin

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

open class Tello {
    private lateinit var socket: DatagramSocket
    val isConnected: Boolean
        get() = socket.isConnected


    fun connect(ip: String = "192.168.10.1", port: Int = 8889) {
        socket = DatagramSocket(port)
        socket.connect(InetAddress.getByName(ip), port)
        sendCommand("command")
    }

    fun disconnect() {
        socket.close()
    }
    // Documentation: https://dl-cdn.ryzerobotics.com/downloads/Tello/Tello%20SDK%202.0%20User%20Guide.pdf

    fun takeOff(): Boolean {
        return isOk(sendCommand("takeoff"))
    }

    fun land(): Boolean {
        return isOk(sendCommand("land"))
    }

    fun streamOn(): Boolean {
        return isOk(sendCommand("streamon"))
    }

    fun streamOff(): Boolean {
        return isOk(sendCommand("streamoff"))
    }

    fun emergency(): Boolean {
        return isOk(sendCommand("emergency"))
    }

    fun hover(): Boolean {
        return isOk(sendCommand("stop")) // Stop = hover in the air.
    }

    fun moveZ(z: Int): Boolean { // forward = (0-100) backwards = (-100-0)
        return isOk(sendCommand("rc 0 $z 0 0 "))
    }


    // Private functions
    private fun isOk(response: String): Boolean {
        return response == "OK"
    }


    private fun sendCommand(command: String): String {
        if (command.isEmpty()) return "No command given."
        if (!isConnected) return "Socket Disconnected."

        val sendData = command.toByteArray()
        val sendPacket = DatagramPacket(sendData, sendData.size, socket.inetAddress, socket.port)
        socket.send(sendPacket)

        val receiveData = ByteArray(1024)
        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        socket.receive(receivePacket)

        return String(receivePacket.data)
    }
}