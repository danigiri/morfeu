/*
 *	  Copyright 2018 Daniel Giribet
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


import { Cell } from "./cell.class";
import { FamilyMember } from "./family-member.interface";
import { Type_ } from "./type_.class";

// //// COMPONENT STUFF ////
import { Subscription } from 'rxjs/Subscription';
import { SelectableWidget } from './selectable-widget.class';
import { CellModelActivatedEvent } from "./events/cell-model-activated.event";
import { CellSelectEvent } from './events/cell-select.event';
import { CellSelectionClearEvent } from './events/cell-selection-clear.event';
import { EventService } from './events/event.service';
// //// PRESENT HERE DUE TO LIMITATIONS IN TREE COMPONENT ////

export class CellModel implements FamilyMember {

id: string;
isExpanded: boolean;

attributes?: CellModel[];
children: CellModel[];
isReference: boolean;
referenceURI?: string;

constructor(public schema: number, 
			public URI: string, 
			public name: string, 
			public desc: string, 
			public presentation: string,
			public cellPresentation: string,
			public thumb: string,
			public isSimple: boolean, 
			public type_: Type_,
			public minOccurs: number,
			public maxOccurs?: number,
			public defaultValue?: string
			) {
	this.init();
}	 


// there are values specific to comply wit the treemodel model, we set them explicitly here 
init() {

	this.id = this.URI;	 // this is guaranteed to be unique 
	this.isExpanded = true;

}


getURI():string {
	return this.URI;
}


getAdoptionName():string {
	return this.name;
}


getAdoptionURI():string {
	return this.URI;
}


matches(e:FamilyMember):boolean {
	return this.getAdoptionName()==e.getAdoptionName() && this.getAdoptionURI()==e.getAdoptionURI();
}
	

canAdopt(element:FamilyMember):boolean {
	return this.children.some(c => c.matches(element));
}


childrenCount():number {
	return this.children ? this.children.length : 0;
}


equals(m:FamilyMember) {
	return this.getURI()==m.getURI();
}


getParent():FamilyMember {
	return undefined;	//TODO: we do not need to setup the parent yet
}


getPresentation() {
	return (this.cellPresentation=='DEFAULT') ? "assets/images/cell.svg" : this.cellPresentation;
}


canGenerateNewCell(): boolean {
	// not dragging WELL-type cell models for the moment, as it is quite a complex use case, so we only allow
	// cell models that do not have any children
	return this.childrenCount()==0;
}


/** Generate a new cell from this model, using defaults if available */
generateCell():Cell {
	
	let cellURI = "/"+this.getAdoptionName()+"(0)"; // this is will be changed on adoption
	let desc = "";									// empty description for the moment
	let value = (this.defaultValue) ? this.defaultValue : "";
	let newCell:Cell = new Cell(this.schema, 
								cellURI, 
								this.getAdoptionName(), 
								desc,
								value, 
								this.getAdoptionURI(), 
								this.isSimple);
	
	newCell.cellModel = this;						// we associate the cell model straightaway, easy peasy =)
 
	if (this.attributes) {							// now we set the attributes when we have defaults
		newCell.attributes = this.attributes.filter(a => a.defaultValue)
												.map(a => this.generateAttributeFrom(a));
	}
	
	return newCell;
}


toJSON(): CellModelJSON {

	let serialisedCellModel:CellModelJSON = Object.assign({}, this);
	if (this.attributes) {
		serialisedCellModel.attributes = this.attributes.map(a => a.toJSON());
	}
	if (this.children) {
		serialisedCellModel.children = this.children.map(c => c.toJSON());
	}
	
	return serialisedCellModel;
	
}


static fromJSON(json: CellModelJSON|string): CellModel {

	if (typeof json === 'string') {
		
		return JSON.parse(json, CellModel.reviver);
		
	} else {
	
		let cellModel = Object.create(CellModel.prototype);
		cellModel = Object.assign(cellModel, json); // add parsed attributes like schema, URI, name...
		cellModel.init();							// make sure we have all attributes ok
		
		if (json.attributes) {
			cellModel = Object.assign(cellModel, 
									  {attributes: json.attributes.map(a => CellModel.fromJSON(a))});
		}
		if (json.children) {
			cellModel = Object.assign(cellModel, 
									  {children: json.children.map(c => CellModel.fromJSON(c))});
		} else {
			cellModel = Object.assign(cellModel, {children: []});  // empty as the Tree class requires it
		}
	
		return cellModel;

	}
}


static reviver(key: string, value: any): any {
	return key === "" ? CellModel.fromJSON(value) : value;
}


private generateAttributeFrom(attribute: CellModel): Cell {
	
	let attrURI = "/"+this.getAdoptionName()+"(0)@"+attribute.getAdoptionName(); //	 be changed on adoption
	let desc = "";									// empty description for the moment
	let value = (attribute.defaultValue) ? attribute.defaultValue : ""; // sanity check, as we only generate
																		// attributes for defaults for now
	let newCell:Cell = new Cell(attribute.schema, 
								attrURI, 
								attribute.getAdoptionName(), 
								desc,
								value, 
								attribute.getAdoptionURI(), 
								attribute.isSimple);	// should always be true
	
	newCell.cellModel = attribute;						// associate the cell model straightaway, yo! =)
 
	return newCell;
	
}


////// THIS SHOULD GO IN THE COMPONENT BUT THE TREE SYSTEM DOES NOT SEEM TO ALLOW ACCESS TO COMPONENTS ////

widget: SelectableCellModelWidget;

public activateEventService(eventService: EventService, position: number) {

	this.widget = new SelectableCellModelWidget(eventService, this, position);
	if (this.children) {
		let i = 0;
		this.children.forEach( c => c.activateEventService(eventService, i++) );
	}

}


public deactivateEventService() {
	
	this.widget.ngOnDestroy();	// to remove subscription callbacks, etc.
	if (this.children) {
		this.children.forEach( c => c.deactivateEventService() );
	}
	
}

}


class SelectableCellModelWidget extends SelectableWidget {
	
active: boolean = false;
	
private activationSubscription: Subscription;

constructor(eventService: EventService, private cellModel:CellModel, private position: number) {
	super(eventService);
}


select(position:number) {
	
	if (position==this.position) {
		
		// if we were activated we deactivate ourselves and become selectable again
//		  if (this.active) {
//			  this.becomeInactive(this.cell);
//		  }
		
		console.log("[UI] SelectableCellModelWidget::select("+this.cellModel.name+"("+this.position+"))");
		this.selected = true; 
		this.unsubscribeFromSelection();
		// cleverly, we now subscribe to cellmodel activation events that may be triggered by shortcuts
		this.subscribeToActivation();
		
		// We temporarly unsubscribe from clear, send a clear event and re-subscribe
		// This means we are the only ones selected now (previous parent will be unselected, for instance)
		this.unsubscribeFromSelectionClear();
		this.events.service.publish(new CellSelectionClearEvent()); // warning: resets model state variables
		this.subscribeToSelectionClear();
		
		this.cellModel.children.forEach(c => c.widget.subscribeToSelection());
		
		// TODO: implement out of bounds handling for cell-models
//	  } else if (this.cellModel.parent && position>=this.cell.parent.childrenCount()) {
//		  console.log("[UI] SelectableCellModelWidget::select(out of bounds)");
	 } else {
		 this.clearSelection();	 // out of bounds, sorry, clear
	}

}


subscribeToSelection() {

	this.selectionSubscription = this.subscribe(this.events.service.of( CellSelectEvent )
			.subscribe( cs => this.select(cs.position) )
	);
	this.subscribeToSelectionClear();  // if we are selectable we are also clearable
}


unsubscribeFromSelection() {
	super.unsubscribeFromSelection();
	if (this.activationSubscription) {	// if we were selectable we may have been activable as well
		this.unsubscribe(this.activationSubscription);
	}
}

private subscribeToActivation() {
	
	console.log("[UI] SelectableCellModelWidget::subscribeToActivation("+this.cellModel.name+")");
	this.activationSubscription = this.subscribe(this.events.service.of( CellModelActivatedEvent )
			.filter( activated => activated.cellModel==undefined)
			.subscribe( activated => {
				this.active = true;
				console.log("[UI] SelectableCellModelWidget::subscribeToActivation(activated!)");
				// this will activate the proper component
				this.events.service.publish(new CellModelActivatedEvent(this.cellModel));

			})
	);
}


private unsubscribeFromActivation() {
	if (this.activationSubscription) {
		this.unsubscribe(this.activationSubscription);
	}
}

}
////// JUST IMAGINE THIS CODE IS IN THE CELL-MODEL.COMPONENT



export interface CellModelJSON {
	
schema: number; 
URI: string;
name: string; 
desc: string;
presentation: string;
cellPresentation: string,
thumb: string;
isSimple: boolean; 
isReference: boolean;
type_: Type_;
minOccurs: number;
maxOccurs?: number;
defaultValue?: string;

attributes?: CellModelJSON[];
children?: CellModelJSON[];
referenceURI?: string;

}


