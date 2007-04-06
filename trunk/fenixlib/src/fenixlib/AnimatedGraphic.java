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
 * AnimatedGraphic.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;


/**
 * A convenient class to represent an animated graphic. An 
 * <code>AnimatedGraphic</code> consist of <b>sequences</b>, <b>keyframes</b>
 * and <b>frames</b>:
 * <ul>
 *    <li><b>Frames</b> are different versions of the graphic. They are
 *    stored as BufferedImages and must be all of the same size.</li>
 *    <li>A <b>keyframes</b> is one of the <i>moments</i> of the animation.
 *    Keyframes store information about the frame to show and some parameters
 *    such as flags, angle and time to wait to the next frame (pause).
 *    <li>A <b>sequence</b> group different keyframes to compound an animation.
 *    An animated graphic can have more than one sequence.
 * </ul>
 * 
 * Note that there is no specific class to represent static graphics. This
 * is not a problem since an <code>AnimatedGraphic</code> can contain a
 * unique frame, keyframe and sequence.
 * 
 * This class does not provides any way to load or save images from files
 * (MAP, FBM, PNG, etc.). You must use <code><em>FILETYPE</em>Reader</code>
 * and <code><em>FILETYPE</em>Writer</code> classes to do so.
 * @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 * @see MapReader, FbmReader, MapWriter, FbmWriter
 */
public class AnimatedGraphic extends AbstractGraphic{
    
    // An array list containing sequences
    private ArrayList<Sequence> sequences = new ArrayList<Sequence>();
    
    // An array list containing all frame buffered images 
    // NOTE: Each keyframe has a buffered image associated but since different
    // KeyFrames can point to the same buffered image, we store here the references
    // to all buffered images of the graphic. This way, we avoid having duplicated
    // data.
    private ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
    
    public AnimatedGraphic (int width, int height) {
        
        this.width = width;
        this.height = height;
        this.depth = DepthMode.DEPTH_16BPP;

    }
    
    /*public AnimatedGraphic (int width, int height, DepthMode depthMode) {
        
        this.width = width;
        this.height = height;
        if (depthMode == DepthMode.DEPTH_8BPP)      
            throw new IllegalArgumentException("Cannot use constructor with" +
                    "DEPTH_8BPP mode");
        
        depth = depthMode;
        
    }*/
    
    public AnimatedGraphic (int width, int height, Palette palette) {
        
        this.width = width;
        this.height = height;
        this.depth = DepthMode.DEPTH_8BPP;
        this.palette = palette;
        
    }
    

    public SequenceInfo getSequenceInfo(int seqIndex) {
        Sequence sequence = sequences.get(seqIndex); // Get the sequence
        int nextseqIndex = sequences.indexOf(sequence.getNextSequence());
        
        return new SequenceInfo(
                sequence.getName(),
                sequence.keyFrames.size(),
                nextseqIndex
                );
    }
    
    public SequenceInfo[] getSequencesInfo() {
        // An ArrayList to store all Info from sequences
        ArrayList<SequenceInfo> seqsInfo = new ArrayList<SequenceInfo>();
        
        int nextseqIndex;
        for(Sequence sequence : sequences) {
            nextseqIndex = sequences.indexOf(sequence.getNextSequence());
            seqsInfo.add(new SequenceInfo(
                    sequence.getName(),
                    sequence.keyFrames.size(),
                    nextseqIndex                    
                    ));
        }
        
        return seqsInfo.toArray(new SequenceInfo[0]);
    }
    
    public KeyFrameInfo getKeyFrameInfo(int seqIndex, int kfIndex) {
        Sequence sequence = sequences.get(seqIndex); // Get the sequence
        KeyFrame kf = sequence.keyFrames.get(kfIndex); // Get the keyframe
        
        // Get the index of the frame pointed by this keyframe
        int frameIndex = frames.indexOf(kf.getFrame()); 
        
        return new KeyFrameInfo(
                frameIndex,
                kf.getFlags(),
                kf.getAngle(),
                kf.getPause()
                );
    }
    
    public void setSequenceParams(int seqIndex, String name, int nextSequence) {
        setSequenceParams(seqIndex, name);
        setSequenceParams(seqIndex, nextSequence);
    }
    
    public void setSequenceParams(int seqIndex, String name) {
        Sequence seq = sequences.get(seqIndex);
        seq.setName(name);
    }
    
    public void setSequenceParams(int seqIndex, int nextSequence) {
        Sequence seq = sequences.get(seqIndex);
        Sequence next;
        
        if (nextSequence != -1)
            next = sequences.get(nextSequence);
        else
            next = null;
        
        seq.setNextSequence(next);        
    }
    
    public KeyFrameInfo[] getKeyFramesInfo(int seqIndex) {
        Sequence sequence = sequences.get(seqIndex); // Get the sequence
        
        ArrayList<KeyFrameInfo> keyFramesInfo = new ArrayList<KeyFrameInfo>();
        int frameIndex;
        for (KeyFrame kf : sequence.keyFrames) { // Get info for each KF in the sequence
            frameIndex = frames.indexOf(kf.getFrame());
            keyFramesInfo.add( new KeyFrameInfo(
                            frameIndex,
                            kf.getFlags(),
                            kf.getAngle(),
                            kf.getPause()
                            ));
        }
        
        return keyFramesInfo.toArray(new KeyFrameInfo[0]);
    }
    
    public void addSequence(String name) {
        Sequence seq = new Sequence(name);
        sequences.add(seq);
    }
    
    public void addKeyFrame(int seqIndex, int frameIndex, int flags,  int angle, int pause) {
        Sequence sequence = sequences.get(seqIndex); // get the sequence
        
        // add the keyframe to the sequence
        KeyFrame kf = new KeyFrame(flags, angle, pause); 
        kf.frame = frames.get(frameIndex);
        
        sequence.keyFrames.add(kf);
    }
    
    public void addFrame(BufferedImage img) {
        // buffImage must have the same dimensions than the AnimatedGraphic
        // and its backing buffer can only be TYPE_BYTE or TYPE_USHORT.
        
        if (img.getWidth() != width || img.getHeight() != height)
            throw new IllegalArgumentException("Frames dimensions must be the same");
        
        // Get and check data type
        int dataType = img.getType();
        
        switch (dataType) {
            /* case BufferedImage.TYPE_BYTE_BINARY:
                if (depth != depth.DEPTH_1BPP) 
                    throw new IllegalArgumentException (
                            "Depth must be the same");      
                
                break;
             */
            
            case BufferedImage.TYPE_BYTE_INDEXED:
                if (depth != depth.DEPTH_8BPP) 
                    throw new IllegalArgumentException (
                            "Depth must be the same");
                
                IndexColorModel cm = (IndexColorModel)img.getColorModel();
                
                // Check the number of colors in the ColorModel
                if (cm.getMapSize() != 256)
                    throw new IllegalArgumentException (
                            "Only 256 color palettes are allowed.");
                
                break;
            
            case BufferedImage.TYPE_USHORT_565_RGB:
                if (depth != depth.DEPTH_16BPP) 
                    throw new IllegalArgumentException (
                            "Depth must be the same");   
                
                break;
                
            default:
                throw new IllegalArgumentException ("Only TYPE_BYTE_INDEXED" +
                        " and TYPE_USHORT_565_RGB BufferedImages allowed. " +
                        "Used: " + dataType );
        }         
        
        frames.add(img);
    }
    
    public BufferedImage[] getFrames() { 
        return frames.toArray(new BufferedImage[0]);
    }
    
    public BufferedImage getFrame(int frameIndex) {
        return frames.get(frameIndex);
    }      
}
