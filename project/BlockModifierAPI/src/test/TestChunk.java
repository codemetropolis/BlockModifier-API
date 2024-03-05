package test;

import codemetropolis.blockmodifier.Chunk;
import codemetropolis.blockmodifier.ext.NBTTag;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class TestChunk {

    private ChunkEntity chunk;

    @Before
    public void setit() {
        chunk = new ChunkEntity(10, 5);
    }

    static class ChunkEntity extends Chunk {

        public ChunkEntity(int x, int z) {
            super(x, z);
        }

    }

    @Test()
    public void testConstructor() {
        assertEquals("x érték nem lett beállítva megfelelően!", 10, chunk.tag.getSubtagByName("Level").getSubtagByName("xPos").getValue());
        assertEquals("z érték nem lett beállítva megfelelően!", 5, chunk.tag.getSubtagByName("Level").getSubtagByName("zPos").getValue());
        assertEquals("A tile entites nem üres!", 0, getTileEntities(chunk).getSubtags().length);
    }

    NBTTag getTileEntities(ChunkEntity ch) {
        return ch.tag.getSubtagByName("Level").getSubtagByName("TileEntities");
    }

    @Test()
    public void testSetSpawnerContentAdded() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A függvény nem adja hozzá a spawnert a tileEntities-hez", 1, getTileEntities(chunk).getSubtags().length);
    }

    @Test()
    public void testSetSpawnerContentSpawnerType() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawnernek összetett típusúnak kell lennie!", NBTTag.Type.TAG_Compound, getTileEntities(chunk).getSubtags()[0].getType());
    }

    @Test()
    public void testSetSpawnerContentSpawnerXCoordiante() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawner x típusa nem jól állítódott be!", NBTTag.Type.TAG_Int, getTileEntities(chunk).getSubtags()[0].getSubtagByName("x").getType());
        assertEquals("A spawner x értéke nem jól állítódott be!", 8, getTileEntities(chunk).getSubtags()[0].getSubtagByName("x").getValue());
    }

    @Test()
    public void testSetSpawnerContentSpawnerYCoordiante() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawner y típusa nem jól állítódott be!", NBTTag.Type.TAG_Int, getTileEntities(chunk).getSubtags()[0].getSubtagByName("y").getType());
        assertEquals("A spawner y értéke nem jól állítódott be!", 12, getTileEntities(chunk).getSubtags()[0].getSubtagByName("y").getValue());
    }

    @Test()
    public void testSetSpawnerContentSpawnerZCoordiante() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawner z típusa nem jól állítódott be!", NBTTag.Type.TAG_Int, getTileEntities(chunk).getSubtags()[0].getSubtagByName("z").getType());
        assertEquals("A spawner z értéke nem jól állítódott be!", 14, getTileEntities(chunk).getSubtags()[0].getSubtagByName("z").getValue());
    }

    @Test()
    public void testSetSpawnerContentSpawnerIdTag() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawner id-jának típusa nem jól állítódott be!", NBTTag.Type.TAG_String, getTileEntities(chunk).getSubtags()[0].getSubtagByName("id").getType());
        assertEquals("A spawner id-jának értéke nem jól állítódott be!", "MobSpawner", getTileEntities(chunk).getSubtags()[0].getSubtagByName("id").getValue());
    }

    @Test()
    public void testSetSpawnerContentSpawnerSpawnDataTag() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawner SpawnData-jának összetett típusúnak kell lennie!", NBTTag.Type.TAG_Compound, getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getType());
        assertNotNull("A spawner SpawnData-ja nem tartalmazza az entitást!", getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getSubtagByName("entity"));
    }

    @Test()
    public void testSetSpawnerContentSpawnerSpawnDataEntityTag() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawner SpawnData-jának entitása nem összetett típusú!", NBTTag.Type.TAG_Compound, getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getSubtagByName("entity").getType());
        assertNotNull("A spawner SpawnData-jának entitása nem tartalmazza az entitás id-jét!", getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getSubtagByName("entity").getSubtagByName("id"));
    }

    @Test()
    public void testSetSpawnerContentSpawnerSpawnDataEntityIdTag() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);

        assertEquals("A spawner SpawnData-jának entitásában az id típúsa nem megfelelő!", NBTTag.Type.TAG_String, getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getSubtagByName("entity").getSubtagByName("id").getType());
        assertEquals("A spawner SpawnData-jának entitásában az id értéke nem megfelelő!", "Zombie", getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getSubtagByName("entity").getSubtagByName("id").getValue());
    }

    @Test()
    public void testSetSpawnerContentSpawnerMultipleSpawnerDifferentCoordinates() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 3);
        chunk.setSpawnerContent(10, 20, 30, "minecraft:spider", (short) 8);

        assertEquals("A tile entites tömb hossza nem nőtt megfelelően hozzáadás után", 2, getTileEntities(chunk).getSubtags().length);
        assertEquals("A spawner SpawnData-jának entitásában az id értéke megváltozott, pedig nem kellett volna!", "Zombie", getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getSubtagByName("entity").getSubtagByName("id").getValue());
        assertEquals("A spawner SpawnData-jának entitásában az id értéke nem megfelelően állítódott be!", "Spider", getTileEntities(chunk).getSubtags()[1].getSubtagByName("SpawnData").getSubtagByName("entity").getSubtagByName("id").getValue());

    }

    @Test()
    public void testSetSpawnerContentSpawnerMultipleSpawnerSameCoordinates() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 8);
        chunk.setSpawnerContent(8, 12, 14, "minecraft:spider", (short) 3);

        assertEquals("A tile entites tömb mérete nőtt pedig nem kellett volna", 1, getTileEntities(chunk).getSubtags().length);
        assertEquals("A spawner SpawnData-jának entitásában az id értéke nem változott, pedig kellett volna!", "Spider", getTileEntities(chunk).getSubtags()[0].getSubtagByName("SpawnData").getSubtagByName("entity").getSubtagByName("id").getValue());

    }

    @Test()
    public void testSetSpawnerContentMaxNearbyEntitiesTagWithCorrectValue() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 1);

        assertEquals("A spawner MaxNearbyEntities-ének típusa nem jól állítódott be!", NBTTag.Type.TAG_Short, getTileEntities(chunk).getSubtags()[0].getSubtagByName("MaxNearbyEntities").getType());
        assertEquals("A spawner MaxNearbyEntities-ének értéke nem jól állítódott be!", (short) 1, getTileEntities(chunk).getSubtags()[0].getSubtagByName("MaxNearbyEntities").getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSpawnerContentMaxNearbyEntitiesTagWithNegative() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) -1);

        assertEquals("A spawner MaxNearbyEntities-ének értéke nem jól állítódott be!", (short) 10, getTileEntities(chunk).getSubtags()[0].getSubtagByName("MaxNearbyEntities").getValue());
    }

    @Test()
    public void testSetSpawnerContentMaxNearbyEntitiesTagWithTooLargeValue() {
        chunk.setSpawnerContent(8, 12, 14, "minecraft:zombie", (short) 22);

        assertEquals("A spawner MaxNearbyEntities-ének típusa nem jól állítódott be!", NBTTag.Type.TAG_Short, getTileEntities(chunk).getSubtags()[0].getSubtagByName("MaxNearbyEntities").getType());
        assertEquals("A spawner MaxNearbyEntities-ének értéke nem jól állítódott be!", (short) 10, getTileEntities(chunk).getSubtags()[0].getSubtagByName("MaxNearbyEntities").getValue());
    }

    @Test
    public void testSetSignTextWithLongString() {
        String longText = "Ez egy hosszú szöveg ami négy részben kellene legyen.";
        chunk.setSignText(1, 2, 3, longText);

        assertEquals("Sign-nak léteznie kellene az adott koordinátákon", 1, getTileEntities(chunk).getSubtags().length);
        assertEquals("Első sor szövegének egyeznie kellene", "Ez egy hosszú ", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text1").getValue());
        assertEquals("Második sor szövegének egyeznie kellene", "szöveg ami nég", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text2").getValue());
        assertEquals("Harmadik sor szövegének egyeznie kellene", "y részben kell", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text3").getValue());
        assertEquals("Negyedik sor szövegének egyeznie kellene", "ene legyen.", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text4").getValue());
    }

    @Test
    public void testSetSignTextWithEmptyString() {
        chunk.setSignText(1, 2, 3, "");

        assertEquals("Sign-nak léteznie kellene az adott koordinátákon", 1, getTileEntities(chunk).getSubtags().length);
        assertEquals("Text1 üres kellene legyen", "", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text1").getValue());
        assertEquals("Text2 üres kellene legyen", "", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text2").getValue());
        assertEquals("Text3 üres kellene legyen", "", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text3").getValue());
        assertEquals("Text4 üres kellene legyen", "", getTileEntities(chunk).getSubtags()[0].getSubtagByName("Text4").getValue());
    }

}
