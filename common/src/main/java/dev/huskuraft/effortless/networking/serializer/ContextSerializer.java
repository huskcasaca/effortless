package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.CubeFilling;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.RaisedEdge;

public class ContextSerializer implements BufferSerializer<Context> {

    @Override
    public Context read(Buffer buffer) {
        return new Context(
                buffer.readUUID(),
                buffer.readEnum(BuildState.class),
                buffer.readEnum(BuildType.class),
                new Context.BuildInteractions(
                        buffer.readList(buffer1 -> buffer1.readNullable(new BlockInteractionSerializer()))
                ),
                new Context.StructureParams(
                        buffer.readEnum(BuildMode.class),
                        buffer.readEnum(CircleStart.class),
                        buffer.readEnum(CubeFilling.class),
                        buffer.readEnum(PlaneFilling.class),
                        buffer.readEnum(PlaneFacing.class),
                        buffer.readEnum(RaisedEdge.class),
                        buffer.readEnum(ReplaceMode.class)),
                new Context.PatternParams(
                        new Pattern(
                                buffer.readUUID(),
                                buffer.readText(),
                                buffer.readList(new TransformerSerializer())
                        ),
                        buffer.readLong()),
                new Context.LimitationParams(
                        buffer.read(new GeneralConfigSerializer())
                )
        );
    }

    @Override
    public void write(Buffer buffer, Context context) {
        buffer.writeUUID(context.getId());
        buffer.writeEnum(context.state());
        buffer.writeEnum(context.type());
        buffer.writeList(context.interactions().results(), (buffer1, target) -> buffer1.writeNullable(target, new BlockInteractionSerializer()));

        buffer.writeEnum(context.buildMode());
        buffer.writeEnum(context.circleStart());
        buffer.writeEnum(context.cubeFilling());
        buffer.writeEnum(context.planeFilling());
        buffer.writeEnum(context.planeFacing());
        buffer.writeEnum(context.raisedEdge());
        buffer.writeEnum(context.replaceMode());

        buffer.writeUUID(context.patternParams().pattern().id());
        buffer.writeText(context.patternParams().pattern().name());
        buffer.writeList(context.patternParams().pattern().transformers(), new TransformerSerializer());
        buffer.writeLong(context.patternParams().seed());

        buffer.write(context.limitationParams().generalConfig(), new GeneralConfigSerializer());
    }

}
