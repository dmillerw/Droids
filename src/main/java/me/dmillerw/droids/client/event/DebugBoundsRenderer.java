package me.dmillerw.droids.client.event;

import me.dmillerw.droids.client.network.ClientSyncHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

public class DebugBoundsRenderer {

    public static class Box {
        
        public BlockPos start;
        public BlockPos end;

        public AxisAlignedBB aabb;

        public boolean connected;
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) {
            return;
        }
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
        Entity entity = mc.getRenderViewEntity();

        double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks();
        double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks();
        double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks();

        GlStateManager.translate(-posX, -posY, -posZ);        
        GlStateManager.glLineWidth(2.5F);

        World world = entity.world;
        int x1 = (int) entity.posX;
        int z1 = (int) entity.posZ;

        Chunk chunks[] = new Chunk[9];

        chunks[4] = world.getChunkFromBlockCoords(new BlockPos(x1, 1, z1));
        int cX = chunks[4].x;
        int cZ = chunks[4].z;

        chunks[0] = world.getChunkFromChunkCoords(cX - 1, cZ - 1);
        chunks[1] = world.getChunkFromChunkCoords(cX, cZ - 1);
        chunks[2] = world.getChunkFromChunkCoords(cX + 1, cZ - 1);

        chunks[3] = world.getChunkFromChunkCoords(cX - 1, cZ);
        chunks[5] = world.getChunkFromChunkCoords(cX + 1, cZ);

        chunks[6] = world.getChunkFromChunkCoords(cX - 1, cZ + 1);
        chunks[7] = world.getChunkFromChunkCoords(cX, cZ + 1);
        chunks[8] = world.getChunkFromChunkCoords(cX + 1, cZ + 1);

        ArrayDeque<Box> boxes = new ArrayDeque<>();
        for (int c = 0; c < 9; ++c) {
            for (TileEntity obj : chunks[c].getTileEntityMap().values()) {

            }
        }

        ClientSyncHandler.INSTANCE.getClaimedBlocks().forEach((pos -> {
            Box box = new Box();
            box.start = pos;
            box.end = pos.add(1, 1, 1);
            box.connected = false;
            boxes.add(box);
        }));

        List<Entity> entities = world.loadedEntityList.stream()
                .filter(ClientSyncHandler.INSTANCE::isClaimed)
                .collect(Collectors.toList());

        entities.forEach((e) -> {
            Box box = new Box();
            AxisAlignedBB aabb = new AxisAlignedBB(e.getPosition(), e.getPosition().add(1, 1, 1));
            box.aabb = aabb;
            boxes.add(box);
        });

        Box renderPair;
        while (boxes.size() > 0) {
            renderPair = boxes.pop();

            if (renderPair.aabb != null) {
                RenderGlobal.drawBoundingBox(
                        renderPair.aabb.minX,
                        renderPair.aabb.minY,
                        renderPair.aabb.minZ,
                        renderPair.aabb.maxX,
                        renderPair.aabb.maxY,
                        renderPair.aabb.maxZ,
                        renderPair.connected ? 0 : 1,
                        renderPair.connected ? 1 : 0,
                        0,
                        1);
            } else {
                RenderGlobal.drawBoundingBox(
                        renderPair.start.getX(),
                        renderPair.start.getY(),
                        renderPair.start.getZ(),
                        renderPair.end.getX(),
                        renderPair.end.getY(),
                        renderPair.end.getZ(),
                        renderPair.connected ? 0 : 1,
                        renderPair.connected ? 1 : 0,
                        0,
                        1);
            }
        }

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GL11.glPopAttrib();
		GlStateManager.popMatrix();
    }
}