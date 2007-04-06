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
 * FplReader.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

import fenixlib.util.GZFileReader;
import java.io.File;
import java.io.IOException;

/**
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class FplReader implements FileReader<Palette>, FenixlibConstants {
    private final File file;
    
    /* Version code constants */
    private static final short VERSION_MAJOR = 0x0100;
    private static final short VERSION_MINOR = 0x0000;    
    
    /** 
     *  @param f
     */
    public FplReader(File f) {
        file = f;
    }

    /** 
     *  @return 
     */
    public Palette read() throws IOException {
        GZFileReader buff = new GZFileReader(file);
        
        byte[] descriptor = buff.readBytes(16);
        
        Color[] colors = new Color[256];
        
        // Check descriptor
        if( FPL_MAGIC.compareTo(new String(descriptor))==0 ) {  
            // Check version
            short versionMajor = buff.readShort();
            short versionMinor = buff.readShort();
            if (versionMajor == VERSION_MAJOR) {
                // Check depth
                int depth = buff.readInt();
                if (depth == 8) {
                    colors = new Color[256];
                    for (int i = 0; i < 256; i++) // Read Colors
                        colors[i] = new Color(
                                buff.readUnsignedByte(), 
                                buff.readUnsignedByte(),
                                buff.readUnsignedByte());
                } else 	// Unsuported depth
                    throw new IOException("Unsuported depth");
            } else // Incompatible version 
                throw new IOException("Incompatible file version");
        } else 	// Invalid FPL descriptor
            throw new IOException("The file is not a valid fpl file");    
        
        return new Palette(colors);
    } 
}
