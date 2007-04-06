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
 * FplWriter.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import fenixlib.util.GZFileWriter;
import java.io.File;
import java.io.IOException;

/**
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class FplWriter implements FileWriter<Palette>, FenixlibConstants {
    
    private File file;
    
    /* Version code constants */
    private static final short VERSION_MAJOR = 0x0100;
    private static final short VERSION_MINOR = 0x0000;      
    
    /** 
     *  @param f
     */
    public FplWriter(File f) {
        file = f;
    }
    
    /** 
     *  @param palette
     */    
    public void write(Palette palette) throws IOException {
        GZFileWriter gzfile = new GZFileWriter();
        
        gzfile.writeAsciiZ(FPL_MAGIC,16);
        gzfile.writeShort(VERSION_MAJOR);
        gzfile.writeShort(VERSION_MINOR);
        gzfile.writeInt(8); // Depth
        
        Color[] colors = palette.getColors();
        for(int i=0; i<256; i++) {
            gzfile.writeByte((byte)colors[i].getRed());
            gzfile.writeByte((byte)colors[i].getGreen());
            gzfile.writeByte((byte)colors[i].getBlue());
        }
        gzfile.toFile(file);        
    }
    
}
