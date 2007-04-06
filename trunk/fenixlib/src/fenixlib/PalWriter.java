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
 * PalWriter.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An implementation of the <code>FileWriter</code> interface to write Pal Fenix
 * files.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see FileWriter
 */
public class PalWriter implements FileWriter<Palette>, FenixlibConstants {
    
    private File file;
    
    /**
     * Constructs a new <code>PalWriter</code> associated to the specified file.
     * @param f a <code>File</code> object which specifies the file to be used by 
     * different methods of the class
     */
    public PalWriter(File f) {
        file = f;
    }
    
    /**
     * Writes a Pal file from the information in an <code>Palette</code>
     * object.
     * @param palette the <code>Palette</code> whose information is being used to write
     * the file
     * @throws java.io.IOException if any error occurrs during the writing process
     */ 
    public void write(Palette palette) throws IOException {
        
        // Create a new DataOutputStream to write file data
        DataOutputStream outputStream = new DataOutputStream(
                new BufferedOutputStream(
                new FileOutputStream(file) ) );
        
        // Write header
        outputStream.writeBytes(PAL_MAGIC);
        
        // Write Color table
        Color colors[] = palette.getColors();
        for (int i=0; i<colors.length; i++) {
            outputStream.write (colors[i].getRed()>>2);
            outputStream.write (colors[i].getGreen()>>2);
            outputStream.write (colors[i].getBlue()>>2);
        }
        
        // Write the Gamma colors data. This data can not be set to 0
        for(int i=0; i< 576; i++)
            outputStream.write(1);
        
        // Close the stream
        outputStream.close();        
    }
}
