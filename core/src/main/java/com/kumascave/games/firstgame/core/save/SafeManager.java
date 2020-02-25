package com.kumascave.games.firstgame.core.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.kumascave.games.firstgame.core.world.CPos;
import com.kumascave.games.firstgame.core.world.Chunk;
import com.kumascave.games.firstgame.core.world.GPos;
import com.kumascave.games.firstgame.core.world.tiles.Floor;
import com.kumascave.games.firstgame.core.world.tiles.Tile;
import com.kumascave.games.firstgame.core.world.tiles.Wall;

import de.javakaffee.kryoserializers.FieldAnnotationAwareSerializer;

public class SafeManager {

	public static final String SAFE_FILE_BASE_PATH = "./";
	public static final String CHUNK_BASE_PATH = SAFE_FILE_BASE_PATH + "map/chunks/";

	public static Kryo getSerializer() {
		Kryo kryo = new Kryo();
		kryo.setReferences(true);

		final SerializerFactory<?> includeSerializerFactory = new FieldAnnotationAwareSerializer.Factory(
				Arrays.<Class<? extends Annotation>>asList(Include.class), false);

		kryo.addDefaultSerializer(Wall.class, includeSerializerFactory);
		kryo.addDefaultSerializer(Floor.class, includeSerializerFactory);
		kryo.addDefaultSerializer(Chunk.class, includeSerializerFactory);

		kryo.register(Wall.class);
		kryo.register(Floor.class);
		kryo.register(Chunk.class);
		kryo.register(CPos.class);
		kryo.register(GPos.class);
		kryo.register(Tile[][].class);
		kryo.register(Tile[].class);

		return kryo;
	}

	public static boolean chunkExists(CPos pos) {
		return new File(CHUNK_BASE_PATH + pos.toString()).canRead();
	}

	// public static Map<CPos, Chunk> readChunks() {
	// Map<CPos, Chunk> chunks = new HashMap<CPos, Chunk>();
	// try (FileInputStream inStream = new FileInputStream("map")) {
	// try (Input input = new Input(inStream)) {
	// chunks = getSerializer().readObject(input, chunks.getClass());
	// return chunks;
	// }
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return chunks;
	// }

	public static Chunk readChunk(CPos pos) throws IOException {
		try (FileInputStream inStream = new FileInputStream(CHUNK_BASE_PATH + pos.toString())) {
			try (Input input = new Input(inStream)) {
				return getSerializer().readObject(input, Chunk.class);
			}
		} catch (IOException e) {
			throw new IOException("cnable to read Chunk. File missing or corrupted", e);
		}
	}

	public static void writeChunks(Map<CPos, Chunk> chunks) throws IOException {
		for (Chunk c : chunks.values()) {
			writeChunk(c);
		}
	}

	public static void writeChunk(Chunk chunk) throws IOException {
		try (FileOutputStream outStream = new FileOutputStream(CHUNK_BASE_PATH + chunk.getChunkPos().toString())) {
			try (Output output = new Output(outStream)) {
				getSerializer().writeObject(output, chunk);
			}
		} catch (IOException e) {
			throw new IOException("unable to write chunk", e);
		}
	}

}
