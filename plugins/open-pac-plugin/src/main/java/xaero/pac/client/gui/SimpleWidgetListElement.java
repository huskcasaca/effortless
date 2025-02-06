package xaero.pac.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.Vec3i;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.function.BiFunction;

public class SimpleWidgetListElement extends WidgetListElement<SimpleWidgetListElement> {

	protected SimpleWidgetListElement(int w, int h, boolean mutable, BiFunction<SimpleWidgetListElement, Vec3i, AbstractWidget> widgetSupplier, List<FormattedCharSequence> tooltip) {
		super(w, h, mutable, widgetSupplier, tooltip);
	}

	@Override
	public final void render(GuiGraphics guiGraphics) {
		super.render(guiGraphics);
	}

	public static final class Builder extends WidgetListElement.Builder<SimpleWidgetListElement, Builder> {

		@Override
		protected SimpleWidgetListElement buildInternal() {
			return new SimpleWidgetListElement(w, h, mutable, widgetSupplier, tooltip);
		}

		public static Builder begin() {
			return new Builder().setDefault();
		}

	}

}
