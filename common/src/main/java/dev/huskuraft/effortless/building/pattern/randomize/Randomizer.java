package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.text.Text;

import java.util.Collection;

public interface Randomizer<T> extends Transformer {

    static Category extract(Object object) {
        if (object instanceof ItemStack) {
            return Category.ITEM;
        }
        throw new IllegalArgumentException("Invalid object: " + object);

    }

    Order getOrder();

    Target getTarget();

    Category getCategory();

    Collection<Chance<T>> getChances();

    Source<T> asSource(long seed);

    enum Order {
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

    enum Target {
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

    enum Category {
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
