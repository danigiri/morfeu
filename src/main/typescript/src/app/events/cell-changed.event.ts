// CELL - CHANGED . EVENT . TS

import {Cell} from '../cell.class';

export class CellChangedEvent {

constructor(private cell: Cell) {}


public toString = (): string => {
	return 'CellChangedEvent:{'+this.cell.URI+'}';
}

}
