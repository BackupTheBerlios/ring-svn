/* fenixlib - Library to support Fenix Files in Java
 * Copyright (C) 2007  Darío Cutillas Carrillo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/*
 * Color.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

/** A class for representing RGB colors
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class Color {
    private int r;
    private int g;
    private int b;
    
    /** Creates an RGB color with the specified red, green and blue values in a range
     *  from 0 to 255
     *  @throws IllegalArgumentException if r,g or b are outside of the range 0 to 255
     */
    public Color(int r, int g, int b) throws IllegalArgumentException{
        setRGB(r,g,b);
    }
    
    /** Creates a new RGB color from an java.awt.Color
     */
    public Color(java.awt.Color c) {
        setRGB(c.getRed(),c.getGreen(),c.getBlue());
    }
    
    /** Gets the red component (0-255) of the <code>Color</code>
     *  @return the RED component
     */
    public int getRed() {return r;}
    
    /** Gets the green component (0-255) of the <code>Color</code>
     *  @return the GREEN component
     */
    public int getGreen() {return g;}
    
    /** Gets the blue component (0-255) of the <code>Color</code>
     *  @return the BLUE component
     */
    public int getBlue() {return b;}
    
    /** Performs grayscale conversion transformation. */
    public void toGrayScale() {
        int grayTone = (int)((float)r*0.299f+(float)g*0.587f + (float)b*0.114f);
        r = grayTone;
        g = grayTone;
        b = grayTone;
    }
    
    /** Performs color inversion transformation. */
    public void invert() {
        r = 255-r;
        g = 255-g;
        b = 255-b;
    }
    
    /** Sets Red, Green and Blue components of the color in a range from 0 to 255, inclusive
     *  @param r The RED component
     *  @param g The GREEN component
     *  @param b The BLUE component
     *  @throws IllegalArgumentException if r,g or b are outside of the range 0 to 255
     */
    public void setRGB(int r, int g, int b) throws IllegalArgumentException{
        if(r>255||r<0||g>255||g<0||b>255||b<0)
            throw new IllegalArgumentException();
        else
            this.r=r;this.g=g;this.b=b;
    }
    
    /** Sets Red, Green and Blue components from a <code>java.awt.Color</code> object
     *  @param c The <code>java.awt.Color</code> object
     */
    public void setRGB(java.awt.Color c) {
        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();
    }
    
    /** Sets the Red component of the <code>Color</code>
     *  @param r The RED component
     *  @throws IllegalArgumentException if r is outside of the range 0 to 255
     */
    public void setRed(int r) throws IllegalArgumentException {
        if(r<0||r>255)
            throw new IllegalArgumentException();
        else
            this.r=r;
    }
    
    /** Sets the Green component of the <code>Color</code>
     *  @param g The RED component
     *  @throws IllegalArgumentException if g is outside of the range 0 to 255
     */
    public void setGreen(int g) throws IllegalArgumentException {
        if(g<0||g>255)
            throw new IllegalArgumentException();
        else
            this.g=g;
    }
    
    /** Sets the Blue component of the <code>Color</code>
     *  @param b The BLUE component
     *  @throws IllegalArgumentException if b is outside of the range 0 to 255
     */
    public void setBlue(int b) throws IllegalArgumentException {
        if(b<0||b>255)
            throw new IllegalArgumentException();
        else
            this.b=b;
    }
    
    /** Get an independent copy of this <code>Color</code>
     *  @return a new <code>Color</code> with the same RGB components
     */
    public Color getCopy() {
        return new Color(r, g, b);
    }
    
    /** Creates a new <code>java.awt.Color</code>
     *  @return a new <code>java.awt.Color</code> object with the same RGB components
     */
    public java.awt.Color getAWTColor() {
        return new java.awt.Color(r,g,b);
    }    
}
