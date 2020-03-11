/*******************************************************
 * Copyright (C) 2015 Mirco Timmermann - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mirco Timmermann <mtimmermann@gmx.de>, December 2016
 *
 *******************************************************/
package com.kumascave.games.firstgame.atest;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


/*
 * command pattern for organizing shape render code into objects for deferred rendering.
 */
public class Graphics {
	public class Line {
		public Vector2 pointA = new Vector2();
		public Vector2 pointB = new Vector2();


		public Line() { }

		public Line(Vector2 pointA, Vector2 pointB) {
			Set(pointA, pointB);
		}

		public Line(float xA, float yA, float xB, float yB) {
			Set(xA, yA, xB, yB);
		}

		public void SetA(Vector2 pointA) {
			this.pointA.set(pointA);
		}

		public void SetB(Vector2 pointB) {
			this.pointB.set(pointB);
		}

		public void SetA(float xA, float yA) {
			this.pointA.set(xA, yA);
		}

		public void SetB(float xB, float yB) {
			this.pointB.set(xB, yB);
		}

		public void Set(Vector2 pointA, Vector2 pointB) {
			this.pointA.set(pointA);
			this.pointB.set(pointB);
		}

		public void Set(float xA, float yA, float xB, float yB) {
			this.pointA.set(xA, yA);
			this.pointB.set(xB, yB);
		}
	}

	private List<ICommand> _commands = null;

	public interface ICommand {
		public void execute(ShapeRenderer ctx);
	}

	public class Arc implements ICommand {
		public float _x;
		public float _y;
		public float _radius;
		public float _start;
		public float _degrees;
		private int _segments = 0;

		public Arc(float x, float y, float radius, float start, float degrees) {
			_x = x;
			_y = x;
			_radius = radius;
			_start = start;
			_degrees = degrees;
		}

		public Arc(float x, float y, float radius, float start, float degrees, int segments) {
			_x = x;
			_y = x;
			_radius = radius;
			_start = start;
			_degrees = degrees;
			_segments = segments;
		}

		@Override
		public void execute(ShapeRenderer ctx) {
			if(_segments > 0) {
				ctx.arc(_x, _y, _radius, _start, _degrees, _segments);
			} else {
				ctx.arc(_x, _y, _radius, _start, _degrees);
			}

		}
	};

	public class Circle1 extends Circle implements ICommand {
		public Circle1(float x, float y, float r) {
			super(x, y, r);
		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.circle(x, y, radius);
		}
	};

	public class Rect extends Rectangle implements ICommand {
		public Rect(float x, float y, float w, float h) {
			super(x, y, w, h);
		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.rect(x, y, width, height);
		}
	};

	/*
	public class IgnoreBegin extends Boolean implements ICommand {
		public IgnoreBegin() {
			super(false);
		}

		public void execute(ShapeRenderer ctx) {
			ctx
		}
	};
	 */

	public class Poly extends Polygon implements ICommand {
		public Poly(float[] vertices) {
			super(vertices);
		}

		public Poly(Polygon polygon) {
			this(polygon.getVertices());
		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.polygon(this.getTransformedVertices());
		}
	};

	public class LineO extends Line implements ICommand {
		public LineO(float xA, float yA, float xB, float yB) {
			super(xA, yA, xB, yB);
		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.line(pointA.x, pointA.y, pointB.x, pointB.y);
		}
	};

	public class LineWidth implements ICommand {
		private float _width;

		public LineWidth(float width) {
			_width = width;
		}

		@Override
		public void execute(ShapeRenderer ctx) {
			Gdx.gl.glLineWidth(_width);
		}
	};

	public class BeginFilled implements ICommand {
		public BeginFilled() {

		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.begin(ShapeType.Filled);
		}
	};

	public class BeginLine implements ICommand {
		public BeginLine() {

		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.begin(ShapeType.Line);
		}
	};

	public class BeginPoint implements ICommand {
		public BeginPoint() {

		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.begin(ShapeType.Point);
		}
	};

	public class End implements ICommand {
		public End() {

		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.end();
		}
	};

	public class SetColor extends Color implements ICommand {
		public SetColor(Color color) {
			super(color);
		}

		@Override
		public void execute(ShapeRenderer ctx) {
			ctx.setColor(this);
		}
	};

	public Graphics() {
		_commands = new ArrayList<ICommand>();
	}

	public void AddCommand(ICommand command) {
		_commands.add(command);
	}

	/*
	void strokeRect(float x, float y, float w, float h) {
		AddCommand(new StrokeRect(x, y, w, h));
	}
	 */
	//////////

	public class EnableBlend implements ICommand {
		public EnableBlend() {

		}

		@Override
		public void execute(ShapeRenderer ctx) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			// Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
	};

	public class DisableBlend implements ICommand {
		public DisableBlend() {

		}

		@Override
		public void execute(ShapeRenderer ctx) {
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	};

	public Color setColor(Color color) {
		SetColor setColor = new SetColor(color);
		AddCommand(setColor);

		return setColor;
	}

	public void lineWidth(float width) {
		AddCommand(new LineWidth(width));
	}

	public Arc arc(float x, float y, float radius, float start, float degrees) {
		return arc(x, y, radius, start, degrees, 0);
	}

	public Arc arc(float x, float y, float radius, float start, float degrees, int segments) {
		Arc arc = new Arc(x, y, radius, start, degrees, segments);
		AddCommand(arc);

		return arc;
	}

	/*
	public Boolean ignoreBegin() {
		Boolean bool = new Boolean(false);
		Rect rect = new Rect(x, y, width, height);
		AddCommand(rect);

		return bool;
	}
	 */

	public Circle1 circle(float x, float y, float r) {
		Circle1 circ = new Circle1(x, y, r);
		AddCommand(circ);

		return circ;
	}

	public Rect rect(float x, float y, float width, float height) {
		Rect rect = new Rect(x, y, width, height);
		AddCommand(rect);

		return rect;
	}

	public Polygon polygon(Polygon polygon) {
		Poly poly = new Poly(polygon);
		AddCommand(poly);

		return poly;
	}

	public Line line(float x1, float y1, float x2, float y2) {
		LineO lineO = new LineO(x1, y1, x2, y2);
		AddCommand(lineO);

		return lineO;
	}

	public Polygon polygon(float[] vertices) {
		Poly poly = new Poly(vertices);
		AddCommand(poly);

		return poly;
	}

	public void beginFilled() {
		AddCommand(new BeginFilled());
	}

	public void beginLine() {
		AddCommand(new BeginLine());
	}

	public void beginPoint() {
		AddCommand(new BeginPoint());
	}

	public void end() {
		AddCommand(new End());
	}

	public void enableBlend() {
		AddCommand(new EnableBlend());
	}

	public void disableBlend() {
		AddCommand(new DisableBlend());
	}

	//////////

	public void execute(ShapeRenderer ctx) {
		for(int i = 0; i < _commands.size(); i++) {
			_commands.get(i).execute(ctx); //_ctx);
		}
	}
}
