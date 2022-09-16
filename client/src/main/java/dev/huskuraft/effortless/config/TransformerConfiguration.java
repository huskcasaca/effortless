package dev.huskuraft.effortless.config;

import dev.huskuraft.effortless.building.pattern.array.Array;
import dev.huskuraft.effortless.building.pattern.mirror.Mirror;
import dev.huskuraft.effortless.building.pattern.raidal.Radial;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.settings.RandomizerSettings;

import java.util.List;

public class TransformerConfiguration implements Configuration {

    private List<Array> arrays;

    private List<Mirror> mirrors;

    private List<Radial> radials;

    private List<ItemRandomizer> itemRandomizers;

    public TransformerConfiguration(
            List<Array> arrays,
            List<Mirror> mirrors,
            List<Radial> radials,
            List<ItemRandomizer> itemRandomizers
    ) {
        this.arrays = arrays;
        this.mirrors = mirrors;
        this.radials = radials;
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

    public List<Array> getArrays() {
        return arrays;
    }

    public void setArrays(List<Array> arrays) {
        this.arrays = arrays;
    }

    public List<Mirror> getMirrors() {
        return mirrors;
    }

    public void setMirrors(List<Mirror> mirrors) {
        this.mirrors = mirrors;
    }

    public List<Radial> getRadials() {
        return radials;
    }

    public void setRadials(List<Radial> radials) {
        this.radials = radials;
    }

    public List<ItemRandomizer> getItemRandomizers() {
        return itemRandomizers;
    }

    public void setItemRandomizers(List<ItemRandomizer> itemRandomizers) {
        this.itemRandomizers = itemRandomizers;
    }

}
