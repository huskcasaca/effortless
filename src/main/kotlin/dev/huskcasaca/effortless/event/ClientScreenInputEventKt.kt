//package dev.huskcasaca.effortless.event
//
//sealed class ClientScreenInputEventKt {
//
//    data class KeyPressEvent(
//        val key: Int,
//        val scanCode: Int,
//        val action: Int,
//        val modifiers: Int
//    ) : ClientScreenInputEvent() {
//        companion object {
//            val Dispatcher = createSimpleEvent<KeyPressEvent>()
//        }
//    }
//
//
//}