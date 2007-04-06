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
 * ControlPoint.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

/**
 * A class that defines a control point, understood as a pair of X,Y coordinates
 * and an integer value which indicates the index of the control point.
 * 
 * Control points are used in <code>AbstractGraphic</code> objects to specify
 * imaginary coordinates. This information can be writen in a Map or Fbm file and
 * recovered from Fenix for different purposes.
 * 
 * The index of the control point is the position of it in the ControlPoint array
 * which is stored in an AbstractGraphic. The reason for having this information
 * in <code>ControlPoint</code> is to allow the programmer extract a <code>ControlPoint</code>
 * and then inserting it in the same position.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see AbstractGraphic
 */
public class ControlPoint { //implements Comparable {
    private int x;
    private int y;
    
    private final int index;
    
    /**
     * Creates a new <code>ControlPoint</code> setting its x, y coordinates to 0, 0.
     * @param index The index of the control point
     */
    public ControlPoint(int index) {
        this.index = index;
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Creates a new <code>ControlPoint</code> setting its x, y coordinates.
     * @param index The index of the control point
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public ControlPoint(int index, int x, int y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the index of the control point.
     * @return The index of the control point
     */
    public int getIndex(){ return index; }
    
    /**
     * Gets the X coordinate of the control point.
     * @return The X coordinate of the control point
     */
    public int getX(){ return x; }
    
    /**
     * Gets the Y coordinate of the control point.
     * @return The Y coordinate of the control point
     */
    public int getY(){ return y; }
    
    /**
     * Sets the X coordinate of the control point.
     * @param x The new value for the X coordinate of the control point
     */
    public void setX(int x) { this.x = x; }
    
    /**
     * Sets the Y coordinate of the control point.
     * @param y The new value for the Y coordinate of the control point
     */
    public void setY(int y) { this.y = y; }
    
    /**
     * Returns a string representation of this control point.
     * @return a string representation of this control point.
     */
    public String toString() { return index + ": (" + x + ", " + y + ")"; }
 /*   
    public int compareTo(Object o) {
        ControlPoint c;
        if (o instanceof ControlPoint)
            c = (ControlPoint)o;
        else
            throw new ClassCastException(
                    "A ControlPoint can only be compared to a ControlPoint");
        
        return index - c.index;
    }
   */
}
