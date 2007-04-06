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
 * GraphicsTests.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlibtest;

import fenixlib.*;

import java.io.*;
import junit.framework.TestCase;

/** Tests for graphic classes
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class GraphicsTests extends TestCase {
    
    public GraphicsTests() { }
    
    public void testMapReading() {
        int expWidth;
        int expHeight;
        int expId;
        String expName;
        File file; 
        
        AnimatedGraphic animatedG;
        MapReader m;
        
        /* 16 BPP */
        file = new File("./res/test/map16bpp.map");
        m = new MapReader(file);
        
        expWidth = 640;
        expHeight = 480;
        expId = 1;
        expName = "Background";
        
        try {
            animatedG = m.read();
            assertEquals(expWidth, animatedG.getWidth());
            assertEquals(expHeight, animatedG.getHeight());
            assertEquals(expId, animatedG.getId());
            assertEquals(expName, animatedG.getName());            
        } catch (IOException e) {
            fail(e.toString());
        }     
        
        /* 16 BPP */
        file = new File("./res/test/map8bpp.map");
        m = new MapReader(file);
        
        expWidth = 323;
        expHeight = 283;
        expId = 0;
        expName = "Background8bpp";
        
        try {
            animatedG = m.read();
            assertEquals(expWidth, animatedG.getWidth());
            assertEquals(expHeight, animatedG.getHeight());
            assertEquals(expId, animatedG.getId());
            assertEquals(expName, animatedG.getName());            
        } catch (IOException e) {
            fail(e.toString());
        }            
    }
    
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(GraphicsTests.class);
        
    }    
}
