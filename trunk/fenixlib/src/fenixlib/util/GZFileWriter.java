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
 * GZFileWriter.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.GZIPOutputStream;

/** Basic gzfile writer
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class GZFileWriter {
private ByteArrayOutputStream baos;
    
    public GZFileWriter() {
        baos = new ByteArrayOutputStream();
    }
    
    public void writeByte(byte b){
        baos.write(b);
    }
    public void writeBytes(byte[] bytes){
        baos.write(bytes,0,bytes.length);
    }
    
    public void writeShort(short s) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN).putShort(s);
        byte[] bytes = bb.array();
        baos.write(bytes,0,bytes.length);
    }
    
    public void writeShorts(short[] shorts) {
        ByteBuffer bb = ByteBuffer.allocate(2*shorts.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for(int i=0; i<shorts.length; i++)
            bb.putShort(shorts[i]);
        byte[] bytes = bb.array();
        baos.write(bytes,0,bytes.length);
    }
    
    public void writeInt(int i){
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN).putInt(i);
        byte[] bytes = bb.array();
        baos.write(bytes,0,bytes.length);
    }
    
    public void writeAsciiZ(String s, int maxlen) throws UnsupportedEncodingException{
        byte[] bytes=new byte[maxlen];
        byte[] strBytes=s.getBytes("US-ASCII");
        int i;
        for(i=0; i<s.length() && i<maxlen; i++)
            bytes[i]=strBytes[i];
        for(int j=i;j<maxlen; j++)
            bytes[j]=0x00;
        baos.write(bytes,0,bytes.length);
    }
    
    public void toFile(File file) throws IOException {
        GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(file));
        baos.writeTo(gzos);
        gzos.close();
    }
}
