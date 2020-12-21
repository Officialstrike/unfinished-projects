import main.kotlin.Tello

fun main() {
    val tello = Tello()

    tello.connect()
    if (tello.isConnected) {
        tello.takeOff()
        tello.moveZ(20)
        tello.hover()
        Thread.sleep(10000) // 10s
        tello.land()
        tello.disconnect()
    }

}
