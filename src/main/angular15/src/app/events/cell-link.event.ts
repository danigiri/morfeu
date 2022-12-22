// CELL - LINK . EVENT . TS

import { MorfeuEvent } from './morfeu-event.class';

import { Cell } from '../cell.class';
import { Rect } from '../utils/rect.class';

/** this is sent only with a source */
export class CellLinkEvent extends MorfeuEvent {


constructor(public source: Cell, public destination: Cell, public sourceRect?: Rect, public destRect?: Rect) {
	super('CellLinkEvent');
}


public override toString = (): string => {

	const sourceRect = this.sourceRect ? '('+this.sourceRect+')' : ''
	const destRect = this.destRect ? '('+this.destRect+')' : ''

	return `CellLinkEvent:{${this.source.name}${sourceRect} -> ${this.destination.name}${destRect}`;

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
