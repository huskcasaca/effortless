//package dev.huskcasaca.effortless.event
//
//import net.fabricmc.fabric.api.event.Event
//import net.fabricmc.fabric.api.event.EventFactory
//
//internal fun <E : Any> createSimpleEvent(): Event<(E) -> Unit> {
//    return EventFactory.createArrayBacked<(E) -> Unit>(Function1::class.java) { listeners ->
//        { evt ->
//            for (listener in listeners) {
//                listener(evt)
//            }
//        }
//    }
//}
//
///**
// * If any listener returns true, this cancels the method
// */
//internal fun <E : Any> createCancellableEvent(): Event<(E) -> Boolean> {
//    return EventFactory.createArrayBacked<(E) -> Boolean>(Function1::class.java) { listeners ->
//        { evt ->
//            var cancelled = false
//            for (listener in listeners) {
//                cancelled = cancelled || listener(evt)
//            }
//            cancelled
//        }
//    }
//}