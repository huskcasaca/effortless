package dev.huskuraft.effortless.screen.clipboard;

import java.awt.*;

import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.math.Vector3f;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.renderer.opertaion.BlockRenderLayers;

public final class StructureSnapshotWidget extends AbstractWidget {

    private Snapshot snapshot;

    public StructureSnapshotWidget(Entrance entrance, int x, int y, int width, int height, Snapshot snapshot) {
        super(entrance, x, y, width, height, Text.empty());
        this.snapshot = snapshot;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        renderer.pushPose();
        renderer.translate(getX() + getWidth() / 2f, getY() + getHeight() / 2f, 100);
        var center = snapshot.getCenter();
        var maxY = snapshot.blockData().stream().mapToDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()) * Math.sin(Math.PI / 4) * 0.8  + blockData.blockPosition().getCenter().sub(center).y()).max().orElse(0) + 1.5;
        var minY = snapshot.blockData().stream().mapToDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()) * Math.sin(Math.PI / 4) * 0.8  + blockData.blockPosition().getCenter().sub(center).y()).min().orElse(0) - 1.5;

        var maxX = snapshot.blockData().stream().mapToDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()) * 0.8).max().orElse(0) + 1.5;
        var minX = snapshot.blockData().stream().mapToDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()) * 0.8).min().orElse(0) - 1.5;
        renderer.scale(-Math.min(getWidth() / (maxX - minX), getHeight() / Math.max((maxY - minY), 8) * 1.1));

        renderer.rotate(Vector3f.XP.rotationDegrees(-30));
        renderer.rotate(Vector3f.YP.rotationDegrees(45));
        renderer.pushPose();
        renderer.translate(-snapshot.getCenter().x(), -snapshot.getCenter().y(), -snapshot.getCenter().z());
        for (var blockData : snapshot.blockData()) {
            if (blockData.blockState() != null) {
                renderer.pushPose();
                renderer.translate(blockData.blockPosition().x(), blockData.blockPosition().y(), blockData.blockPosition().z());
                renderer.renderBlockState(BlockRenderLayers.block(Color.WHITE.getRGB()), getEntrance().getClient().getWorld(), blockData.blockPosition().add(getEntrance().getClient().getPlayer().getPosition().toVector3i()), blockData.blockState());
                renderer.popPose();
            }
            if (blockData.blockEntity() != null) {
                renderer.pushPose();
                renderer.translate(blockData.blockPosition().x(), blockData.blockPosition().y(), blockData.blockPosition().z());
                renderer.renderBlockEntity(BlockRenderLayers.block(Color.WHITE.getRGB()), getEntrance().getClient().getWorld(), blockData.blockPosition().add(getEntrance().getClient().getPlayer().getPosition().toVector3i()), blockData.blockEntity());
                renderer.popPose();
            }
        }
        renderer.popPose();
        renderer.popPose();

    }

    private int backgroundColor = 0xdc000000;


    @Override
    public void renderWidgetBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        renderer.renderGradientRect(getLeft(), getTop(), getRight(), getBottom(), backgroundColor, backgroundColor);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }
}
