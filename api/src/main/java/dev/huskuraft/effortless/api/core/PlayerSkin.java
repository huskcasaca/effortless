package dev.huskuraft.effortless.api.core;

import javax.annotation.Nullable;

public record PlayerSkin(
        @Nullable
        ResourceLocation texture,
        @Nullable
        ResourceLocation capeTexture,
        @Nullable
        ResourceLocation elytraTexture,
        PlayerSkin.Model model
) {

   public enum Model {
      SLIM("slim"),
      WIDE("default");

      private final String id;

      Model(String id) {
         this.id = id;
      }

      public String id() {
         return this.id;
      }

       public static PlayerSkin.Model byName(@Nullable String name) {
           if (name == null) {
               return WIDE;
           }
           if (name.equals("slim")) {
               return SLIM;
           } else {
               return WIDE;
           }
       }
   }
}
