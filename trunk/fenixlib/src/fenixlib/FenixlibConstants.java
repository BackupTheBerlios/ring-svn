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
 * FenixlibConstants.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

/**
 * A collection of constants used by the library.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public interface FenixlibConstants {
    
    /* ------------------------- *
     | FILE MAGICS (DESCRIPTORS) |
     * ------------------------- */
    
    /* PALETTES */

    /**
     * An string (actually a sequence of bytes) that identifies a Fenix Pal file.
     */
    static final String PAL_MAGIC = new String (new byte[]{
                    'p', 'a', 'l'
                    , 0x1A,0x0D, 0x0A, 0x00, 0x00 });  

    /**
     * An string (actually a sequence of bytes) that identifies a Fenix Fpl file.
     */
    static final String FPL_MAGIC = new String (new byte[]{
                    'F', 'e', 'n', 'i', 'x', 'P', 'a', 'l', 'e', 't', 't', 'e'
                    ,0x1A,0x0D, 0x0A, 0x00 });
    
    /* GRAPHICS */
    
    /**
     * An string (actually a sequence of bytes) that identifies a Fenix Map file of
     * 8bpp.
     */
    static final String MAP_MAGIC = new String (new byte[]{
                    'm', 'a', 'p'
                    , 0x1A,0x0D, 0x0A, 0x00, 0x00 });  
   
    /**
     * An string (actually a sequence of bytes) that identifies a Fenix Map file of
     * 16bpp.
     */
    static final String M16_MAGIC = new String (new byte[]{
                    'm', '1', '6'
                    , 0x1A,0x0D, 0x0A, 0x00, 0x00 });      
    
    /**
     * An string (actually a sequence of bytes) that identifies a Fenix Fbm file.
     */
    static final String FBM_MAGIC = new String (new byte[]{
                    'F', 'e', 'n', 'i', 'x', 'B', 'i', 't', 'm', 'a', 'p', ' '
                    ,0x1A,0x0D, 0x0A, 0x00 });
}
