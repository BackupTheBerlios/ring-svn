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
 * GZFileReader.java
 *
 * Created on 31 de marzo de 2007
 */

package fenixlib.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.zip.GZIPInputStream;

/** Provides basic functionality to open and read files optionaly compressed with GZip. <br />
 *  Files are readed and decompressed entirely when the constructor is called and data is stored in an intermeriary buffer.
 *  <h3>Accessing binary data</h3>This class provides methods from reading values from different primitive types (and arrays of them).
 *  Reading is performed allways using the LITTLE ENDIAN byte order.
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public class GZFileReader {
    private static final int INFLATER_BUFF_SIZE = 1048576;	/* Memory used to decompress. Currently 1MB */
    private final ByteBuffer byteBuffer;
    
    /** Creates a <code>GZFileReader</code> which will hold an internal buffer to store
     *  the file <code>file</code> decompressed.
     *  @param file the file to be opened represented by a <code>File</code> object
     */
    public GZFileReader(File file) throws IOException {
        /* Decompression of the file is performed following this steps:
         *	- File is opened in a FileInputStream and filtered with a DataInputStream. The stream is buffered using a
         *	  BufferInputStream so that mark and reset operations are supported.
         *	- First two bytes of the file are read to determine if the file is compressed (0x1f 0x8b).
         *	- If the file is a GZ file then create an GZFileInputStream based on the existing DataInputStream. This will be our inpuStream
         *	- If not, our inputStream will be the DataInputStream created in the first step.
         *	- Finally, we read the entire file (inflated if GZ) and create a byte buffer for accesing later.
         */
        
        /* Create a DataInputStream to read bytes of the file  */
        DataInputStream dataInputStream = new DataInputStream(
                new BufferedInputStream(
                new FileInputStream(file) ) );
        /* Read first two bytes (GZIP MAGIC)  to dtermine if it is a GZIP File */
        int[] magic = new int[2];
        dataInputStream.mark(2);
        magic[0]=dataInputStream.readUnsignedByte();
        magic[1]=dataInputStream.readUnsignedByte();
        dataInputStream.reset();	/* Reset so the dataInputStream points at the begining of the file */
        InputStream inputStream;	/* The final stream used to read the file */
        if( magic[0]==0x1f && magic[1]==0x8b ) { 	/* Is a GZIP file */
            /* The inputStream object to read bytes needs to by a GZIPInputStream to allow decompressing reading */
            inputStream = new GZIPInputStream(dataInputStream);
        }else { 	/* Not a GZIP file */
            inputStream = dataInputStream;
        }
        /* Read uncompressed data and put it into a ByteArrayOutputStream object */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[INFLATER_BUFF_SIZE];
        int len;
        while((len=inputStream.read(buffer,0,buffer.length)) != -1) {
            baos.write(buffer,0,len);
        }
        inputStream.close(); 	/* Close the inputStream  */
        /* Wrap uncompressed file bytes into the ByteBuffer */
        byteBuffer=ByteBuffer.wrap(baos.toByteArray());
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);		/* Our gzfiles follow intel LittleEndian byte order */
    }
    
    /** Reads next byte of the buffer and returns it as a byte value.
     *  @return the next byte of the buffer
     */
    public byte readByte() {return byteBuffer.get();};
    
    /** Reads next byte of the buffer and returns an int value in the range 0 through 255.
     *  version of the byte
     *  @return the next byte of the buffer, interpreted as an unsigned 8-bit number
     */
    public int readUnsignedByte() {	return (byteBuffer.get() & 0xff); }
    
    /** Reads an array of bytes from the buffer of a len <code>len</code>
     *  @param len the number of bytes to be read
     *  @return an array of bytes containing the next <code>len</code> bytes of
     *  the buffer
     */
    public byte[] readBytes(int len) {
        byte[] buff = new byte[len];
        byteBuffer.get(buff);
        return buff;
    }
    
    /** Reads next 2 bytes of the buffer and returns them as a short value
     *  @return a short which represent the next two bytes of the buffer
     */
    public short readShort() {return byteBuffer.getShort();}
    
    /** Reads next 2 bytes and returns an int value in the range 0  through 65535.
     *  @return the next 2 byte of the buffer, interpreted as an unsigned 16-bit number
     */
    public int readUnsignedShort() {
        return (int)byteBuffer.getShort() &  0xffff; //COMPROBAR ESTO
    }
    
    /** Reads an array of short from the buffer of a len <code>len</code>
     *  @param len the number of shorts to be read
     *  @return an array of shorts containing the next <code>len</code>*2 bytes of
     *  the buffer
     */
    public short[] readShorts(int len) {
        short[] buff = new short[len];
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.get(buff);
        return buff;
    }
    
    /** Reads next 4 bytes of the buffer and returns them as an int value
     *  @return an int value which represent the next 4 bytes of the buffer
     */
    public int readInt() {return byteBuffer.getInt(); }
    
    /** Reads next 4 bytes and returns an long value in the range 0  through 2^32-1.
     *  @return the next 4 byte of the buffer, interpreted as an unsigned 32-bit number
     */
    public long readUnsignedInt() {
        return byteBuffer.getInt() & 0xffffffffL;
    }
    
    /** Reads an array of int from the buffer of a len <code>len</code>
     *  @param len the number of ints to be read
     *  @return an array of int containing the next <code>len</code>*4 bytes of
     *  the buffer
     */
    public int[] readInts(int len) {
        int[] buff = new int[len];
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.get(buff);
        return buff;
    }
    
    /** Reads next 8 bytes of the buffer and returns them as a long value
     *  @return a long value which represent the next 8 bytes of the buffer
     */
    public long readLong() {return byteBuffer.getLong(); }
    
    /** Reads an array of long from the buffer of a len <code>len</code>
     *  @param len the number of longs to be read
     *  @return an array of long containing the next <code>len</code>*8 bytes of
     *  the buffer
     */
    public long[] readLongs(int len) {
        long[] buff = new long[len];
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        longBuffer.get(buff);
        return buff;
    }
    
    /** Reads next <code>len</code> bytes of the buffer and returns an String
     *  which results from interpreting the bytes as an AsciiZ string (a
     *  fixed-length string where the last character is 0x00)
     *  @param len the maximun len of the string
     *  @return the AsciiZ string interpreted as an String object
     */
    public String readAsciiZ(int len) {
        byte[] buff = readBytes(len);
        int i;
        for(i=0; i<len; i++)
            if(buff[i]==0x00)
                break;
        return (new String(buff,0,i));
    }
    
    /** Skips next <code>n</code> bytes
     *  @param n number of bytes to skip
     */
    public void skip(int n) { byteBuffer.position(byteBuffer.position()+n); }    
    
}
