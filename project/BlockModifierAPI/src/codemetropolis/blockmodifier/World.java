package codemetropolis.blockmodifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

import codemetropolis.blockmodifier.ext.NBTException;

public class World {
	
	public final String PATH;
	public final String NAME;
	public final int GROUNDLEVEL;
	
	private boolean groundBuilding = true;
	private int maxLoadedRegions = 1;
	private LinkedList<Region> loadedRegions = new LinkedList<Region>();
	
	public World(String path, int groundLevel) {
		
		this.PATH = path;
		this.GROUNDLEVEL = groundLevel;
		String[] splitPath = path.split("[/\\\\]");
		this.NAME = splitPath[splitPath.length - 1];
		Level level = new Level(this);
		level.writeToFile();
	}

    private Chunk setBlockInChunk(int x, int y, int z, int type, int data) {
        checkCoordinateYBoundaries(y);

        int regionX = getRegionCoordinate(x);
        int regionZ = getRegionCoordinate(z);

        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        int chunkIndexX = getChunkIndex(x);
        int chunkIndexZ = getChunkIndex(z);

        int blockX = getBlockCoordinate(x);
        int blockZ = getBlockCoordinate(z);

        Chunk activeChunk = locateChunk(chunkIndexX, chunkIndexZ, chunkX, chunkZ, regionX, regionZ, blockX, y, blockZ, type, data);

        int[] blockTypes = new int[]{63, 68, 54, 176, 52};
        Arrays.sort(blockTypes);
        if (Arrays.binarySearch(blockTypes, type) >= 0) {
            activeChunk.clearTileEntitiesAt(blockX, y, blockZ);
        }

        return activeChunk;
    }

    private int getRegionCoordinate(int a) {
        return a >> 9;
    }

    private int getChunkCoordinate(int a) {
        return a >> 4;
    }

    private int getChunkIndex(int a) {
        int chunkIndexA = (a % 512) >> 4;
        chunkIndexA = chunkIndexA < 0 ? chunkIndexA + 32 : chunkIndexA;

        return chunkIndexA;
    }

    private int getBlockCoordinate(int a) {
        int blockA = (a % 512) % 16;
        blockA = a < 0 ? blockA + 15 : blockA;

        return blockA;
    }

    private Chunk locateChunk(int xChunkIndex, int zChunkIndex, int chunkX, int chunkZ, int regionX, int regionZ,
                              int blockX, int y, int blockZ, int type, int data) {
        Region region = getRegion(regionX, regionZ);
        Chunk chunk = region.getChunk(xChunkIndex, zChunkIndex);
        if (chunk == null) {
            chunk = new Chunk(chunkX, chunkZ);
            if (groundBuilding)
                chunk.fill(GROUNDLEVEL, (byte) 2);
            region.setChunk(xChunkIndex, zChunkIndex, chunk);
        }

        chunk.setBlock(blockX, y, blockZ, (byte) type, (byte) data);

        return chunk;
    }

    /**
     * This method sets a sign block in the world at the specified coordinates with the specified data, and text.
     * This method is specifically for sign blocks.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     * @param z the z-coordinate of the block
     * @param type the type of the block
     * @param data the data of the block, example: the type of wood the sign is made of
     * @param text the text of the sign
     */
    private void setBlock(int x, int y, int z, int type, int data, String text) {
        Chunk currentChunk = setBlockInChunk(x, y, z, type, data);
        currentChunk.setSignText(x, y, z, text);
    }

    /**
     * This method sets a chest block in the world at the specified coordinates with the specified data, and items.
     * This method is specifically for chest blocks.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     * @param z the z-coordinate of the block
     * @param type the type of the block
     * @param data the data of the block, example: the orientation of the chest
     * @param items the items stored in the chest
     */
    private void setBlock(int x, int y, int z, int type, int data, int[] items) {
        Chunk currentChunk = setBlockInChunk(x, y, z, type, data);
        for (int i = 0; i < items.length; i += 2)
            currentChunk.addChestItem(x, y, z, items[i], items[i + 1]);
    }

    /**
     * This method sets a banner block in the world at the specified coordinates with the specified data, and items.
     * This method is specifically for banner blocks.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     * @param z the z-coordinate of the block
     * @param type the type of the block
     * @param data the data of the block, example: the orientation of the flag
     * @param color the color of the banner
     */
    private void setBlock(int x, int y, int z, int type, int data, int color) {
        Chunk currentChunk = setBlockInChunk(x, y, z, type, data);
        currentChunk.setBannerColor(x, y, z, color);
    }

    /**
     * This method sets a spawner block in the world at the specified coordinates with the specified data, and items.
     * This method is specifically for spawner blocks.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     * @param z the z-coordinate of the block
     * @param type the type of the block
     * @param data the data of the block, example: the orientation of the spawner
     * @param entityId the name of the entity that the spawner spawns
     * @param dangerLevel the danger level of the spawner for max spawn able entities
     */
    private void setBlock(int x, int y, int z, int type, int data, String entityId, Short dangerLevel) {
        Chunk currentChunk = setBlockInChunk(x, y, z, type, data);
        currentChunk.setSpawnerSubstance(x, y, z, entityId, dangerLevel);
    }

    private void checkCoordinateYBoundaries(int y) {
        if (y < 0 || y > 255) {
            try {
                throw new NBTException("Block's 'y' coordinate must be between 0 and 255");
            } catch (NBTException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method sets a block in the world at the specified coordinates with the specified type and data.
     * This is a general method that can be used for any type of block.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     * @param z the z-coordinate of the block
     * @param type the type of the block
     * @param data the data of the block
     */
    public void setBlock(int x, int y, int z, int type, int data) {
        setBlockInChunk(x, y, z, type, data);
    }

    public void setBlock(int x, int y, int z, int type) {
        setBlock(x, y, z, type, 0);
    }

    public void removeBlock(int x, int y, int z) {
        setBlock(x, y, z, 0);
    }

    public void setSignPost(int x, int y, int z, int data, String text) {
        setBlock(x, y, z, 63, data, text);
    }

    public void setSignPost(int x, int y, int z, String text) {
        setSignPost(x, y, z, 0, text);
    }

    public void setWallSign(int x, int y, int z, int data, String text) {
        setBlock(x, y, z, 68, data, text);
    }

    public void setWallSign(int x, int y, int z, String text) {
        setWallSign(x, y, z, 0, text);
    }

    /**
     * This method sets the spawner blocks that it receives, the type 52 is the code of spawners
     *
     * @param x x index of spawner
     * @param y y index of spawner
     * @param z z index of spawner
     * @param data data of block
     * @param entityId name of entity that spawner spawns, example: minecraft:zombie
     */
    public void setSpawner(int x, int y, int z, int data, String entityId, Short dangerLevel) {
        setBlock(x, y, z, 52, data, entityId, dangerLevel);
    }

    /**
     * This method receives the position data and monster entity of spawner blocks from codemetropolis and forwards them
     * to another method that setBlock by data's
     *
     * @param x x index of spawner
     * @param y y index of spawner
     * @param z z index of spawner
     * @param entityId name of entity that spawner spawns, example: minecraft:zombie
     */
    public void setSpawner(int x, int y, int z, String entityId, Short dangerLevel) {
        setSpawner(x, y, z, 0, entityId, dangerLevel);
    }

    public void setChest(int x, int y, int z, int data, int[] items) {
        setBlock(x, y, z, 54, data, items);
    }

    public void setChest(int x, int y, int z, int[] items) {
        setChest(x, y, z, 0, items);
    }

    public void setBanner(int x, int y, int z, int data, BannerColor color) {
        setBlock(x, y, z, 176, data, color.ordinal());
    }

	private Region getRegion(int x, int z) {
		
		for(Region r : loadedRegions) {
			if(r.getX() == x && r.getZ() == z) {
				return r;
			}
		}
		
		if(loadedRegions.size() >= maxLoadedRegions) {
			loadedRegions.removeFirst().writeToFile();
		} 
		
		Region result = Region.loadFromFile(x, z, this);
		loadedRegions.add(result);
		return result;
	}

	public void groundBuildingOn() {
		this.groundBuilding = true;
	}
	
	public void groundBuildingOff() {
		this.groundBuilding = false;
	}
	
	public void setMaximumNumberOfLoadedRegions(int max) {
		this.maxLoadedRegions = max;
	}

	public void finish() {
		for(Region r : loadedRegions) {
			r.writeToFile();
		}
		loadedRegions.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        File regionDirectory = new File(PATH + "/region");
        for(File f : regionDirectory.listFiles()) {
            if(f.getName().matches("r\\.-?[0-9]*\\.-?[0-9]*.mca")) {
                String[] parts = f.getName().split("\\.");
                Region region = getRegion(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                sb.append("** RegionFile: " + f.getName() + " **\n");
                sb.append(region.toString());
                sb.append("\n");
            }
        }    
        return sb.toString();
	}
	
	public void toNBTFile(String path) {
		try {
			PrintWriter writer = new PrintWriter(new File(path + ".nbt"));
			writer.println(this);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void toNBTFile() {
		toNBTFile(NAME);
	}
	
	public enum BannerColor {
		BLACK,
		RED,
		GREEN,
		BROWN,
		BLUE,
		PURPLE,
		TURQUOISE,
		LIGHT_GRAY,
		GRAY,
		PINK,
		LIGHT_GREEN,
		YELLOW,
		LIGHT_BLUE,
		LIGHT_PURPLE,
		ORANGE,
		WHITE;
	}
	
}
