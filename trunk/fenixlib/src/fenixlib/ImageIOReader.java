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
 * ImageIOReader.java
 *
 * Created on 4 de abril de 2007
 */

package fenixlib;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class ImageIOReader implements FileReader<AnimatedGraphic>, FenixlibConstants {
    
    private File file;
    
    public ImageIOReader(File f) {
        file = f;
    }
    
    public AnimatedGraphic read() throws IOException {
        
        AnimatedGraphic ag;
        
        BufferedImage buffImage = ImageIO.read(file); // Read the file
        BufferedImage frame; // The final BufferedImage, ready to the AnimatedGraphic
        
        int type = buffImage.getType();
        
        /*if (type == BufferedImage.TYPE_BYTE_BINARY) {
            
            // The frame is directly the buffered image
            frame = buffImage;
            
            // Create an 8bpp animated graphic
            ag = new AnimatedGraphic(
                buffImage.getWidth(),
                buffImage.getHeight(),
                DepthMode.DEPTH_1BPP
                );              
            
        }else */
        if (type == BufferedImage.TYPE_BYTE_INDEXED) { // 8bpp Indexed
            
            // Get the indexed color model associated to the BufferedImage
            IndexColorModel cm = (IndexColorModel)buffImage.getColorModel();
            
            // Get componets
            byte[] reds = new byte[cm.getMapSize()],
                    blues = new byte[cm.getMapSize()],
                    greens = new byte[cm.getMapSize()];
            cm.getReds(reds);
            cm.getGreens(greens);
            cm.getBlues(blues);
            
            // Only consider
            int nColors = java.lang.Math.min(cm.getMapSize(), 256); 
            
            // Create an empty palette and set its colors
            Palette palette = new Palette();
            for (int i = 0; i < nColors; i++)
                palette.getColor(i).setRGB( 
                        ((int)reds[i]) & 0xFF,
                        ((int)greens[i]) & 0xFF,
                        ((int)blues[i]) & 0xFF
                        );
                //palette.setColor(0, new Color(reds[i], greens[i], blues[i]));
            
            // The frame is directly the buffered image
            frame = buffImage;
            
            // Create an 8bpp animated graphic
            ag = new AnimatedGraphic(
                buffImage.getWidth(),
                buffImage.getHeight(),
                palette
                );                 
            
        } else if (type == BufferedImage.TYPE_USHORT_565_RGB) { // 16bpp 565
            
            // The frame is directly the buffered image
            frame = buffImage;
            
            // Create a 16bpp animated graphic
            ag = new AnimatedGraphic(
                buffImage.getWidth(),
                buffImage.getHeight()
                );
            
        } else { // If the image has a type other than the previous, convert it to 16bpp
            
            // Create a compatible BufferedImage
            frame = new BufferedImage(
                buffImage.getWidth(),
                buffImage.getHeight(),
                BufferedImage.TYPE_USHORT_565_RGB
                );
            
            // Create an 16bpp animated graphic
            ag = new AnimatedGraphic(
                buffImage.getWidth(),
                buffImage.getHeight()
                );
            
            // Draw the image
            frame.getGraphics().drawImage(buffImage, 0, 0, null);
        }
        
        // Add the frame to the AnimatedGraphic and create a sequence
        ag.addFrame(frame);
        ag.addSequence("Sequence 1");
        ag.addKeyFrame(0, 0, 0, 0, 0);
        
        return ag;
    }
}

