package xaero.pac.client.gui;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.Vec3i;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.function.BiFunction;

public class SimpleValueWidgetListElement<T, E extends SimpleValueWidgetListElement<T, E>> extends WidgetListElement<E> {

	protected T draftValue;

	protected SimpleValueWidgetListElement(T startValue, int w, int h, boolean mutable, BiFunction<E, Vec3i, AbstractWidget> widgetSupplier, List<FormattedCharSequence> tooltip) {
		super(w, h, mutable, widgetSupplier, tooltip);
		this.draftValue = startValue;
	}

	public T getDraftValue() {
		return draftValue;
	}

	public void setDraftValue(T draftValue) {
		this.draftValue = draftValue;
	}

	public static abstract class Builder<T, E extends SimpleValueWidgetListElement<T, E>, B extends Builder<T, E, B>> extends WidgetListElement.Builder<E, B> {

		protected T startValue;

		@Override
		public B setDefault() {
			super.setDefault();
			setStartValue(null);
			return self;
		}

		public B setStartValue(T startValue) {
			this.startValue = startValue;
			return self;
		}

		@Override
		public E build() {
			return super.build();
		}

	}

	public static final class Final<T> extends SimpleValueWidgetListElement<T, Final<T>>{

		private Final(T startValue, int w, int h, boolean mutable,
				BiFunction<Final<T>, Vec3i, AbstractWidget> widgetSupplier, List<FormattedCharSequence> tooltip) {
			super(startValue, w, h, mutable, widgetSupplier, tooltip);
		}

	}

	public static final class FinalBuilder<T> extends Builder<T, Final<T>, FinalBuilder<T>> {

		@Override
		protected Final<T> buildInternal() {
			return new Final<>(startValue, w, h, mutable, widgetSupplier, tooltip);
		}

		public static <T> FinalBuilder<T> begin() {
			return new FinalBuilder<T>().setDefault();
		}

	}

}
