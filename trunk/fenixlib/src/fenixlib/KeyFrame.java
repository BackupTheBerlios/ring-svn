/* Ring Ide - The all in one IDE for Fenix.
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
 * KeyFrame.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import java.awt.image.BufferedImage;

/** 
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
class KeyFrame {
    
    private int flags = 0;
    private int pause = 0;
    private int angle = 0;
    
    public KeyFrame() {}
    
    public KeyFrame(int flags, int angle, int pause) {
        this.flags = flags;
        this.pause = pause;
        this.angle = angle;
    }
    
    public void setFlags(int flags) {
        this.flags = flags;
    }
    
    public void setPause(int pause) {
        this.pause = pause;
    }
    
    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    public int getFlags() { return flags; }
    
    public int getAngle() { return angle; }
    
    public int getPause() { return pause; }
    
    public BufferedImage getFrame() { return frame; }
    
    BufferedImage frame;    /* Image data */
    
}
