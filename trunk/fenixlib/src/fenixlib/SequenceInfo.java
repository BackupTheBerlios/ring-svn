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
 * SequenceInfo.java
 *
 * Created on 3 de abril de 2007
 */

package fenixlib;

/**
 * A class to be used as an read-only structure with information about a sequence.
 * Some methods of the <code>AnimatedGraphic</code> class return objects of this
 * class.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see AnimatedGraphic
 */
public final class SequenceInfo {
    /**
     * A descriptive name for the sequence.
     */
    public final String name;
    /**
     * The number of keyframes contained by this sequence.
     */
    public final int nKeyFrames;
    /**
     * An integer value that indicates the next sequence in the animation. The value
     * represents the index of the sequence in the sequence array of an 
     * <code>AnimatedGraphic</code> object.
     * 
     * Can be -1 if the animation must stop after the sequence finishes.
     */
    public final int nextSequence;
    
    SequenceInfo(String name, int nKeyFrames, int nextSequence) {
        this.name = name;
        this.nKeyFrames = nKeyFrames;
        this.nextSequence = nextSequence;
    }
}
