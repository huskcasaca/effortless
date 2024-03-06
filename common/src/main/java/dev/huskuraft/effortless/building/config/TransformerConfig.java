package dev.huskuraft.effortless.building.config;

import java.util.List;

import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.settings.RandomizerSettings;

public class TransformerConfig implements ModConfig {

    private List<ArrayTransformer> arrayTransformers;

    private List<MirrorTransformer> mirrorTransformers;

    private List<RadialTransformer> radialTransformers;

    private List<ItemRandomizer> itemRandomizers;

    public TransformerConfig(
            List<ArrayTransformer> arrayTransformers,
            List<MirrorTransformer> mirrorTransformers,
            List<RadialTransformer> radialTransformers,
            List<ItemRandomizer> itemRandomizers
    ) {
        this.arrayTransformers = arrayTransformers;
        this.mirrorTransformers = mirrorTransformers;
        this.radialTransformers = radialTransformers;
        this.itemRandomizers = itemRandomizers;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() {
    }

    public RandomizerSettings getRandomizerSettings() {
        return new RandomizerSettings(this.itemRandomizers);
    }

    public void setItemRandomizerSettings(RandomizerSettings settings) {
        this.itemRandomizers = settings.randomizers();
    }

    public List<ArrayTransformer> getArrays() {
        return arrayTransformers;
    }

    public void setArrays(List<ArrayTransformer> arrayTransformers) {
        this.arrayTransformers = arrayTransformers;
    }

    public List<MirrorTransformer> getMirrors() {
        return mirrorTransformers;
    }

    public void setMirrors(List<MirrorTransformer> mirrorTransformers) {
        this.mirrorTransformers = mirrorTransformers;
    }

    public List<RadialTransformer> getRadials() {
        return radialTransformers;
    }

    public void setRadials(List<RadialTransformer> radialTransformers) {
        this.radialTransformers = radialTransformers;
    }

    public List<ItemRandomizer> getItemRandomizers() {
        return itemRandomizers;
    }

    public void setItemRandomizers(List<ItemRandomizer> itemRandomizers) {
        this.itemRandomizers = itemRandomizers;
    }

}
