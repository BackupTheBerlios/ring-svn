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
 * FbmWriter.java
 *
 * Created on 4 de abril de 2007
 */

package fenixlib;

import fenixlib.util.GZFileWriter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;


/**
 * An implementation of the <code>FileWriter</code> interface to write Fbm Fenix
 * files.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see FileWriter
 */
public class FbmWriter implements FileWriter<AnimatedGraphic>, FenixlibConstants {
    
    private File file;
    
    /* Version code constants */
    private static final short VERSION_MAJOR = 0x0100;
    private static final short VERSION_MINOR = 0x0000;      
    

    /**
     * Constructs a new <code>FbmWriter</code> associated to the specified file.
     * @param f a <code>File</code> object which specifies the file to be used by 
     * different methods
     * of the class
     */
    public FbmWriter(File f) {
        file = f;
    }
    
    
    /**
     * Writes an Fbm file from the information in an <code>AnimatedGraphic</code>
     * object.
     * @param ag the <code>AnimatedGraphic</code> whose information is being used to write
     * the file
     * @throws java.io.IOException if any error occurrs during the writing process
     */
    public void write (AnimatedGraphic ag) throws IOException { 
        
       GZFileWriter gzfile = new GZFileWriter();
        
       // Generate InternalSequence and InternalKeyFrame structures from the
       // data in the AnimatedGraphic   
       SequenceInfo [] seqsInfo = ag.getSequencesInfo();
       KeyFrameInfo [] kfsInfo;
       
       InternalSequence[] intSeqs = 
                    new InternalSequence[seqsInfo.length]; 
       ArrayList<InternalKeyFrame> intKFs = new ArrayList<InternalKeyFrame>();
       InternalKeyFrame intKF;
       
       int keyFrameCount = 0; 
       for (int i = 0; i < seqsInfo.length; i++) {
           intSeqs[i] = new InternalSequence();
           intSeqs[i].name = seqsInfo[i].name;
           intSeqs[i].nextSequence = seqsInfo[i].nextSequence;
           intSeqs[i].firstKeyFrame = keyFrameCount;
           keyFrameCount += seqsInfo[i].nKeyFrames - 1;
           intSeqs[i].lastKeyFrame = keyFrameCount;
           
           kfsInfo = ag.getKeyFramesInfo(i);
           for (KeyFrameInfo kfInfo : kfsInfo) {
               intKF = new InternalKeyFrame();
               intKF.frameIndex = kfInfo.frameIndex;
               intKF.flags = kfInfo.flags;
               intKF.angle = kfInfo.angle;
               intKF.pause = kfInfo.pause;
               intKFs.add (intKF);
           }
           
           keyFrameCount++;
       }  
        
        // Animation parameters
        int maxFrame = ag.getFrames().length;
        int maxKeyFrame = intKFs.size();
        int maxSequence = intSeqs.length;
        
        // Header
        gzfile.writeAsciiZ(FBM_MAGIC,16);
        gzfile.writeShort(VERSION_MAJOR);
        gzfile.writeShort(VERSION_MINOR);              
        gzfile.writeInt(ag.getDepth().toInt());     // Depth
        
        // Header extension (descriptor)
        gzfile.writeAsciiZ(ag.getName(), 64);   // Name
        gzfile.writeInt(ag.getWidth());    // Width
        gzfile.writeInt(ag.getHeight());   // Height
        gzfile.writeInt(ag.getFlags());    // Flags
        gzfile.writeInt(ag.getId());       // Id
        gzfile.writeInt(maxFrame - 1);      // Max frame
        gzfile.writeInt(maxSequence - 1);   // Max sequence
        gzfile.writeInt(maxKeyFrame - 1);   // Max keyframe
        
        int lastCp, nCp = 0;
        nCp = ag.getControlPoints().length;
        lastCp = (nCp == 0 ? 0 : ag.getLastControlPoint().getIndex());
        gzfile.writeInt(lastCp);            // Max control point index
        gzfile.writeInt(nCp);               // Number of cps

        // Palette (8bpp)
        if (ag.depth==DepthMode.DEPTH_8BPP) {
            Color c;
            for(int i=0; i<256; i++) {
                c = ag.getPalette().getColor(i);
                gzfile.writeByte ((byte)c.getRed());
                gzfile.writeByte ((byte)c.getGreen());
                gzfile.writeByte ((byte)c.getBlue());
            }
        }
        
        // Sequences
        for (InternalSequence internalSeq : intSeqs) {
            gzfile.writeAsciiZ(internalSeq.name, 32);
            gzfile.writeInt(internalSeq.firstKeyFrame);     // First keyframe
            gzfile.writeInt(internalSeq.lastKeyFrame);      // Last keyframe
            gzfile.writeInt(-1);                            // Next sequence
        }
        
        // KeyFrames
        for (InternalKeyFrame internalKeyFrame : intKFs) {
            gzfile.writeInt(internalKeyFrame.frameIndex);       // Frame Index
            gzfile.writeInt(internalKeyFrame.angle);            // Angle
            gzfile.writeInt(internalKeyFrame.flags);            // Flags
            gzfile.writeInt(internalKeyFrame.pause);            // Pause 
        }
        
        // Control Points
        for (ControlPoint cp : ag.getControlPoints()) {
            gzfile.writeInt(cp.getIndex());
            gzfile.writeInt(cp.getX());
            gzfile.writeInt(cp.getY());
        }
        
        // Graphic data
        Raster raster;
        
        switch (ag.getDepth()) {
           /*
            case DEPTH_1BPP:
                {
                    // For 1BPP we need to align data so as each scanline is 
                    // ocupate
                    DataBufferByte dbb;
                    byte[] alignedData; // An ArrayList to perform the alignment
                    byte[] data;
                    
                    int w = ag.getWidth();
                    int padding = w % 8; // Aligned bits (must be set to 0)
                    // Write each fram
                    for (BufferedImage bi : ag.getFrames()) {
                        // Get image data
                        raster = bi.getData();
                        data = ((DataBufferByte)raster.getDataBuffer()).getData();
                        
                        // Create a byte array to store aligned data
                        alignedData = new byte[(w + padding) * ag.getHeight()];
                        Arrays.fill (alignedData, (byte)0); // Fill the entire array with 0
                        // Copy data to alignedData
                        for(int i = 0; i < ag.getHeight(); i++) {
                            System.arraycopy(data, i * w, alignedData, i * (w + padding), w);
                        }
                        
                        gzfile.writeBytes(alignedData);
                    }               
                    

                }
                //break;
            */
            
            case DEPTH_8BPP:
                {
                    DataBufferByte dbb;
                    byte[] data;
                    // Write each frame
                    for (BufferedImage bi : ag.getFrames()) {
                        raster = bi.getData();
                        dbb = (DataBufferByte)raster.getDataBuffer();
                        data = dbb.getData();
                        gzfile.writeBytes(data);
                    }   
                }
                break;
                
            case DEPTH_16BPP:
                {
                    DataBufferUShort dbus;
                    short[] data;
                    // Write each frame
                    for (BufferedImage bi: ag.getFrames()) {
                        raster = bi.getData();    
                        dbus = (DataBufferUShort)raster.getDataBuffer();
                        data = dbus.getData();    
                        gzfile.writeShorts(data);
                    }
                }
                break;
        }           
        
        gzfile.toFile(file);        
    }
}
