// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * PolyBezierTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: PolyBezierTo.java,v 1.5 2009-08-17 21:44:44 murkle Exp $
 */
public class PolyBezierTo extends EMFTag {

	private Rectangle bounds;

	private int numberOfPoints;

	private Point[] points;

	public PolyBezierTo() {
		super(5, 1);
	}

	public PolyBezierTo(Rectangle bounds, int numberOfPoints, Point[] points) {
		this();
		this.bounds = bounds;
		this.numberOfPoints = numberOfPoints;
		this.points = points;
	}

	@Override
	public EMFTag read(int tagID, EMFInputStream emf, int len)
			throws IOException {

		Rectangle r = emf.readRECTL();
		int n = emf.readDWORD();
		PolyBezierTo tag = new PolyBezierTo(r, n, emf.readPOINTL(n));
		return tag;
	}

	@Override
	public void write(int tagID, EMFOutputStream emf) throws IOException {
		emf.writeRECTL(bounds);
		emf.writeDWORD(numberOfPoints);
		emf.writePOINTL(numberOfPoints, points);
	}

	@Override
	public String toString() {
		return super.toString() + "\n" + "  bounds: " + bounds + "\n"
				+ "  #points: " + numberOfPoints;
	}
}
