/* fenixlib - Library to support Fenix Files in Java
 * Copyright (C) 2007  Dar�o Cutillas Carrillo
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
 * FileWriter.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import java.io.IOException;

/**
 *  @author Dar�o Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public interface FileWriter<T> {
    
    /** Creates a new instance of FileWriter */
    public void write(T t) throws IOException;

}
