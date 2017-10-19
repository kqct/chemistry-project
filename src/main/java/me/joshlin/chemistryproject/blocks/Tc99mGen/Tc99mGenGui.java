package me.joshlin.chemistryproject.blocks.Tc99mGen;

import me.joshlin.chemistryproject.ChemistryProject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Tc99mGenGui extends GuiContainer {

    // Background Image Resource Location
    private static final ResourceLocation texture = new ResourceLocation(ChemistryProject.modId,"textures/gui/tc99m_bg.png");
    private Tc99mGenTileEntity tileEntity;

    public Tc99mGenGui(InventoryPlayer invPlayer, Tc99mGenTileEntity tc99mGenTileEntity) {
        super(new Tc99mGenContainer(invPlayer, tc99mGenTileEntity));

        xSize = 176;
        ySize = 132;

        this.tileEntity = tc99mGenTileEntity;
    }

    // some [x,y] coordinates of graphical elements
    final int PROCESS_BAR_XPOS = 47;
    final int PROCESS_BAR_YPOS = 19;
    final int PROCESS_BAR_ICON_U = 0;   // texture position of white arrow icon
    final int PROCESS_BAR_ICON_V = 132;
    final int PROCESS_BAR_WIDTH = 80;
    final int PROCESS_BAR_HEIGHT = 17;

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        double processProgress = tileEntity.fractionOfProcessingTimeComplete();


        drawTexturedModalRect(guiLeft + PROCESS_BAR_XPOS, guiTop + PROCESS_BAR_YPOS, PROCESS_BAR_ICON_U, PROCESS_BAR_ICON_V, (int)(processProgress * PROCESS_BAR_WIDTH), PROCESS_BAR_HEIGHT);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int LABEL_XPOS = 5;
        final int LABEL_YPOS = 5;
        fontRendererObj.drawString(tileEntity.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());

        List<String> hoveringText = new ArrayList<String>();

        // if mouse over progress bar add hovering text
        if (isInRect(guiLeft + PROCESS_BAR_XPOS, guiTop + PROCESS_BAR_YPOS, PROCESS_BAR_WIDTH, PROCESS_BAR_HEIGHT, mouseX, mouseY)) {
            hoveringText.add("Progress:");
            int processPercentage = (int)(tileEntity.fractionOfProcessingTimeComplete() * 100);
            hoveringText.add(processPercentage + "%");
        }

        // if hoveringText not empty draw text
        if (!hoveringText.isEmpty()) {
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
        }
    }

    // Returns true if the given x,y coordinates are within the given rectangle
    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
        return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
    }
}


// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/GuiInventoryFurnace.java