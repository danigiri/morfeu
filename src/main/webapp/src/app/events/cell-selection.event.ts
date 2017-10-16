/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import { FamilyMember } from "../family-member.interface";

export class CellSelectionEvent {

public 
    
constructor(public position:number, public parents:FamilyMember[], public completed:boolean = false) {}


public toString = () : string => {
    return "CellSelectionEvent:{position:"+this.position
    +", parents:["+this.parents.map(p => p.getAdoptionName()+",")+"]}";
}

}