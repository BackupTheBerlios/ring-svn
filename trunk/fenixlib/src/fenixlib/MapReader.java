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
 * MapReader.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import fenixlib.util.GZFileReader;
import static fenixlib.FenixlibConstants.MAP_MAGIC;
import static fenixlib.FenixlibConstants.M16_MAGIC;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An implementation of the <code>FileReader</code> interface to read Map Fenix
 * files.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see FileReader
 */
public class MapReader implements FileReader<AnimatedGraphic> {
    
    private final File file;
    
    /**
     * Constructs a new <code>MapReader</code> associated to the specified file.
     * @param f a <code>File</code> object which specifies the file to be used by read methods
     */
    public MapReader(File f) {
        file = f;
    }
    
    /**
     * Reads the file associated to this <code>MapReader</code> object as if it was
     * a Map fenix file and returns an <code>AnimatedGraphic</code> object created
     * from its information.
     * @return an AnimatedGraphic created from the information of the file
     * @see AnimatedGraphic
     * @throws java.io.IOException if the file is not a valid Map file or it couldn't 
     * be read for any reason
     */
    public AnimatedGraphic read() throws IOException {
        GZFileReader gzfile = new GZFileReader(file);
        
        String name;
        String descriptor;
        int width, height, id;
        
        DepthMode depth;
        Palette palette;
        
        BufferedImage buffImage = null;
        DataBuffer dataBuffer = null; // Stores image internaly as bytes        
        
        AnimatedGraphic ag = null;
        
        // Read and check descriptor
        descriptor = new String(gzfile.readBytes(8));
        if( MAP_MAGIC.compareToIgnoreCase(descriptor)==0 ) { // 8bpp MAP
            depth = DepthMode.DEPTH_8BPP;
        } else if( M16_MAGIC.compareToIgnoreCase(descriptor)==0 ) { // 16bpp Map
            depth = DepthMode.DEPTH_16BPP;
        } else	/* Incompatible format */ {
            throw new IOException("The file is not a valid map file");
        }
        
        // Read width, height, id and name of the map
        width = gzfile.readUnsignedShort();
        height = gzfile.readUnsignedShort();
        id = gzfile.readInt();
        name = gzfile.readAsciiZ(32);
        
        // Read palette if 8bpp
        palette = null;
        if(depth == DepthMode.DEPTH_8BPP)	{
            Color[] colors = new Color[256];
            for(int i = 0; i < 256; i++)
                colors[i] = new Color(
                        gzfile.readUnsignedByte() << 2,
                        gzfile.readUnsignedByte() << 2,
                        gzfile.readUnsignedByte() << 2
                        );
            palette = new Palette(colors);
            gzfile.skip(576); // This 576 bytes are useless in Fenix
        }
        
        /*  CONTROL POINTS */
        /*  Flags: First 12 bits of the short integer tells us the number of
            stored control points. Bit 13 tells if there is animation or not, 
            but since map animation is not actually supported by fenix we 
            consider this an error.
         */
        ArrayList<ControlPoint> controlPoints = new ArrayList<ControlPoint>();
        {
            short nFlags = gzfile.readShort();
            int nPoints = nFlags & 0xfff; // Number of Control Points 
            if ((nFlags >> 12) != 0) // Animation bit set to one
                throw new IOException("Map animation is not supported");
            
            if  (nPoints>0) { // Read all control points
                short cX, cY;
                for (int i = 0; i < nPoints; i++) {
                    cX = gzfile.readShort();
                    cY = gzfile.readShort();
                    // Only create the Control Point if it is not the invalid CP (-1,-1)
                    if (cX != -1 && cY != -1) {
                        controlPoints.add (new ControlPoint(i, (int)cX, (int)cY));
                    }
                }
                
            }
        }
        
        /* MAP PIXEL DATA
         * First create the buffered image. Then create a <Type>Buffer to 
         * store pixel data and  set our BufferedImage data by
         * creating a Raster object containing the ByteBuffer information
         */
        switch (depth) {
            case DEPTH_8BPP:
                // Obtain pixel data and create the buffer    
                {
                    byte[] data = gzfile.readBytes(width * height); 
                    dataBuffer =  new DataBufferByte(data, data.length);
                }   
                
                // Create an IndexColorModel to store the 256 color palette
                IndexColorModel cm = new IndexColorModel(8, 256, 
                        palette.getRedComponents(),
                        palette.getGreenComponents(), 
                        palette.getBlueComponents()
                        );      
                
                // Create a BufferedImage compatible with the IndexColorModel
                buffImage = new BufferedImage(width, height,
                        BufferedImage.TYPE_BYTE_INDEXED, cm);  
                
                break;
            
            case DEPTH_16BPP:
                // Obtain pixel data and create the buffer
                {    
                    short[] data = gzfile.readShorts(width * height);
                    dataBuffer = new DataBufferUShort(data, data.length);
                }
                
                // Create a BufferedImage compatible with the IndexColorModel
                buffImage = new BufferedImage(width, height,
                        BufferedImage.TYPE_USHORT_565_RGB);
            
                break;
        }
        
        // Set data to the BufferedImage
        buffImage.setData(Raster.createRaster(
                buffImage.getSampleModel(), dataBuffer, 
                new java.awt.Point(0,0)
                ));        
        
        // Create the AnimatedGraphic
        if (depth == DepthMode.DEPTH_8BPP) {
            ag = AnimatedGraphic.create8(width, height, palette);
        } else if (depth == DepthMode.DEPTH_16BPP) {
            ag = AnimatedGraphic.create16(width, height);
        }
        
        // Properties
        ag.setName(name);
        ag.setId(id);
        ag.setFlags(0);
        
        // Control points
        for(ControlPoint cp : controlPoints)
            ag.setControlPoint(cp);
        
        // Animation
        ag.addFrame(buffImage);
        ag.addSequence("Map sequence");
        ag.addKeyFrame(0, 0, 0, 0, 0);
        
        return ag;
    }
    
}
