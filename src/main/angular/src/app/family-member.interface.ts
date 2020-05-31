// FAMILY - MEMBER . INTERFACE . TS

/**	 we can have /test(0)/foo(1)
*	 which accepts children named 'bar' of type /model/bar
*	 AdoptionName is 'bar', and model (adoption uri) is '/model/bar'
*	 Elements named 'fubar' of model '/model/bar' are not accepted into the family
*	 So would be elements named 'bar' but with model '/model/whatever'
*/
export interface FamilyMember {

/** unique location */
getURI(): string;

/** name of element in respect to creating new family members or adopting ones, in the example 'bar' */
getAdoptionName(): string;

/** URI in respect of creating new family members ar adopting ones, in the example '/model/bar' */
getAdoptionURI(): string;

/** if adoption is done in order, this will be the order of this kind (model) of family members, or undefined */
getAdoptionOrder(): number;

/** are we the same? (used to highlight siblings), to cells of equal name and model, match */
matches(e:FamilyMember): boolean;

/** can we adopt this new family member? (usually means can have children with given name and adoption uri) */
canAdopt(newMember:FamilyMember, position?: number): boolean;

//canRemoveAsChild(cell:Cell):boolean;

/** return the number of children */
childrenCount(): number;

/** reference to the parent */
getParent(): FamilyMember;

/** returns a list of ancestors, starting with the parent and up to the content */
getAncestors(): FamilyMember[];

/** exactly the same? */
equals(m:FamilyMember): boolean;

}

/*
 *	  Copyright 2019 Daniel Giribet
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