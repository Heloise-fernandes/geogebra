/* SpaceAtom.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 *
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules 
 * is making a combined work based on this library. Thus, the terms 
 * and conditions of the GNU General Public License cover the whole 
 * combination.
 * 
 * As a special exception, the copyright holders of this library give you 
 * permission to link this library with independent modules to produce 
 * an executable, regardless of the license terms of these independent 
 * modules, and to copy and distribute the resulting executable under terms 
 * of your choice, provided that you also meet, for each linked independent 
 * module, the terms and conditions of the license of that module. 
 * An independent module is a module which is not derived from or based 
 * on this library. If you modify this library, you may extend this exception 
 * to your version of the library, but you are not obliged to do so. 
 * If you do not wish to do so, delete this exception statement from your 
 * version.
 * 
 */

/* Modified by Calixte Denizet */

package com.himamis.retex.renderer.share;

import java.util.HashMap;
import java.util.Map;

import com.himamis.retex.renderer.share.exception.InvalidUnitException;

/**
 * An atom representing whitespace. The dimension values can be set using different unit types.
 */
public class SpaceAtom extends Atom {

	private static Map<String, Integer> units = new HashMap<String, Integer>();
	static {
		units.put("em", TeXConstants.UNIT_EM);
		units.put("ex", TeXConstants.UNIT_EX);
		units.put("px", TeXConstants.UNIT_PIXEL);
		units.put("pix", TeXConstants.UNIT_PIXEL);
		units.put("pixel", TeXConstants.UNIT_PIXEL);
		units.put("pt", TeXConstants.UNIT_PT);
		units.put("bp", TeXConstants.UNIT_POINT);
		units.put("pica", TeXConstants.UNIT_PICA);
		units.put("pc", TeXConstants.UNIT_PICA);
		units.put("mu", TeXConstants.UNIT_MU);
		units.put("cm", TeXConstants.UNIT_CM);
		units.put("mm", TeXConstants.UNIT_MM);
		units.put("in", TeXConstants.UNIT_IN);
		units.put("sp", TeXConstants.UNIT_SP);
		units.put("dd", TeXConstants.UNIT_DD);
		units.put("cc", TeXConstants.UNIT_CC);
	}

	private static interface UnitConversion { // NOPMD
		public double getPixelConversion(TeXEnvironment env);
	}

	private static UnitConversion[] unitConversions = new UnitConversion[] {

	new UnitConversion() {// EM
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return env.getTeXFont().getEM(env.getStyle());
				}
			},

			new UnitConversion() {// EX
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return env.getTeXFont().getXHeight(env.getStyle(), env.getLastFontId());
				}
			},

			new UnitConversion() {// PIXEL
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return 1 / env.getSize();
				}
			},

			new UnitConversion() {// BP (or PostScript point)
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return TeXFormula.PIXELS_PER_POINT / env.getSize();
				}
			},

			new UnitConversion() {// PICA
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (12 * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			},

			new UnitConversion() {// MU
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					TeXFont tf = env.getTeXFont();
					return tf.getQuad(env.getStyle(), tf.getMuFontId()) / 18;
				}
			},

			new UnitConversion() {// CM
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (28.346456693f * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			},

			new UnitConversion() {// MM
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (2.8346456693f * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			},

			new UnitConversion() {// IN
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (72 * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			},

			new UnitConversion() {// SP
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (65536 * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			},

			new UnitConversion() {// PT (or Standard Anglo-American point)
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (.9962640099f * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			},

			new UnitConversion() {// DD
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (1.0660349422f * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			},

			new UnitConversion() {// CC
				@Override
				public double getPixelConversion(TeXEnvironment env) {
					return (12.7924193070f * TeXFormula.PIXELS_PER_POINT) / env.getSize();
				}
			} };

	// whether a hard space should be represented
	private boolean blankSpace;

	// thinmuskip, medmuskip, thickmuskip
	private int blankType;

	// dimensions
	private double width;
	private double height;
	private double depth;

	// units for the dimensions
	private int wUnit;
	private int hUnit;
	private int dUnit;

	@Override
	final public Atom duplicate() {
		SpaceAtom ret = new SpaceAtom();
		
		ret.blankSpace = blankSpace;
		ret.blankType = blankType;
		ret.width = width;
		ret.height = height;
		ret.depth = depth;
		ret.wUnit = wUnit;
		ret.hUnit = hUnit;
		ret.dUnit = dUnit;
		
		return setFields(ret);
	}

	public SpaceAtom() {
		blankSpace = true;
	}

	public SpaceAtom(int type) {
		blankSpace = true;
		blankType = type;
	}

	public SpaceAtom(int unit, double width, double height, double depth) throws InvalidUnitException {
		// check if unit is valid
		checkUnit(unit);

		// unit valid
		this.wUnit = unit;
		this.hUnit = unit;
		this.dUnit = unit;
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	/**
	 * Check if the given unit is valid
	 *
	 * @param unit the unit's integer representation (a constant)
	 * @throws InvalidUnitException if the given integer value does not represent a valid unit
	 */
	public static void checkUnit(int unit) throws InvalidUnitException {
		if (unit < 0 || unit >= unitConversions.length) {
			throw new InvalidUnitException();
		}
	}

	public SpaceAtom(int widthUnit, double width, int heightUnit, double height, int depthUnit, double depth)
			throws InvalidUnitException {
		// check if units are valid
		checkUnit(widthUnit);
		checkUnit(heightUnit);
		checkUnit(depthUnit);

		// all units valid
		wUnit = widthUnit;
		hUnit = heightUnit;
		dUnit = depthUnit;
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public static int getUnit(String unit) {
		Integer u = units.get(unit);
		return u == null ? TeXConstants.UNIT_PIXEL : u.intValue();
	}

	public static double[] getLength(String lgth) {
		if (lgth == null) {
			return new double[] { TeXConstants.UNIT_PIXEL, 0f };
		}

		int i = 0;
		for (; i < lgth.length() && !Character.isLetter(lgth.charAt(i)); i++) {
			;
		}
		double f = 0;
		try {
			f = Double.parseDouble(lgth.substring(0, i));
		} catch (NumberFormatException e) {
			return new double[] { Double.NaN };
		}

		int unit;
		if (i != lgth.length()) {
			unit = getUnit(lgth.substring(i).toLowerCase());
		} else {
			unit = TeXConstants.UNIT_PIXEL;
		}

		return new double[] {  unit, f };
	}

	@Override
	public Box createBox(TeXEnvironment env) {
		if (blankSpace) {
			if (blankType == 0) {
				return new StrutBox(env.getSpace(), 0, 0, 0);
			}
			int bl = blankType < 0 ? -blankType : blankType;
			Box b;
			if (bl == TeXConstants.THINMUSKIP) {
				b = Glue.get(TeXConstants.TYPE_INNER,
						TeXConstants.TYPE_BIG_OPERATOR, env);
			} else if (bl == TeXConstants.MEDMUSKIP) {
				b = Glue.get(TeXConstants.TYPE_BINARY_OPERATOR,
						TeXConstants.TYPE_BIG_OPERATOR, env);
			} else {
				b = Glue.get(TeXConstants.TYPE_RELATION,
						TeXConstants.TYPE_BIG_OPERATOR, env);
			}
			if (blankType < 0) {
				b.negWidth();
			}
			return b;
		}
		return new StrutBox(width * getFactor(wUnit, env),
				height * getFactor(hUnit, env), depth * getFactor(dUnit, env),
				0);
	}

	public static double getFactor(int unit, TeXEnvironment env) {
		return unitConversions[unit].getPixelConversion(env);
	}
}
