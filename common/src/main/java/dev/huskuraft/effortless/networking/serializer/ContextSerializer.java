package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.CubeFilling;
import dev.huskuraft.effortless.building.structure.LineDirection;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.RaisedEdge;

public class ContextSerializer implements NetByteBufSerializer<Context> {

    @Override
    public Context read(NetByteBuf byteBuf) {
        return new Context(
                byteBuf.readUUID(),
                byteBuf.readEnum(BuildState.class),
                byteBuf.readEnum(BuildType.class),
                new Context.BuildInteractions(
                        byteBuf.readList(buffer1 -> buffer1.readNullable(new BlockInteractionSerializer()))
                ),
                new Context.StructureParams(
                        byteBuf.readEnum(BuildMode.class),
                        byteBuf.readEnum(CircleStart.class),
                        byteBuf.readEnum(CubeFilling.class),
                        byteBuf.readEnum(PlaneFilling.class),
                        byteBuf.readEnum(PlaneFacing.class),
                        byteBuf.readEnum(RaisedEdge.class),
                        byteBuf.readEnum(ReplaceMode.class),
                        byteBuf.readEnum(PlaneLength.class),
                        byteBuf.readEnum(LineDirection.class)),
                new Context.PatternParams(
                        new Pattern(
                                byteBuf.readUUID(),
                                byteBuf.readText(),
                                byteBuf.readList(new TransformerSerializer())
                        ),
                        byteBuf.readLong()),
                new Context.CustomParams(
                        byteBuf.read(new GeneralConfigSerializer())
                )
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, Context context) {
        byteBuf.writeUUID(context.getId());
        byteBuf.writeEnum(context.state());
        byteBuf.writeEnum(context.type());
        byteBuf.writeList(context.interactions().results(), (buffer1, target) -> buffer1.writeNullable(target, new BlockInteractionSerializer()));

        byteBuf.writeEnum(context.structureParams().buildMode());
        byteBuf.writeEnum(context.structureParams().circleStart());
        byteBuf.writeEnum(context.structureParams().cubeFilling());
        byteBuf.writeEnum(context.structureParams().planeFilling());
        byteBuf.writeEnum(context.structureParams().planeFacing());
        byteBuf.writeEnum(context.structureParams().raisedEdge());
        byteBuf.writeEnum(context.structureParams().replaceMode());
        byteBuf.writeEnum(context.structureParams().planeLength());
        byteBuf.writeEnum(context.structureParams().lineDirection());

        byteBuf.writeUUID(context.patternParams().pattern().id());
        byteBuf.writeText(context.patternParams().pattern().name());
        byteBuf.writeList(context.patternParams().pattern().transformers(), new TransformerSerializer());
        byteBuf.writeLong(context.patternParams().seed());

        byteBuf.write(context.customParams().generalConfig(), new GeneralConfigSerializer());
    }

}
