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
 * KeyFrameInfo.java
 *
 * Created on 3 de abril de 2007
 */

package fenixlib;

/**
 * A class to be used as an read-only structure with information about a keyframe.
 * Some methods of the <code>AnimatedGraphic</code> class return objects of this
 * class.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see AnimatedGraphic
 */
public final class KeyFrameInfo {
    /**
     * An integer value which specifies additional information of the keyframe, used
     * when representing the frame.
     */
    public final int flags;
    /**
     * An integer value indicating the position of the frame used by this KeyFrame in
     * the array of frames of an <code>AnimatedGraphic</code> object.
     * @see AnimatedGraphic
     */
    public final int frameIndex;
    /**
     * An integer value indicating the time to wait (in milliseconds) before going to
     * the next keyframe of the sequence.
     */
    public final int pause;
    /**
     * An integer value indicating the number of millidegrees that the frame should be 
     * rotated (counter-clockwise).
     */
    public final int angle;
    
    KeyFrameInfo(int frameIndex, int flags, int angle, int pause) {
        this.frameIndex = frameIndex;
        this.flags = flags;
        this.angle = angle;
        this.pause = pause;
    }
}