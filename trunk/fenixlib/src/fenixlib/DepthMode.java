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
 * DepthMode.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

/**
 * A convenient enumeration for depth modes. Currently only DEPTH_8BPP and
 * DEPTH_16BPP modes are supported since Fenix only work with 8bpp and 16bpp modes
 * (it is suppose to accept 1BPP images aswell but it doesn't in fact).
 * @author Darío
 */
public enum DepthMode {
    // DEPTH_1BPP (1), 
    /**
     * Specifies a depth of 8 bits per pixel (indexed).
     */
    DEPTH_8BPP (8), 
    /**
     * Specifies a depth of 16 bits per pixel (565 format).
     */
    DEPTH_16BPP(16);
    
    private final int depth;
    
    private DepthMode(int d) {
        depth = d;
    }
    
    /**
     * Returns the number of bits per pixel used for this DepthMode.
     * @return the number of bits per pixel used for this DepthMode
     */
    public int toInt() {
        return depth;
    }
}
