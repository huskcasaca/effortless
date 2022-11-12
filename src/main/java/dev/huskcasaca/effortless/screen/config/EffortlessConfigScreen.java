package dev.huskcasaca.effortless.screen.config;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.config.BuildConfig;
import dev.huskcasaca.effortless.config.ConfigManager;
import dev.huskcasaca.effortless.config.EffortlessConfig;
import dev.huskcasaca.effortless.config.PreviewConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Function;

public class EffortlessConfigScreen {

    private static final Function<Boolean, Component> yesNoTextSupplier = bool -> {
        if (bool) return Component.translatable("effortless.settings.toggle.on").withStyle(ChatFormatting.GREEN);
        else return Component.translatable("effortless.settings.toggle.off").withStyle(ChatFormatting.RED);
    };

    static String getSettingsNamespace() {
        return Effortless.MOD_ID + "." + "settings";
    }

    static String getSettingsNamespaceTooltip(String path) {
        return Effortless.MOD_ID + "." + "settings" + "." + path + "." + "tooltip";
    }

    static String getSettingsNamespaceTooltip(String path, int ordinal) {
        return Effortless.MOD_ID + "." + "settings" + "." + path + "." + "tooltip" + "_" + ordinal;
    }

    public static Screen createConfigScreen(Screen parentScreen) {

        final var defaults = new EffortlessConfig();

        final var manager = new ConfigManager();
        final var config = manager.getConfig();

        final var builder = ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Component.translatable("effortless.settings.title"))
                .transparentBackground()
                .setDoesConfirmSave(true)
                .setSavingRunnable(() -> {
                    config.validate();
                    manager.writeConfig(false);
                    ConfigManager.INSTANCE.readConfig(false);
                });


        final var configCategory = builder.getOrCreateCategory(Component.translatable("effortless.settings.category.config.title"));

        final var entryBuilder = builder.entryBuilder();

        final var buildSubCat = entryBuilder.startSubCategory(Component.translatable("effortless.settings.category.config.build.title"));
        final var previewSubCat = entryBuilder.startSubCategory(Component.translatable("effortless.settings.category.config.preview.title"));

//        final var maxReachDistance = new SliderEntryData("max_reach", defaults.getBuildConfig().getMaxReachDistance(), config.getBuildConfig().getMaxReachDistance(), BuildConfig.MIN_MAX_REACH_DISTANCE, BuildConfig.MAX_MAX_REACH_DISTANCE, config.getBuildConfig()::setMaxReachDistance);
//        final var maxBlockPlacePerAxis = new SliderEntryData("max_block_axis", defaults.getBuildConfig().getMaxBlockPlacePerAxis(), config.getBuildConfig().getMaxBlockPlacePerAxis(), BuildConfig.MIN_MAX_BLOCK_PLACE_PER_AXIS, BuildConfig.MAX_MAX_BLOCK_PLACE_PER_AXIS, config.getBuildConfig()::setMaxBlockPlacePerAxis);
//        final var maxBlockPlaceAtOnce = new IntegerEntryData("max_block_total", defaults.getBuildConfig().getMaxBlockPlaceAtOnce(), config.getBuildConfig().getMaxBlockPlaceAtOnce(), BuildConfig.MIN_MAX_BLOCK_PLACE_AT_ONCE, BuildConfig.MAX_MAX_BLOCK_PLACE_AT_ONCE, config.getBuildConfig()::setMaxBlockPlaceAtOnce);
//        final var isCanBreakFar = new BooleanEntryData("far_reach", defaults.getBuildConfig().isCanBreakFar(), config.getBuildConfig().isCanBreakFar(), config.getBuildConfig()::setCanBreakFar);
//        final var enableUndo = new BooleanEntryData("undo", defaults.getBuildConfig().isEnableUndo(), config.getBuildConfig().isEnableUndo(), config.getBuildConfig()::setEnableUndo);
//        final var undoStackSize = new SliderEntryData("undo_stack_size", defaults.getBuildConfig().getUndoStackSize(), config.getBuildConfig().getUndoStackSize(), BuildConfig.MIN_UNDO_STACK_SIZE, BuildConfig.MAX_UNDO_STACK_SIZE, config.getBuildConfig()::setUndoStackSize);

//        buildSubCat.add(
//                entryBuilder.startIntSlider(Component.translatable(maxReachDistance.getTitleKey()), maxReachDistance.currentValue, maxReachDistance.minValue, maxReachDistance.maxValue)
//                        .setTooltip(Component.translatable(maxReachDistance.getTooltipKey()))
//                        .setDefaultValue(maxReachDistance.defaultValue)
//                        .setSaveConsumer(maxReachDistance.saveConsumer)
//                        .setTextGetter((integer) -> Component.literal(integer <= 0 ? "Disabled" : integer + " " + (integer <= 1 ? "block" : "blocks")))
//                        .build()
//        );
//        buildSubCat.add(
//                entryBuilder.startIntSlider(Component.translatable(maxBlockPlacePerAxis.getTitleKey()), maxBlockPlacePerAxis.currentValue, maxBlockPlacePerAxis.minValue, maxBlockPlacePerAxis.maxValue)
//                        .setTooltip(Component.translatable(maxBlockPlacePerAxis.getTooltipKey()))
//                        .setDefaultValue(maxBlockPlacePerAxis.defaultValue)
//                        .setSaveConsumer(maxBlockPlacePerAxis.saveConsumer)
//                        .setTextGetter((integer) -> Component.literal(integer <= 0 ? "Disabled" : integer + " " + (integer <= 1 ? "block" : "blocks")))
//                        .build()
//        );
//        buildSubCat.add(
//                entryBuilder.startIntSlider(Component.translatable(maxBlockPlaceAtOnce.getTitleKey()), maxBlockPlaceAtOnce.currentValue, maxBlockPlaceAtOnce.minValue, maxBlockPlaceAtOnce.maxValue)
//                        .setTooltip(Component.translatable(maxBlockPlaceAtOnce.getTooltipKey()))
//                        .setDefaultValue(maxBlockPlaceAtOnce.defaultValue)
//                        .setSaveConsumer((integer) -> {
//                            int rounded = Math.toIntExact(Math.round(integer / 1000.0));
//                            config.getBuildConfig().setMaxBlockPlaceAtOnce(rounded * 1000);
//                        })
//                        .setTextGetter((integer) -> {
//                            // round double
//                            int rounded = Math.toIntExact(Math.round(integer / 1000.0));
//                            return Component.literal(integer <= rounded ? "Disabled" : rounded * 1000 + " blocks");
//                        })
//                        .build()
//        );
//        buildSubCat.add(isCanBreakFar.build(entryBuilder));
//        buildSubCat.add(
//                entryBuilder.startBooleanToggle(Component.translatable(enableUndo.getTitleKey()), enableUndo.currentValue)
//                        .setTooltip(Component.translatable(enableUndo.getTooltipKey()))
//                        .setDefaultValue(enableUndo.defaultValue)
//                        .setSaveConsumer(enableUndo.saveConsumer)
//                        .setYesNoTextSupplier(yesNoTextSupplier)
//                        .build()
//        );
//        buildSubCat.add(
//                entryBuilder.startIntSlider(Component.translatable(undoStackSize.getTitleKey()), undoStackSize.currentValue, undoStackSize.minValue, undoStackSize.maxValue)
//                        .setTooltip(Component.translatable(undoStackSize.getTooltipKey()))
//                        .setDefaultValue(undoStackSize.defaultValue)
//                        .setSaveConsumer(undoStackSize.saveConsumer)
//                        .setTextGetter((integer) -> Component.literal((integer <= 0 ? "Disabled" : (integer + " " + (integer <= 1 ? "step" : "steps")))))
//                        .build()
//        );


        final var alwaysShowBlockPreview = new BooleanEntryData("always_show_block_preview", defaults.getPreviewConfig().isAlwaysShowBlockPreview(), config.getPreviewConfig().isAlwaysShowBlockPreview(), config.getPreviewConfig()::setAlwaysShowBlockPreview);
        final var useShaders = new BooleanEntryData("use_shaders", defaults.getPreviewConfig().isUseShaders(), config.getPreviewConfig().isUseShaders(), config.getPreviewConfig()::setUseShaders);
        final var shaderThreshold = new SliderEntryData("shader_threshold", defaults.getPreviewConfig().getShaderThreshold(), config.getPreviewConfig().getShaderThreshold(), PreviewConfig.MIN_SHADER_THRESHOLD, PreviewConfig.MAX_SHADER_THRESHOLD, config.getPreviewConfig()::setShaderThreshold);
        final var dissolveTimeMultiplier = new SliderEntryData("shader_dissolve_time_multiplier", defaults.getPreviewConfig().getShaderDissolveTimeMultiplier(), config.getPreviewConfig().getShaderDissolveTimeMultiplier(), PreviewConfig.MIN_SHADER_DISSOLVE_TIME_MULTIPLIER, PreviewConfig.MAX_SHADER_DISSOLVE_TIME_MULTIPLIER, config.getPreviewConfig()::setShaderDissolveTimeMultiplier);


        previewSubCat.add(
                entryBuilder.startBooleanToggle(Component.translatable(alwaysShowBlockPreview.getTitleKey()), alwaysShowBlockPreview.currentValue)
                        .setTooltip(Component.translatable(alwaysShowBlockPreview.getTooltipKey()))
                        .setDefaultValue(alwaysShowBlockPreview.defaultValue)
                        .setSaveConsumer(alwaysShowBlockPreview.saveConsumer)
                        .setYesNoTextSupplier(yesNoTextSupplier)
                        .build()
        );
        previewSubCat.add(
                entryBuilder.startBooleanToggle(Component.translatable(useShaders.getTitleKey()), useShaders.currentValue)
                        .setTooltip(Component.translatable(useShaders.getTooltipKey()))
                        .setDefaultValue(useShaders.defaultValue)
                        .setSaveConsumer(useShaders.saveConsumer)
                        .setYesNoTextSupplier(yesNoTextSupplier)
                        .build()
        );
        previewSubCat.add(
                entryBuilder.startIntSlider(Component.translatable(shaderThreshold.getTitleKey()), shaderThreshold.currentValue, shaderThreshold.minValue, shaderThreshold.maxValue)
                        .setTooltip(
                                Component.translatable(getSettingsNamespaceTooltip(shaderThreshold.name()))
                        )
                        .setDefaultValue(shaderThreshold.defaultValue)
                        .setSaveConsumer((integer) -> {
                            int rounded = Math.toIntExact(Math.round(integer / 1000.0));
                            config.getPreviewConfig().setShaderThreshold(rounded * 1000);
                        })
                        .setTextGetter((integer) -> {
                            // round double
                            int rounded = Math.toIntExact(Math.round(integer / 1000.0));
                            return Component.literal(integer <= rounded ? "Disabled" : rounded * 1000 + " blocks");
                        })
                        .build()
        );
        previewSubCat.add(
                entryBuilder.startIntSlider(Component.translatable(dissolveTimeMultiplier.getTitleKey()), dissolveTimeMultiplier.currentValue, dissolveTimeMultiplier.minValue, dissolveTimeMultiplier.maxValue)
                        .setTooltip(
                                Component.translatable(getSettingsNamespaceTooltip(dissolveTimeMultiplier.name(), 0)),
                                Component.translatable(getSettingsNamespaceTooltip(dissolveTimeMultiplier.name(), 1)),
                                Component.translatable(getSettingsNamespaceTooltip(dissolveTimeMultiplier.name(), 2)),
                                Component.translatable(getSettingsNamespaceTooltip(dissolveTimeMultiplier.name(), 3)),
                                Component.translatable(getSettingsNamespaceTooltip(dissolveTimeMultiplier.name(), 4))
                        )
                        .setDefaultValue(dissolveTimeMultiplier.defaultValue)
                        .setSaveConsumer(dissolveTimeMultiplier.saveConsumer)
                        .setTextGetter((integer) -> Component.literal(integer / 10.0 + "x"))
                        .build()
        );

        var buildSubEntry = buildSubCat.build();
        buildSubEntry.setExpanded(true);
        configCategory.addEntry(buildSubEntry);

        var previewSubEntry = previewSubCat.build();
        previewSubEntry.setExpanded(true);
        configCategory.addEntry(previewSubEntry);

        return builder.build();

    }

    interface Entry {

        AbstractConfigListEntry<?> build(ConfigEntryBuilder builder);

        String getName();

        default String getTitleKey() {
            return getSettingsNamespace() + "." + getName() + "." + "title";
        }

        default String getTooltipKey() {
            return getSettingsNamespace() + "." + getName() + "." + "tooltip";
        }

    }

    record SliderEntryData(
            String name,
            int defaultValue,
            int currentValue,
            int minValue,
            int maxValue,
            Consumer<Integer> saveConsumer
    ) implements Entry {

        @Override
        public AbstractConfigListEntry<?> build(ConfigEntryBuilder builder) {
            return builder.startIntSlider(Component.translatable(getTitleKey()), currentValue, minValue, maxValue)
                    .setTooltip(Component.translatable(getTooltipKey()))
                    .setDefaultValue(defaultValue)
                    .setSaveConsumer(saveConsumer)
                    .build();
        }


        public IntSliderBuilder preBuild(ConfigEntryBuilder builder) {
            return builder.startIntSlider(Component.translatable(getTitleKey()), currentValue, minValue, maxValue)
                    .setTooltip(Component.translatable(getTooltipKey()))
                    .setDefaultValue(defaultValue)
                    .setSaveConsumer(saveConsumer);
        }

        @Override
        public String getName() {
            return name();
        }
    }

    record IntegerEntryData(
            String name,
            int defaultValue,
            int currentValue,
            int minValue,
            int maxValue,
            Consumer<Integer> saveConsumer
    ) implements Entry {

        @Override
        public AbstractConfigListEntry<?> build(ConfigEntryBuilder builder) {
            return builder.startIntField(Component.translatable(getTitleKey()), currentValue)
                    .setTooltip(Component.translatable(getTooltipKey()))
                    .setDefaultValue(defaultValue)
                    .setMin(minValue)
                    .setMax(maxValue)
                    .setSaveConsumer(saveConsumer)
                    .build();
        }

        @Override
        public String getName() {
            return name();
        }
    }

    record BooleanEntryData(
            String name,
            boolean defaultValue,
            boolean currentValue,
            Consumer<Boolean> saveConsumer
    ) implements Entry {

        @Override
        public AbstractConfigListEntry<?> build(ConfigEntryBuilder builder) {
            return builder.startBooleanToggle(Component.translatable(getTitleKey()), currentValue)
                    .setTooltip(Component.translatable(getTooltipKey()))
                    .setDefaultValue(defaultValue)
                    .setSaveConsumer(saveConsumer)
                    .setYesNoTextSupplier(yesNoTextSupplier)
                    .build();
        }

        @Override
        public String getName() {
            return name();
        }
    }
}
