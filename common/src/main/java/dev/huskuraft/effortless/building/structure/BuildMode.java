package dev.huskuraft.effortless.building.structure;

import java.awt.*;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.standard.Circle;
import dev.huskuraft.effortless.building.structure.builder.standard.Cone;
import dev.huskuraft.effortless.building.structure.builder.standard.Cube;
import dev.huskuraft.effortless.building.structure.builder.standard.Cylinder;
import dev.huskuraft.effortless.building.structure.builder.standard.DiagonalLine;
import dev.huskuraft.effortless.building.structure.builder.standard.DiagonalWall;
import dev.huskuraft.effortless.building.structure.builder.standard.Disable;
import dev.huskuraft.effortless.building.structure.builder.standard.Floor;
import dev.huskuraft.effortless.building.structure.builder.standard.Line;
import dev.huskuraft.effortless.building.structure.builder.standard.Pyramid;
import dev.huskuraft.effortless.building.structure.builder.standard.Single;
import dev.huskuraft.effortless.building.structure.builder.standard.SlopeFloor;
import dev.huskuraft.effortless.building.structure.builder.standard.Sphere;
import dev.huskuraft.effortless.building.structure.builder.standard.Wall;

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

    PYRAMID("pyramid", new Pyramid(), Category.ROOF),
    CONE("cone", new Cone(), Category.ROOF),
    ;

//    DOME("dome", new Dome(), Category.ROOF);

    private final BlockStructure provider;
    private final Category category;
    private final BuildFeature[] features;
    private final String name;

    BuildMode(String name, BlockStructure instance, Category category, BuildFeature... features) {
        this.name = name;
        this.provider = instance;
        this.category = category;
        this.features = features;
    }

    public BlockStructure getInstance() {
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

    public ResourceLocation getIcon() {
        return ResourceLocation.of(Effortless.MOD_ID, "textures/mode/%s.png".formatted(name));
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
