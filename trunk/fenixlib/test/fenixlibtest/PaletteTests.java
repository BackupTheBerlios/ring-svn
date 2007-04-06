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
 * PalettesTest.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlibtest;

import fenixlib.*;
import junit.framework.*;
import java.io.*;

/** Tests for Palette related classes
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class PaletteTests extends TestCase {
    
    public PaletteTests() {}
    
    // Test loading of PAL files
    public void testPalReading() {
        File file = new File("./res/test/pal.pal");
        // Three colors to check
        Color[] expectedColors = new Color[] {
            new Color(0, 0, 0), // Index 0
            new Color(232, 232, 232), // Index 14
            new Color(252, 220, 252) // Index 255
        };
        
        fenixlib.FileReader<Palette> fr;
        Palette palette;
        Color color, expected;
        try {
            fr = new PalReader(file);
            palette = fr.read(); // Read Pal file

            // Check 0, 14 and 255 color indexes
            color = palette.getColor(0);
            expected = expectedColors[0];
            assertEquals (expected.getRed(), color.getRed());
            assertEquals (expected.getGreen(), color.getGreen());
            assertEquals (expected.getBlue(), color.getBlue());  
            
            color = palette.getColor(14);
            expected = expectedColors[1];
            assertEquals (expected.getRed(), color.getRed());
            assertEquals (expected.getGreen(), color.getGreen());
            assertEquals (expected.getBlue(), color.getBlue());   
            
            color = palette.getColor(255);
            expected = expectedColors[2];
            assertEquals (expected.getRed(), color.getRed());
            assertEquals (expected.getGreen(), color.getGreen());
            assertEquals (expected.getBlue(), color.getBlue());  
            
        } catch (IOException e) {
            fail(e.toString());
        }
    }

    // Test loading of FPL files
    public void testFplReading() {
        File file = new File("./res/test/fpl.fpl");
        // Three colors to check
        Color[] expectedColors = new Color[] {
            new Color(0, 0, 0), // Index 0
            new Color(232, 232, 232), // Index 14
            new Color(252, 220, 252) // Index 255
        };
        
        fenixlib.FileReader<Palette> fr;
        Palette palette;
        Color color, expected;
        try {
            fr = new FplReader(file);
            palette = fr.read(); // Read Pal file

            // Check 0, 14 and 255 color indexes
            color = palette.getColor(0);
            expected = expectedColors[0];
            assertEquals (expected.getRed(), color.getRed());
            assertEquals (expected.getGreen(), color.getGreen());
            assertEquals (expected.getBlue(), color.getBlue());  
            
            color = palette.getColor(14);
            expected = expectedColors[1];
            assertEquals (expected.getRed(), color.getRed());
            assertEquals (expected.getGreen(), color.getGreen());
            assertEquals (expected.getBlue(), color.getBlue());   
            
            color = palette.getColor(255);
            expected = expectedColors[2];
            assertEquals (expected.getRed(), color.getRed());
            assertEquals (expected.getGreen(), color.getGreen());
            assertEquals (expected.getBlue(), color.getBlue());  
            
        } catch (IOException e) {
            fail(e.toString());
        }
    }    
    
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(PaletteTests.class);     
    }
}
