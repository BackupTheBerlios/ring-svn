/* Ring Ide - The all in one IDE for Fenix.
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
 * SequenceInfo.java
 *
 * Created on 3 de abril de 2007
 */

package fenixlib;

/**
 *  @author Darío Cutillas Carrillo (lord_danko at sourceforge.net)
 */
public final class SequenceInfo {
    public final String name;
    public final int nKeyFrames;
    public final int nextSequence;
    
    SequenceInfo(String name, int nKeyFrames, int nextSequence) {
        this.name = name;
        this.nKeyFrames = nKeyFrames;
        this.nextSequence = nextSequence;
    }
}
