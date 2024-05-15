package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.BuildStructure;
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

public class BuildStructureSerializer implements NetByteBufSerializer<BuildStructure> {

    @Override
    public BuildStructure read(NetByteBuf byteBuf) {
        return switch (byteBuf.readEnum(BuildMode.class)) {
            case DISABLED -> new Disable();
            case SINGLE -> new Single();
            case LINE -> new Line();
            case WALL -> new Wall();
            case FLOOR -> new Floor();
            case CUBE -> new Cube();
            case DIAGONAL_LINE -> new DiagonalLine();
            case DIAGONAL_WALL -> new DiagonalWall();
            case SLOPE_FLOOR -> new SlopeFloor();
            case CIRCLE ->new Circle();
            case CYLINDER -> new Cylinder();
            case SPHERE ->new Sphere();
            case PYRAMID -> new Pyramid();
            case CONE -> new Cone();
        };
//        byteBuf.readEnum(CircleStart.class);
//        byteBuf.readEnum(CubeFilling.class);
//        byteBuf.readEnum(PlaneFilling.class);
//        byteBuf.readEnum(PlaneFacing.class);
//        byteBuf.readEnum(RaisedEdge.class);
//        byteBuf.readEnum(PlaneLength.class);
//        byteBuf.readEnum(LineDirection.class);

    }

    @Override
    public void write(NetByteBuf byteBuf, BuildStructure buildStructure) {
        byteBuf.writeEnum(buildStructure.getMode());
    }

}
