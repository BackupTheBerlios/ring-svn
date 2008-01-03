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
 * PalReader.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib;

import fenixlib.util.GZFileReader;
import static fenixlib.FenixlibConstants.PAL_MAGIC;

import java.io.File;
import java.io.IOException;


/**
 * An implementation of the <code>FileReader</code> interface to read Pal Fenix
 * files.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see FileReader
 */
public class PalReader implements FileReader<Palette> {    
    private final File file;

    /**
     * Constructs a new <code>PalReader</code> associated to the specified file.
     * @param f a <code>File</code> object which specifies the file to be used by read methods
     */   
    public PalReader(File f) {
        file = f;
    }
    
    /**
     * Reads the file associated to this <code>PalReader</code> object as if it was
     * a Pal fenix file and returns a <code>Palette</code> object created
     * from its information.
     * @return a Palette created from the information of the file
     * @see Palette
     * @throws java.io.IOException if the file is not a valid Pal file or it 
     * couldn't be read for any reason
     */
    public Palette read() throws IOException {
        GZFileReader buff = new GZFileReader(file);
        
        byte[] descriptor = buff.readBytes(8);
        Color[] colors = new Color [256];
        
        // Check descriptor
        if (PAL_MAGIC.equals (new String(descriptor).toLowerCase())) { 
            // Read all colors
            for(int i=0; i<256; i++)	
                colors[i] = new Color(
                        (int)buff.readUnsignedByte() << 2, 
                        (int)buff.readUnsignedByte() << 2,
                        (int)buff.readUnsignedByte() << 2);
        } else // Invalid PAL descriptor
            throw new IOException("The file is not a valid pal file");
        
        return new Palette(colors);
    }
    
}
