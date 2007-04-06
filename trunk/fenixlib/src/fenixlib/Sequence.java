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
 * Sequence.java
 *
 * Created on 1 de abril de 2007
 */

package fenixlib;

import java.util.ArrayList;

/** A class which contains a sequence of frames and can optionaly point to another
 *  sequence.
 *  @autor Dar�o Cutillas Carrillo (lord_danko at users.sourceforge.net)
 */
class Sequence {
    
    ArrayList<KeyFrame> keyFrames = new ArrayList<KeyFrame>();
    
    private String name = "";
    private Sequence nextSequence = null;
    
    public Sequence() {}
    
    public Sequence(String name) {
        this.name = name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    Sequence getNextSequence() {
        return nextSequence;
    }
    
    void setNextSequence(Sequence ns) {
        nextSequence = ns;
    }
    
}
