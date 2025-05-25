package onlysole.fluxnetworks.client.gui.button;

import onlysole.fluxnetworks.client.gui.basic.GuiButtonCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class PageLabelButton extends GuiButtonCore {

    public int page, pages, color;
    public double currentLeft, singleWidth;
    private int guiLeft, guiTop;
    public int hoveredPage, showTick;

    public PageLabelButton(int x, int y, int page, int pages, int color, int guiLeft, int guiTop) {
        super(x, y, 148, 4, 0);
        this.color = color;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
        refreshPages(page, pages);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, int guiLeft, int guiTop) {
        GlStateManager.pushMatrix();

        drawRect(x, y, x + width, y + 1, 0x80FFFFFF);
        drawRect(x, y + 3, x + width, y + height, 0x80FFFFFF);
        drawRect(currentLeft, y + 1, currentLeft + singleWidth, y + 3, color | 0xF0000000);

        boolean b = isMouseHovered(mc, mouseX - guiLeft, mouseY - guiTop);

        if(b) {
            this.hoveredPage = (int) ((mouseX - guiLeft - x - 1) / singleWidth) + 1;
            if(hoveredPage != page) {
                double c = (hoveredPage - 1) * singleWidth + x + 1;
                drawRect(c, y + 1, c + singleWidth, y + 3, color | 0x60000000);
            }
            drawCenteredString(mc.fontRenderer, hoveredPage + " / " + pages, 88, y + 6, color);
        } else if(showTick > 0) {

            int alpha = Math.min(255, showTick * 32);

            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();

            drawCenteredString(mc.fontRenderer, page + " / " + pages, 88, y + 6, color | alpha << 24);

            GlStateManager.disableBlend();

            showTick--;
        }
        drawRect(x + 1, y + 1, x + width - 1, y + 3, 0x20000000);

        GlStateManager.popMatrix();
    }

    @Override
    public boolean isMouseHovered(Minecraft mc, int mouseX, int mouseY) {
        return mouseX >= this.x + 1 && mouseY >= this.y && mouseX < this.x + this.width - 1 && mouseY < this.y + this.height;
    }

    public void refreshPages(int page, int pages) {
        this.page = page;
        this.pages = pages;
        singleWidth = (double) 146 / pages;
        currentLeft = (page - 1) * singleWidth + x + 1;
        showTick = 40;
    }

    @Override
    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawString(text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
    }
}
