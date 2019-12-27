// KEY - CAPTURE . COMPONENT . TS

import { Component, OnInit, OnDestroy } from "@angular/core";

import { HotkeysService, Hotkey } from "angular2-hotkeys";

import { KeyPressedEvent } from "../events/keypressed.event";
import { EventListener } from "../events/event-listener.class";
import { EventService } from "../services/event.service";

@Component({
	moduleId: module.id,
	selector: 'key-capture',
	template: `<div></div>`
})

export class KeyCaptureComponent extends EventListener implements OnInit, OnDestroy  {

public readonly ALL_KEYS: string[] = [	"'", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l","m",
										"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
										"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L","M",
										"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
										];

//private readonly keyMap: {[key: number]: string} = {39:"'", 97:"a", 98:"b", 99:"c", 100:"d", 101:"e", 102:"f"
//													,103:"g", 104:"h", 105:"i", 106:"j", 107:"k", 108:"l"
//													,109:"m", 110:"n", 111:"o", 112:"p", 113:"q", 114:"r"
//													,115:"s", 116:"t", 117:"u", 118:"v", 119:"w", 120:"x"
//													,121:"y", 122:"z"
//												};

//@Input() commands?: string[];

private numberHotkey: Hotkey | Hotkey[];
private commandHotkey: Hotkey | Hotkey[];


constructor(eventService: EventService, public hotkeysService: HotkeysService) {
	super(eventService);
}


ngOnInit() {

	console.log("KeyCaptureComponent::ngOnInit()");
	this.registerKeyShortcuts();

}


public registerKeyShortcuts() {

	console.log("KeyCaptureComponent::registerKeyShortcuts()");

	const numbers: string[] = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
	this.numberHotkey = this.hotkeysService.add(new Hotkey(numbers, this.numberPressed_));

	this.commandHotkey = this.hotkeysService.add(new Hotkey(this.ALL_KEYS, this.keyPressed_)); 

}



numberPressed_ = (event: KeyboardEvent): boolean => {

	let num = (event.key) ? parseInt(event.key, 10) : event.charCode-48;
	//console.log("KeyCaptureComponent::numberPressed(%s)", event.key);
	this.events.service.publish(new KeyPressedEvent(String(num), num));

	return false;	// prevent event from bubbling

}


keyPressed_ = (event: KeyboardEvent): boolean => {

	let key = event.key

	console.log("KeyCaptureComponent::keyPressed(%s) %s", key, event.shiftKey ? "[SHIFT]":"");
	
	if (event.shiftKey) {
		this.events.service.publish(new KeyPressedEvent(key.toUpperCase()));
	} else {
		this.events.service.publish(new KeyPressedEvent(key));
	}

	return false;	// prevent event from bubbling

}


public unregisterKeyShortcuts() {

	this.hotkeysService.remove(this.numberHotkey);
	this.hotkeysService.remove(this.commandHotkey);

}


ngOnDestroy() {

	super.ngOnDestroy();
	this.unregisterKeyShortcuts();

}


}

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
