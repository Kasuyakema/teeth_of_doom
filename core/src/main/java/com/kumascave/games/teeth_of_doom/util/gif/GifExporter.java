package com.kumascave.games.teeth_of_doom.util.gif;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.Deflater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;

public class GifExporter {

	public static void exportPng(String name) {
		Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		PixmapIO.writePNG(Gdx.files.external("/tmp/" + name + ".png"), pixmap, Deflater.DEFAULT_COMPRESSION, true);
		pixmap.dispose();
	}

	public static void exportGif() {
		new Thread(() -> {
			long start = System.currentTimeMillis();
			try (OutputStream outputStream = new FileOutputStream(Gdx.files.external("/tmp/mygif.gif").file())) {
				List<Pixmap> frames = FrameMemory.getFrames();

				int width = Gdx.graphics.getWidth();
				int height = Gdx.graphics.getHeight();

				AnimatedGifEncoder encoder = new AnimatedGifEncoder();
				encoder.setFrameRate(DynamicVariables.frameMemoryRate);
				encoder.setSize(width, height);
				encoder.setRepeat(0);

				encoder.start(outputStream);

				int[] pixels = new int[height * width];

				long prep = System.currentTimeMillis() - start;
				System.out.println("prep: " + prep);
				List<Long> ts = new ArrayList<>();
				for (Pixmap pixmap : frames) {
					long t = System.currentTimeMillis();
					for (int x = 0; x < width; ++x) {
						for (int y = 0; y < height; ++y) {

							int pixel = pixmap.getPixel(x, height - y - 1); // RGBA

							int r = pixel >> 24 & 0xFF;
							int g = pixel >> 16 & 0xFF;
							int b = pixel >> 8 & 0xFF;
							int a = pixel & 0xFF;

							pixel = (a << 24) + (r << 16) + (g << 8) + b; // ARGB

							pixels[y * width + x] = pixel;
						}
					}

					BufferedImage image = new BufferedImage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
							BufferedImage.TYPE_INT_ARGB);
					final int[] a = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
					System.arraycopy(pixels, 0, a, 0, pixels.length);

					encoder.addFrame(image);
					ts.add(System.currentTimeMillis() - t);
				}

				long enc = System.currentTimeMillis();
				encoder.finish();
				System.out.println("enc: " + (System.currentTimeMillis() - enc));
				Double avg = ts.stream().collect(Collectors.averagingLong(x -> x));
				System.out.println("Gif done in " + (System.currentTimeMillis() - start) + "ms " + frames.size()
						+ " frames at avg " + avg.intValue() + "ms");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

}
