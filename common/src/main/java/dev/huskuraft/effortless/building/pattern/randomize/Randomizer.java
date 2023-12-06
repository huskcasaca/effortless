package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.text.Text;

import java.util.Collection;

public abstract class Randomizer<T> extends Transformer {

    public static Category extract(Object object) {
        if (object instanceof ItemStack) {
            return Category.ITEM;
        }
        throw new IllegalArgumentException("Invalid object: " + object);

    }

    public abstract Order getOrder();

    public abstract Target getTarget();

    public abstract Category getCategory();

    public abstract Collection<Chance<T>> getChances();

    public abstract Source<T> asSource(long seed);

    public static enum Order {
        SEQUENCE("sequence"),
        RANDOM("random");

        private final String name;

        Order(String name) {
            this.name = name;
        }

        public String getNameKey() {
            return "effortless.randomizer.order.%s".formatted(getName());
        }

        public String getName() {
            return name;
        }
    }

    public static enum Target {
        SINGLE("single"),
        GROUP("group");

        private final String name;

        Target(String name) {
            this.name = name;
        }

        public String getNameKey() {
            return "effortless.randomizer.target.%s".formatted(getName());
        }

        public String getName() {
            return name;
        }
    }

    public static enum Category {
        ITEM("item", ItemStack.class);

        private final String name;
        private final Class<?> clazz;

        Category(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        public String getNameKey() {
            return "effortless.randomizer.category.%s".formatted(getName());
        }

        public String getName() {
            return name;
        }
    }

}
