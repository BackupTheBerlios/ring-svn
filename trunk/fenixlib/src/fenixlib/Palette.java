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
 * Palette.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

/** This class defines a 256 color palette and provides methods for accesing these
 *  colors.
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class Palette {
    Color[] colors = new Color[256];
    
    /** Creates a <code>Palette</code> and initialize all colors to black (0,0,0) .*/
    public Palette() {
        for(int i=0; i<256; i++)
            colors[i] = new Color(0, 0, 0);
    }
    
    /** Creates a <code>Palette</code> so its colors are set from the <code>colors</code> 
     *  color array. The color array is copied  so changes in the palette will 
     *  not affect the original array. If the color array has less than 256 colors, 
     *  the palette is completed with black entries.
     *	@param colors the color array.
     */
    public Palette(Color[] colors){
        this();
        System.arraycopy(colors,0,this.colors,0,colors.length);
    }
    
    /** Creates a palette from another palette. Changes in one of the palettes will 
     *  affect the other 
     */
    Palette(Palette palette) {
        colors = palette.getColors();
    }
    
    /** Returns a reference <code>Color</code> object which is the color at the 
     *  position <code>index</code> in the palette. Changes made
     *	in the color will result in changes in the palette.
     *	@param index the index of the color in the palette.
     *	@return a reference to a <code>Color</code> object which represent the 
     *  color at the given position.
     */
    public Color getColor(int index) {
        return colors[index];
    }
    
    public void setColor(int index, Color c) {
        colors[index] = c;
    }
    
    /**	Returns the entire palette colors as an array of <code>Color</code> objects. 
     *  Changes made to this array will result in changes
     *	in the palette.
     *	@return a <code>Color</code> array representing the entire palette.
     */
    public Color[] getColors() {
        return colors;
    }
    
    /** The following functions return an array containing all Red, Green or Blue 
        components 
     */
    byte[] getRedComponents() {
        byte[] bytes = new byte[256];
        for(int i=0; i<256; i++)
            bytes[i]=(byte)colors[i].getRed();
        return bytes;
    }
    byte[] getGreenComponents() {
        byte[] bytes = new byte[256];
        for(int i=0; i<256; i++)
            bytes[i]=(byte)colors[i].getGreen();
        return bytes;
    }
    byte[] getBlueComponents() {
        byte[] bytes = new byte[256];
        for(int i=0; i<256; i++)
            bytes[i]=(byte)colors[i].getBlue();
        return bytes;
    }    
}
