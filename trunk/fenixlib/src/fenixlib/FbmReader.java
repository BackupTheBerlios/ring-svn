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
 * FbmReader.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import fenixlib.util.GZFileReader;
import fenixlib.util.GZFileWriter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An implementation of the <code>FileReader</code> interface to read Fbm Fenix
 * files.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see FileReader
 */
public class FbmReader implements FileReader<AnimatedGraphic>, FenixlibConstants {
    
    private File file;
    
    /* Version code constants */
    private static final short VERSION_MAJOR = 0x0100;
    private static final short VERSION_MINOR = 0x0000;    
    
    
    /**
     * Constructs a new <code>FbmReader</code> associated to the specified file.
     * @param f a <code>File</code> object which specifies the file to be used by read methods
     */
    public FbmReader(File f) {
        file = f;
    }
    
    
    /**
     * Reads the file associated to this <code>FbmReader</code> object as if it was
     * an Fbm fenix file and returns an <code>AnimatedGraphic</code> object created
     * from its information.
     * @return an AnimatedGraphic created from the information of the file
     * @throws java.io.IOException if the file is not a valid Fbm file or it couldn't be read for any reason
     * @see AnimatedGraphic
     */
    public AnimatedGraphic read() throws IOException {
        GZFileReader gzfile = new GZFileReader(file);
        
        AnimatedGraphic ag = null;
        
        // Check header
        byte[] descriptor = gzfile.readBytes(16);
        DepthMode depth;
        
        if( FBM_MAGIC.compareTo(new String(descriptor))==0 ) {
            
            // Check version
            short versionMajor = gzfile.readShort();
            short versionMinor = gzfile.readShort();
            
            if( versionMajor == VERSION_MAJOR ) {
                // Check depth
                int d = gzfile.readInt();
                switch (d) {
                    /*case 1:
                        depth = DepthMode.DEPTH_1BPP;
                        break;
                     */
                    case 8:
                        depth = DepthMode.DEPTH_8BPP;
                        break;
                    case 16:
                        depth = DepthMode.DEPTH_16BPP;
                        break;
                    default:
                        throw new IOException("Unsuported depth");
                }
                
            } else      /* Incompatible version */
                throw new IOException("Incompatible file version");
        } else          /* Invalid FPL descriptor */
            throw new IOException("The file is not a valid fbm file");
        
        // Descriptor
        String name = gzfile.readAsciiZ(64);       // Name
        int width = gzfile.readInt();               // Width
        int height = gzfile.readInt();              // Height
        int flags = gzfile.readInt();               // Flags
        int id = gzfile.readInt();                  // Id
        
        int maxFrame = gzfile.readInt();            // Max frame
        int maxSequence = gzfile.readInt();         // Max sequence
        int maxKeyFrame = gzfile.readInt();         // Max keyframe
        int maxPoint = gzfile.readInt();            // Max point
        int nPoints = gzfile.readInt();             // Points       
        
        // Palette (8bpp)
        Palette palette = null;
        
        if (depth==DepthMode.DEPTH_8BPP) {
            Color[] colors = new Color[256];
            for(int i=0;i<256;i++)
                colors[i] = new Color(
                                gzfile.readUnsignedByte(), 
                                gzfile.readUnsignedByte(),
                                gzfile.readUnsignedByte() 
                                );
            palette = new Palette(colors);
            ag = new AnimatedGraphic (width, height, palette);
        } else if (depth == DepthMode.DEPTH_16BPP)
            ag = new AnimatedGraphic (width, height);   
        
        ag.setName(name);
        ag.setId(id);
        ag.setFlags(flags);
        
        // Sequences
        InternalSequence[] internalSequences = new InternalSequence[maxSequence+1];
        for(int i=0; i<internalSequences.length; i++) {
            internalSequences[i] = new InternalSequence();
            internalSequences[i].name = gzfile.readAsciiZ(32);     // Name
            internalSequences[i].firstKeyFrame = gzfile.readInt();  // First keyframe
            internalSequences[i].lastKeyFrame = gzfile.readInt();   // Last keyframe
            internalSequences[i].nextSequence = gzfile.readInt();   // Next sequence
        }
        
        /* KeyFrames */
        InternalKeyFrame[] internalKeyFrames = new InternalKeyFrame[maxKeyFrame+1];
        for(int i=0; i<internalKeyFrames.length; i++) {
            internalKeyFrames[i] = new InternalKeyFrame();
            internalKeyFrames[i].frameIndex = gzfile.readInt(); // Frame Index
            internalKeyFrames[i].angle = gzfile.readInt();      // Angle
            internalKeyFrames[i].flags = gzfile.readInt();      // Flags
            internalKeyFrames[i].pause = gzfile.readInt();      // Pause
        }        
        
        // Control Points
        ArrayList<ControlPoint> controlPoints = new ArrayList<ControlPoint>();
        for (int i = 0; i < nPoints; i++) {
            controlPoints.add (new ControlPoint (
                                    gzfile.readInt(), 
                                    gzfile.readInt(), 
                                    gzfile.readInt()
                                    ));   
        }
        
        for(ControlPoint cp : controlPoints) {
            ag.setControlPoint(cp);
        }
        
        // Image data 
        BufferedImage bi;
        // Note: I decided to duplicate the for loop instead of putting the
        //       if sentence inside to get better perfomance.        
        
        if (depth == DepthMode.DEPTH_8BPP) { // 8bpp          
            
            // The IndexColorModel to store the palette
            IndexColorModel cm = new IndexColorModel(8, 256, palette.getRedComponents(),
                    palette.getGreenComponents(), palette.getBlueComponents());    
            
            DataBufferByte dbb;
            byte[] data; 
            
            for (int i=0; i<maxFrame+1; i++) {
                // Get pixel data from the file
                data = gzfile.readBytes(width * height);
                
                // Create a BufferedImage which uses the IndexColorModel created before
                bi = new BufferedImage(width, height,
                        BufferedImage.TYPE_BYTE_INDEXED, cm);
                
                // To set the data of the BufferedImage we need a data byte buffer where
                // each byte indicates the index of the color of the pixel in the palette
                dbb = new DataBufferByte(data, data.length);
                
                bi.setData (Raster.createRaster (bi.getSampleModel(),dbb,
                        new java.awt.Point(0,0)));
                
                ag.addFrame(bi);
            }            
            
        } else if (depth == DepthMode.DEPTH_16BPP) { /* 16bpp */
            short[] data; 
            DataBufferUShort dbus;
            
            for (int i=0; i<maxFrame+1; i++) {
                data = gzfile.readShorts(width * height);
               
                // Each pixel of the image is stored as a 16 bit number in
                 // 565 format (5 bits for red component, 6 for green and 5 for blue
                 //
                bi = new BufferedImage(width, height,
                        BufferedImage.TYPE_USHORT_565_RGB);
                
                dbus = new DataBufferUShort(data, data.length);
                
                bi.setData(Raster.createRaster(bi.getSampleModel(), dbus,
                        new java.awt.Point(0,0))); 
                
                ag.addFrame(bi);
            }
        }
        
        // Create the AnimatedGraphic Sequences and KeyFrames
        InternalSequence iseq;
        // for (InternalSequence iseq : internalSequences) {
        for (int i = 0; i < internalSequences.length; i++) {
            // Sequence
            //sequence = new Sequence(iseq.name);
            iseq = internalSequences[i];
            ag.addSequence(iseq.name);
            // Frames
            for (int j = iseq.firstKeyFrame; j <= iseq.lastKeyFrame; j++) {
                ag.addKeyFrame(
                        i,
                        internalKeyFrames[j].frameIndex,
                        internalKeyFrames[j].flags,
                        internalKeyFrames[j].angle,
                        internalKeyFrames[j].pause
                        );
            }
        }
        
        // Set nextSequence for each sequence
        // We can not do this in the previous for cause we would need to point
        // to Sequence objects that may not have been created
        for (int i = 0; i < internalSequences.length; i++) {
            ag.setSequenceParams(i, internalSequences[i].nextSequence);
        }
        
        return ag;
    }
}
