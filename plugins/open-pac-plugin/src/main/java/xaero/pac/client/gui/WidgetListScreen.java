package xaero.pac.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public abstract class WidgetListScreen extends XPACScreen {

	private final static int ROW_HEIGHT = 24;
	private final static int PER_PAGE = 12;

	private int page;
	private final int pageCount;
	protected final List<WidgetListElement<?>> elements;
	private final List<EditBox> tickableBoxes;
	private Component displayedTitle;

	private Button nextButton;
	private Button prevButton;

	protected WidgetListScreen(List<WidgetListElement<?>> elements, List<EditBox> tickableBoxes, Screen escape, Screen parent, Component p_96550_) {
		super(escape, parent, p_96550_);
		this.elements = elements;
		this.tickableBoxes = tickableBoxes;
		this.pageCount = (elements.size() + PER_PAGE - 1) / PER_PAGE;
	}

	@Override
	protected void init() {
		super.init();
		int xAnchor = width / 2;
		int yAnchor = height / 7;
		int startIndex = getStartIndex();
		int endIndex = getEndIndex();
		tickableBoxes.clear();
		for(int index = startIndex; index < endIndex; index++) {
			int indexOff = index - startIndex;
			int x = xAnchor - 205 + 210 * (indexOff / 6);
			int y = yAnchor + (indexOff % 6) * ROW_HEIGHT;
			elements.get(index).screenInit(x, y, this, tickableBoxes);
		}
		addRenderableWidget(Button.builder(Component.translatable("gui.xaero_pac_back"), this::onBackButton).bounds(xAnchor - 100, this.height / 6 + 168, 200, 20).build());

		prevButton = Button.builder(Component.translatable("gui.xaero_pac_previous", new Object[0]), this::onPrevButton).bounds(this.width / 2 - 205, yAnchor + 144, 75, 20).build();
		nextButton = Button.builder(Component.translatable("gui.xaero_pac_next", new Object[0]), this::onNextButton).bounds(this.width / 2 + 131, yAnchor + 144, 75, 20).build();
		if(pageCount > 1) {
			this.addRenderableWidget(prevButton);
			this.addRenderableWidget(nextButton);
			prevButton.active = page > 0;
			nextButton.active = page < pageCount - 1;
		}
		displayedTitle = Component.literal("");
		displayedTitle.getSiblings().add(title);
		displayedTitle.getSiblings().add(Component.literal(" (" + (page + 1) + "/" + pageCount + ")"));
	}

	private int getStartIndex() {
		return page * PER_PAGE;
	}

	private int getEndIndex() {
		return Math.min((page + 1) * PER_PAGE, elements.size());
	}

	@Override
	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		return super.addRenderableWidget(widget);
	}

	@Override
	protected <T extends Renderable> T addRenderableOnly(T widget) {
		return super.addRenderableOnly(widget);
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T p_96625_) {
		return super.addWidget(p_96625_);
	}

	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
		super.renderBackground(guiGraphics, mouseX, mouseY, partial);
		guiGraphics.drawCenteredString(font, displayedTitle, width / 2, 16, -1);
	}

	@Override
	protected void renderPreDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
		super.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
		int startIndex = getStartIndex();
		int endIndex = getEndIndex();
		WidgetListElement<?> tooltipElement = null;
		for (int index = startIndex; index < endIndex; index++) {
			WidgetListElement<?> e = elements.get(index);
			e.render(guiGraphics);
			if (e.isOver(mouseX, mouseY))
				tooltipElement = e;
		}
		if (openDropdown == null && tooltipElement != null) {
			List<FormattedCharSequence> tooltip = tooltipElement.getTooltip();
			if (tooltip != null)
				guiGraphics.renderTooltip(font, tooltip, mouseX, mouseY + ROW_HEIGHT + 10);
		}
	}

	@Override
	public void tick() {
		super.tick();
	}

	private void onPrevButton(Button b) {
		page--;
		init(minecraft, width, height);
	}

	private void onNextButton(Button b) {
		page++;
		init(minecraft, width, height);
	}

	protected void onBackButton(Button b) {
		goBack();
	}

}
