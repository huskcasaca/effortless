package dev.huskuraft.effortless.fabric.mixin;

@Mixin(PlayerList.class)
abstract class PlayerListMixin {

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void onPlayerLoggedIn(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        ServerPlayerEvents.LOGGED_IN.invoker().onLoggedIn(serverPlayer);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void onPlayerLoggedOut(ServerPlayer serverPlayer, CallbackInfo ci) {
        ServerPlayerEvents.LOGGED_OUT.invoker().onLoggedOut(serverPlayer);
    }

    @Inject(method = "respawn", at = @At("TAIL"))
    private void onPlayerRespawned(ServerPlayer oldPlayer, boolean alive, CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayerEvents.RESPAWN.invoker().onRespawn(oldPlayer, cir.getReturnValue(), alive);
    }
}
