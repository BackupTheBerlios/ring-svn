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

import java.awt.image.BufferedImage;
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
 * @see MapReader
 * @see FbmReader
 * @see MapWriter
 * @see FbmWriter
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
    
    private AnimatedGraphic (int width, int height, DepthMode depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
    
//    /**
//     * Creates a new 16bpp <code>AnimatedGraphic</code> with the specified width and
//     * height
//     * @param width the desired width
//     * @param height the desired height
//     */
//    private AnimatedGraphic (int width, int height) {
//        
//        this.width = width;
//        this.height = height;
//        this.depth = DepthMode.DEPTH_16BPP;
//
//    }
    
//    /**
//     * Creates a new 8bpp (indexed) <code>AnimatedGraphic</code> with the specified 
//     * width and height and an underlaying palette.
//     * @param width the desired width
//     * @param height the desired height
//     * @param palette a <code>Palette</code> object to be used as the indexed palette
//     * for the graphic
//     */
//    private AnimatedGraphic (int width, int height, Palette palette) {
//        this.width = width;
//        this.height = height;
//        this.depth = DepthMode.DEPTH_8BPP;
//        this.palette = palette;
//    }
    
    public static AnimatedGraphic create8(int width, int height, Palette palette) {
        AnimatedGraphic ag;
                
        if(palette == null) {
            throw new IllegalArgumentException("Cannot create an 8bpp graphic" +
                    "without a palette");
        }
        
        ag = new AnimatedGraphic(width, height, DepthMode.DEPTH_8BPP);
        ag.palette = palette;
        return ag;
    }
    
    public static AnimatedGraphic create16(int width, int height) {
        AnimatedGraphic ag;
        
        ag = new AnimatedGraphic(width, height, DepthMode.DEPTH_16BPP);
        return ag;
    }
    
    /**
     * Adds a sequence to the graphic.
     * @param name a descriptive name for the sequence
     */
    public void addSequence(String name) {
        Sequence seq = new Sequence(name);
        sequences.add(seq);
    }    
    
    /**
     * Adds a sequence to the graphic.
     */
    public void addSequence() {
        Sequence seq = new Sequence();
        sequences.add(seq);
    }      

    /**
     * Returns the number of sequences of the <code>AnimatedGraphic</code>
     * @return the number of sequences of the <code>AnimatedGraphic</code>
     */
    public int getSequenceCount() {
        return sequences.size();
    }
    
    /**
     * Returns a <code>SequenceInfo</code> object which contains information
     * about the specified sequence. See <code>SequenceInfo</code> documentation
     * for more information.
     * @param seqIndex the index of the sequence to get information from
     * @return a <code>SequenceInfo</code> object with information about the specified sequence
     * @see SequenceInfo
     */
    public SequenceInfo getSequenceInfo(int seqIndex) {
        Sequence sequence = sequences.get(seqIndex); // Get the sequence
        int nextseqIndex = sequences.indexOf(sequence.getNextSequence());
        
        return new SequenceInfo(
                sequence.getName(),
                sequence.keyFrames.size(),
                nextseqIndex
                );
    }
    
    /**
     * Returns an array of <code>SequenceInfo</code> objects which contains information
     * about the sequences of the graphic. See <code>SequenceInfo</code> documentation
     * for more information.
     * @return an array of <code>SequenceInfo</code> objects with information about 
     * all the sequences in the graphic.
     * @see SequenceInfo
     */
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
    
    /**
     * Sets some sequence parameters.
     * @param seqIndex the index of the sequence whose parameters are being changed
     * @param name the new desired descriptive name for the sequence
     */
    public void setSequenceParams(int seqIndex, String name) {
        Sequence seq = sequences.get(seqIndex);
        seq.setName(name);
    }
    
    /**
     * Sets some sequence parameters.
     * @param seqIndex the index of the sequence whose parameters are being changed
     * @param nextSequence the index of the following sequence in the animation. Can be -1 if the animation
     * must stop after finishing the sequence.
     */
    public void setSequenceParams(int seqIndex, int nextSequence) {
        Sequence seq = sequences.get(seqIndex);
        Sequence next;
        
        if (nextSequence != -1)
            next = sequences.get(nextSequence);
        else
            next = null;
        
        seq.setNextSequence(next);        
    }
    
    /**
     * Sets some sequence parameters.
     * @param seqIndex the index of the sequence whose parameters are being changed
     * @param name the new desired descriptive name for the sequence
     * @param nextSequence the index of the following sequence in the animation. Can be -1 if the animation
     * must stop after finishing the sequence.
     */
    public void setSequenceParams(int seqIndex, String name, int nextSequence) {
        setSequenceParams(seqIndex, name);
        setSequenceParams(seqIndex, nextSequence);
    }
    
    public void addKeyFrame(int seqIndex, int frameIndex, int flags,  int angle, int pause) {
        Sequence sequence = sequences.get(seqIndex); // get the sequence
        
        // add the keyframe to the sequence
        KeyFrame kf = new KeyFrame(flags, angle, pause); 
        kf.frame = frames.get(frameIndex);
        
        sequence.keyFrames.add(kf);
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
    
    public void setKeyFrameParams(int seqIndex, int kfIndex, int frameIndex,  
            int flags, int angle, int pause) {
            sequences.get(seqIndex).keyFrames.get(kfIndex).frame = 
                    frames.get(frameIndex);
            sequences.get(seqIndex).keyFrames.get(kfIndex).setFlags(flags);
            sequences.get(seqIndex).keyFrames.get(kfIndex).setAngle(angle);
            sequences.get(seqIndex).keyFrames.get(kfIndex).setPause(pause);
    }

    /**
     * Returns the number of frames of the <code>AnimatedGraphic</code>
     * @return the number of frames of the <code>AnimatedGraphic</code>
     */
    public int getFrameCount() {
        return frames.size();
    }
    
    /**
     * Adds a new frame to the graphic. 
     * 
     * Frames represent different versions of the  graphic (with the same size). 
     * Internally, frames are stored as buffered images and you can get an array with 
     * all frames using <code>getFrames()</code> method.
     * 
     * Frames are independent of keyframes and sequences so a graphic can 
     * contain multiple frames but use only one.
     * 
     * Actually, a frame is nothing different but a <code>BufferedImage</code> object,
     * which can be of type TYPE_BYTE_INDEXED or TYPE_USHORT_565_RGB. This method
     * will only accept buffered images whose type is one of them so you must perform
     * the conversion yourself, otherwise an IllegalArgumentException will be thrown.
     * Also note that you can only add TYPE_BYTE_INDEXED buffered images for 8bpp
     * graphics while TYPE_USHORT_565_RGB buffered images are reserved for 16bpp. An
     * IllefalArgumentException will be thrown if you don't follow this rule.
     * 
     * When adding a TYPE_BYTE_INDEXED buffered image you must note that colors of its
     * "indexed palette" (more exactly, colors of its <code>IndexedColorModel</code>)
     * may differ from the colors of the graphic palette and no color space conversion
     * is performed automatically.
     * @param img the <code>BufferedImage</code> to be used as a frame. Only TYPE_BYTE_INDEXED 
     * and TYPE_USHORT_565_RGB buffered image types are allowed, the first for using
     * with 8bpp graphics and the last for 16bpp graphics.
     */
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
    
    /**
     * Gets an array of <code>BufferedImage</code> objects representing all the frames
     * of the image. See <code>addFrame</code> for a more detailed description about
     * frames.
     * @return an array of <code>BufferedImages</code> objects containing all the frames of
     * the image
     * @see addFrame
     */
    public BufferedImage[] getFrames() { 
        return frames.toArray(new BufferedImage[0]);
    }
    
    /**
     * Gets the frame whose index is <code>frameIndex</code>. See <code>addFrame</code> 
     * for a more detailed description about frames.
     * @param frameIndex the index of the desired frame
     * @return a <code>BufferedImage</code> object that represents the frame
     * @see addFrame
     */
    public BufferedImage getFrame(int frameIndex) {
        return frames.get(frameIndex);
    }      
 
}
