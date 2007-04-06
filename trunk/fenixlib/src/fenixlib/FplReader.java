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
 * An implementation of the <code>FileReader</code> interface to read Fpl Fenix
 * files.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see FileReader
 */
public class FplReader implements FileReader<Palette>, FenixlibConstants {
    private final File file;
    
    /* Version code constants */
    private static final short VERSION_MAJOR = 0x0100;
    private static final short VERSION_MINOR = 0x0000;    
    
    /**
     * Constructs a new <code>FplReader</code> associated to the specified file.
     * @param f a <code>File</code> object which specifies the file to be used by read methods
     */ 
    public FplReader(File f) {
        file = f;
    }

    /**
     * Reads the file associated to this <code>FplReader</code> object as if it was
     * an Fpl fenix file and returns a <code>Palette</code> object created
     * from its information.
     * @return a Palette created from the information of the file
     * @see Palette
     * @throws java.io.IOException if the file is not a valid Fpl file or it 
     * couldn't be read for any reason
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
