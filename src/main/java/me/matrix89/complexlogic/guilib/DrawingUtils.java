package me.matrix89.complexlogic.guilib;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class DrawingUtils {
	
	public static void drawRect(Rectangle rectangle, int color){
		drawRect(rectangle.getXStart(), rectangle.getYStart(), rectangle.getXEnd(), rectangle.getYEnd(), color);
	}
	
	public static void drawRect(int left, int top, int right, int bottom, int color)
	{
		if (left < right)
		{
			int i = left;
			left = right;
			right = i;
		}
		
		if (top < bottom)
		{
			int j = top;
			top = bottom;
			bottom = j;
		}
		
		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
		bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
		bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
		bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
}
