// BASIC FAMILY MEMBER . CLASS . TS

import { FamilyMember } from './family-member.interface';
import { Cell } from './cell.class';
import { CellModel } from './cell-model.class';

export abstract class BasicFamilyMember implements FamilyMember {


constructor(public URI: string, public name: string) {}


getURI(): string {
	return this.URI;
}


getAdoptionName(): string {
	return this.name;
}


abstract getAdoptionURI(): string;


abstract getAdoptionOrder(): number;


matches(e: FamilyMember): boolean {
	return this.getAdoptionName()===e.getAdoptionName() && this.getAdoptionURI()===e.getAdoptionURI();
}


abstract canAdopt(newMember:FamilyMember, position?: number): boolean;


abstract childrenCount(): number;


abstract getParent(): FamilyMember;


getAncestors(): FamilyMember[]  {

	let ancestors: FamilyMember[] = [];
	const p = this.getParent();

	if (p) {
		ancestors = p.getAncestors();
		ancestors.unshift(p);
	}

	return ancestors;

}


/** @returns the root ancestor, if it's already the root, returns itself */
getRootAncestor(): FamilyMember {

	const ancestors = this.getAncestors();

	return ancestors.length>0 ? ancestors.pop() : this;

}


equals(m: FamilyMember) {
	return this.getURI()==m.getURI();
}


isCell(): boolean {
	return false;
}


isCellModel(): boolean {
	return false;
}


asCell(): Cell {
	return undefined;
}


asCellModel(): CellModel {
	return undefined;
}


}


/*
 *	  Copyright 2020 Daniel Giribet
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
