package dev.huskuraft.effortless.renderer.outliner;

import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.renderer.Renderer;

public class ChasingBlockBoundingBoxOutline extends BlockBoundingBoxOutline {

    BoundingBox3d targetBB;
    BoundingBox3d prevBB;

    public ChasingBlockBoundingBoxOutline(BoundingBox3d bb) {
        super(bb);
        prevBB = bb.inflate(0);
        targetBB = bb.inflate(0);
    }

    private static BoundingBox3d interpolateBBs(BoundingBox3d current, BoundingBox3d target, float deltaTick) {
        return BoundingBox3d.of(MathUtils.lerp(deltaTick, current.minX(), target.minX()),
                MathUtils.lerp(deltaTick, current.minY(), target.minY()), MathUtils.lerp(deltaTick, current.minZ(), target.minZ()),
                MathUtils.lerp(deltaTick, current.maxX(), target.maxX()), MathUtils.lerp(deltaTick, current.maxY(), target.maxY()),
                MathUtils.lerp(deltaTick, current.maxZ(), target.maxZ()));
    }

    public void target(BoundingBox3d target) {
        targetBB = target;
    }

    @Override
    public void tick() {
        prevBB = bb;
        setBounds(interpolateBBs(bb, targetBB, .5f));
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {
        renderBB(renderer, interpolateBBs(prevBB, bb, deltaTick));
    }

}
