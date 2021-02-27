
// ARROWS . CLASS . TS

import Stack from 'ts-data.stack';

import { Arrow } from './arrow.class';

/** collection of arrows, calculates the minimum visualisation area */
export class Arrows {

#arrows: Arrow[] = [];
#minX: Stack<number> = new Stack<number>();
#minY: Stack<number> = new Stack<number>();
#maxX: Stack<number> = new Stack<number>();
#maxY: Stack<number> = new Stack<number>();


constructor() {}


/** push this arrow if it has not been added already, updates min and max if need be
 *  @returns true if we added the arrow
 */
push(a: Arrow): boolean {

    let addition = false;
    if (!this.alreadyHave(a)) {

        this.#arrows.push(a);
        addition = true;
        
        // now we check if we have to change the min and max values
        this.addMinStack(this.#minX, a.sx) || this.addMaxStack(this.#maxX, a.sx);
        this.addMinStack(this.#minY, a.sy) || this.addMaxStack(this.#maxY, a.sy);
        this.addMinStack(this.#minX, a.ex) || this.addMaxStack(this.#maxX, a.ex);
        this.addMinStack(this.#minY, a.ey) || this.addMaxStack(this.#maxY, a.ey);

    }

    return addition;

}


/** remove this arrow if it exists in the list, updates min and max if need be 
 *  @returns true if we removed the arrow
*/
remove(a: Arrow): boolean {
    
    let removal = false;

    if (this.alreadyHave(a)) {

        this.#arrows = this.#arrows.filter(ca => !ca.equals(a));
        removal = true;

        // now we check if we have to change the min and max values
        this.removeMinStack(this.#minX, a.sx) || this.removeMaxStack(this.#maxX, a.sx);
        this.removeMinStack(this.#minY, a.sy) || this.removeMaxStack(this.#maxY, a.sy);
        this.removeMinStack(this.#minX, a.ex) || this.removeMaxStack(this.#maxX, a.ex);
        this.removeMinStack(this.#minY, a.ey) || this.removeMaxStack(this.#maxY, a.ey);

    }

    return removal;

}


private alreadyHave(a: Arrow):boolean {
    return this.#arrows.find(ca => ca.equals(a))!==undefined;
}


private addMinStack(s: Stack<number>, v: number): boolean {

    let min = false;
    if (s.isEmpty() || s.peek()>=v) {
        s.push(v);
        min = true;
    }

    return min;

}


private removeMinStack(s: Stack<number>, v: number): boolean {

    let min = false;
    if (!s.isEmpty() && s.peek()===v) { // we consider that values will be identical
        s.pop();
        min = true;
    }

    return min;

}


private addMaxStack(s: Stack<number>, v: number): boolean {

    let max = false;
    if (s.isEmpty() || s.peek()<=v) {
        s.push(v);
        max = true;
    }

    return max;

}


private removeMaxStack(s: Stack<number>, v: number): boolean {
    return this.removeMinStack(s, v);   // the implementation is the same but will change if needed
}


get length(): number {
    return this.#arrows.length;
}


get list(): Arrow[] {
    return this.#arrows;
}


get minX(): number {
    return this.#minX.peek();
}


get minY(): number {
    return this.#minY.peek();
}


get maxX(): number {
    return this.#maxX.peek();
}


get maxY(): number {
    return this.#maxY.peek();
}


}

/*
 *	  Copyright 2021 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */