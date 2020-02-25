package com.kumascave.games.firstgame.atest;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.kumascave.games.firstgame.core.save.Include;
import com.kumascave.games.firstgame.core.world.tiles.Wall;

import de.javakaffee.kryoserializers.FieldAnnotationAwareSerializer;

public class SimpleMain {
	public static void main(String[] args) {
		final Kryo kryo = new Kryo();

		final SerializerFactory<?> disregardingSerializerFactory = new FieldAnnotationAwareSerializer.Factory(
				Arrays.<Class<? extends Annotation>>asList(Include.class), false);
		kryo.addDefaultSerializer(Wall.class, disregardingSerializerFactory);
		kryo.register(Wall.class);

		final byte[] buffer = new byte[1024];

		final Wall outputBean = new Wall(0, 0);
		// outputBean.initStatus();
		final Output output = new Output(buffer);
		kryo.writeObject(output, outputBean);

		final Input input = new Input(buffer);
		final Wall inputBean = kryo.readObject(input, Wall.class);
		System.out.println(inputBean.getX());
	}
}