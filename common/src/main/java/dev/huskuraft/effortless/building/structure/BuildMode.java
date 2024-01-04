package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.building.structure.builder.Builder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Circle;
import dev.huskuraft.effortless.building.structure.builder.doubles.Floor;
import dev.huskuraft.effortless.building.structure.builder.doubles.Line;
import dev.huskuraft.effortless.building.structure.builder.doubles.Wall;
import dev.huskuraft.effortless.building.structure.builder.singles.Disable;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;
import dev.huskuraft.effortless.building.structure.builder.triples.*;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.awt.*;

public enum BuildMode {
    DISABLED("disabled", new Disable(), Category.BASIC),
    SINGLE("single", new Single(), Category.BASIC /*, BuildOption.BUILD_SPEED*/),

    LINE("line", new Line(), Category.SQUARE /*, OptionEnum.THICKNESS*/),
    //    SQUARE("square", new Square(), Category.SQUARE, BuildFeature.PLANE_FILLING, BuildFeature.PLANE_FACING),
    WALL("wall", new Wall(), Category.SQUARE, BuildFeature.PLANE_FILLING),
    FLOOR("floor", new Floor(), Category.SQUARE, BuildFeature.PLANE_FILLING),
    CUBE("cube", new Cube(), Category.SQUARE, BuildFeature.CUBE_FILLING),

    DIAGONAL_LINE("diagonal_line", new DiagonalLine(), Category.DIAGONAL),
    DIAGONAL_WALL("diagonal_wall", new DiagonalWall(), Category.DIAGONAL),
    SLOPE_FLOOR("slope_floor", new SlopeFloor(), Category.DIAGONAL, BuildFeature.RAISED_EDGE),

    CIRCLE("circle", new Circle(), Category.CIRCULAR, BuildFeature.CIRCLE_START, BuildFeature.PLANE_FILLING, BuildFeature.PLANE_FACING),
    CYLINDER("cylinder", new Cylinder(), Category.CIRCULAR, BuildFeature.CIRCLE_START, BuildFeature.PLANE_FILLING, BuildFeature.PLANE_FACING),
    SPHERE("sphere", new Sphere(), Category.CIRCULAR, BuildFeature.CIRCLE_START, BuildFeature.PLANE_FILLING, BuildFeature.PLANE_FACING),
    ;

//    PYRAMID("pyramid", new Pyramid(), Category.ROOF),
//    CONE("cone", new Cone(), Category.ROOF),
//    DOME("dome", new Dome(), Category.ROOF);

    private final Builder provider;
    private final Category category;
    private final BuildFeature[] features;
    private final String name;

    BuildMode(String name, Builder instance, Category category, BuildFeature... features) {
        this.name = name;
        this.provider = instance;
        this.category = category;
        this.features = features;
    }

    public Builder getInstance() {
        return provider;
    }

    public Color getTintColor() {
        return category.getColor();
    }

    public BuildFeature[] getSupportedFeatures() {
        return features;
    }

    public String getName() {
        return name;
    }

    public Text getDisplayName() {
        if (isDisabled()) {
            return Text.translate("effortless.mode.%s".formatted(name)).withStyle(TextStyle.GRAY);
        }
        return Text.translate("effortless.mode.%s".formatted(name));
    }

    public Resource getIcon() {
        return Resource.of("textures/mode/%s.png".formatted(name));
    }

    public boolean isDisabled() {
        return this == BuildMode.DISABLED;
    }

    public boolean isEnabled() {
        return this != BuildMode.DISABLED;
    }

    public enum Category {
        BASIC(new Color(0f, .5f, 1f, .5f)),
        SQUARE(new Color(1f, .54f, .24f, .5f)),
        DIAGONAL(new Color(0.56f, 0.28f, 0.87f, .5f)),
        CIRCULAR(new Color(0.29f, 0.76f, 0.3f, .5f)),
        ROOF(new Color(0.83f, 0.87f, 0.23f, .5f));

        private final Color color;

        Category(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

}
