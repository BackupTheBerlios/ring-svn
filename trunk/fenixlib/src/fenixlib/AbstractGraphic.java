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
 * AbstractGraphic.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

import java.util.TreeSet;
import java.util.Comparator;

/**
 * A class that defines a graphic as a set of properties. This class does
 * not contain graphic information.
 * @author Darío Cutillas Carrillo (lord_danko at users.sourceforge.net)
 */
public abstract class AbstractGraphic {
    /**
     * The name of the graphic.
     */
    protected String name = "Unnamed";
    
    /**
     * The width of the graphic.
     */
    protected int width;
    
    /**
     * The height of the graphic.
     */
    protected int height;
    
    /**
     * The id of the graphic. An integer value which indicates the position where 
     * the graphic should be inserted in a GraphicCollection. This information is 
     * stored here so as graphics can be extracted from the collection and then
     * reinserted in the same position.
     */
    protected int id;
    
    /**
     * The depth of the graphic.
     */
    protected DepthMode depth;
    
    /**
     * The index palette used by the graphic, if necessary.
     */
    protected Palette palette; /* Palette (if 8 bits) */
    
    /**
     * Stores some additional information about the graphic.
     */
    protected int flags = 0;
    
    /**
     * A set containing all ControlPoints of the image. ControlPoints are 
     * pairs of (x, y) coordinates to store imaginary points in the graphic.
     */
    protected TreeSet<ControlPoint> controlPoints = new TreeSet<ControlPoint>(
        new Comparator<ControlPoint>() {
            public int compare(ControlPoint cp1, ControlPoint cp2) {
                return cp1.getIndex() - cp2.getIndex();
            };
    } );

    /**
     * Gets the width of the graphic.
     * @return the width of the graphic
     */
    public int getWidth() { return width; }
    
    /**
     * Gets the height of the graphic.
     * @return the height of the graphic
     */
    public int getHeight() { return height; }
    
    /**
     * Gets the Id of the graphic, that is, a integer which indicates where 
     * the graphic should be inserted in a GraphicCollection.
     * @return the id of the graphic
     */
    public int getId() { return id; }
    
    /**
     * Gets the the depth (bits per pixel) of the graphic
     * @return the depth of he graphic
     */
    public DepthMode getDepth() { return depth; }
    
    /**
     * Gets the name of the graphic.
     * @return the name of the graphic
     */
    public String getName() { return name; }
    
    /**
     * Gets the palette of the graphic. Can be null if the Graphic does not use
     * a palette.
     * @return a <code>Palette</code> object which is the palette of the graphic
     */
    public Palette getPalette() {
        return palette;
    }
    
    /**
     * Gets flags of the image. Flags store some additional information 
     * about the graphic.
     * @return the flags of the graphic
     */    
    public int getFlags() { return flags; }
    
    /**
     * Sets the name of the graphic.
     * @param name the new name
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Sets the id of the graphic, that is, a integer which indicates where the
     * position where the graphic should be put in a graphic collection.
     * @param id the new id
     */
    public void setId(int id) {this.id = id; }
    
    /**
     * Sets the flags of the graphic.
     * @param flags the new value for the flags
     */
    public void setFlags(int flags) {this.flags = flags; }

    /**
     * Returns true if graphic contains the specified ControlPoint.
     * @param index the wanted control point index
     * @return true if the graphic has the specified control point. Otherwise false.
     */    
    public boolean hasControlPoint(int index) {
        return controlPoints.contains(new ControlPoint(index));
    }
    
    /**
     * Gets the <code>ControlPoint</code> at the given index. Changes in the returned
     * object will result in changes in the ControlPoint of the graphic.
     * @param index index of the requested ControlPoint
     * @return the <code>ControlPoint</code> at the given index. This can be
     * <code>null</code> if the ControlPoint have not been set.
     */
    public ControlPoint getControlPoint(int index) {
        ControlPoint cp = new ControlPoint(index);
        if (controlPoints.contains(cp)) 
            return controlPoints.ceiling(cp);
        else
            return null;
    }
    
    /**
     * Returns an array of all defined ControlPoints.
     * @return an array of <code>ControlPoint</code> objects which are
     * the defined control points of the graphic
     */
    public ControlPoint[] getControlPoints() {
        return controlPoints.toArray(new ControlPoint[0]);
    }
    
    /**
     * Gets the greatest <code>ControlPoint</code> defined for the graphic.
     * @return the greatest <code>ControlPoint</code> defined for the graphic
     */
    public ControlPoint getLastControlPoint() {
        return controlPoints.last();
    }
    
    /**
     * Defines (or redefines) a control point.
     * @param cp the <code>ControlPoint</code> object to be set.
     * @throws IllegalArgumentException if cp has a null value
     */
    public void setControlPoint(ControlPoint cp) throws IllegalArgumentException {
        if (cp == null) 
            throw new IllegalArgumentException("Cannot set a null ControlPoint");
        
        if (controlPoints.contains(cp)) {
            ControlPoint oldCp = controlPoints.ceiling(cp);
            oldCp.setX (cp.getX());
            oldCp.setY (cp.getY());
        } else
            controlPoints.add(cp);
    }
    
    /**
     * Defines (or redefines) a control point
     * @param index the index of the control point to be set
     * @param x the new x coordinate for the control point
     * @param y the new y coordinate for the control point
     */
    public void setControlPoint(int index, int x, int y) {
        setControlPoint (new ControlPoint(index, x, y));
    }    
    
    /**
     * Deletes a control point from the graphic.
     * @param index the index of the control point to be removed
     * @return true if the controlpoint was removed
     */    
    public boolean removeControlPoint (int index) {
        return controlPoints.remove (new ControlPoint(index));
    }
       
}
