package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiDialer extends GuiContainer
{
    private TileEntityDialer dialer;
	
	public GuiDialer(InventoryPlayer par1InventoryPlayer, TileEntityDialer par2dialer)
    {
		super(new ContainerDialer(par1InventoryPlayer, par2dialer));
        this.dialer = par2dialer;
    }
	
	public void initGui()
	{
		controlList.clear();
	}
	
	protected void keyTyped(char par1, int par2)
	{
		if (par1 >= '0' && par1 <= '9')
		{
			dialer.Dial(par1 - '0');
		}
		else if (par1 == '*')
		{
			dialer.Dial(10);
		}
		else if (par1 == '#' || par1 == 13)
		{
			dialer.Dial(11);
		}
		else
		{
			super.keyTyped(par1, par2);
		}
	}
	
	protected void mouseClicked(int x, int y, int z) // Z?
	{
		super.mouseClicked(x, y, z);
		int w = 123, h = 185;
		int xpos = (width - w) / 2;
		int ypos = (height - h) / 2;
		
		x -= xpos;
		y -= ypos;
		
		for(int row = 0; row < 4; ++row)
		{
			
			// This row?
			if (!(y >= 38 + row * 30 && y < 63 + row * 30)) continue;
			
			// first column
			if (x >= 23 && x < 48)
			{
				if (row == 3)
				{
					dialer.Dial(10);
				}
				else
				{
					dialer.Dial(row * 3 + 1);
				}
				return;
			}
			
			// second column
			if (x >= 54 && x < 78)
			{
				if (row == 3)
				{
					dialer.Dial(0);
				}
				else
				{
					dialer.Dial(row * 3 + 2);
				}
				return;
			}
			
			// third column
			if (x >= 83 && x < 109)
			{
				if (row == 3)
				{
					dialer.Dial(11);
				}
				else
				{
					dialer.Dial(row * 3 + 3);
				}
				return;
			}
		
		}
	
	}
	
	/**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {	
		int tex = mc.renderEngine.getTexture("/Endgame/dialer-ui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
		mc.renderEngine.bindTexture(tex);
		int w = 123, h = 185;
		int xpos = (width - w) / 2;
		int ypos = (height - h) / 2;
		
		drawTexturedModalRect(xpos, ypos, 0, 0, w, h);
		
		if (dialer != null)
		{
			String addr = dialer.getDisplay();
		
			int nX = 101, nY = 24;
			for(int i = addr.length() - 1; i >= 0; --i)
			{
				int idx = addr.charAt(i) - '0';
				drawTexturedModalRect(xpos+nX, ypos+nY, 176+idx*7, 0, 7, 8);
				nX -= 7;
			}
		}
		
		GL11.glDisable(GL11.GL_BLEND);
    }
}