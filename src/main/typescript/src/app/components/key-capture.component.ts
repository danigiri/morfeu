// KEY - CAPTURE . COMPONENT . TS

import { Component, AfterViewInit, OnDestroy } from "@angular/core";

//import { HotkeysService, Hotkey } from "angular2-hotkeys";
import { ShortcutInput, ShortcutEventOutput } from 'ng-keyboard-shortcuts';
//import { KeyboardShortcutsComponent } from 'ng-keyboard-shortcuts';
//import { KeyboardShortcutsService } from 'ng-keyboard-shortcuts';

import { KeyPressedEvent } from "../events/keypressed.event";
import { EventListener } from "../events/event-listener.class";
import { EventService } from "../services/event.service";

@Component({
	selector: 'key-capture',
	template: `<ng-keyboard-shortcuts [shortcuts]="shortcuts"></ng-keyboard-shortcuts>`
})

export class KeyCaptureComponent extends EventListener implements AfterViewInit, OnDestroy  {

public readonly ALL_KEYS: string[] = [	"'", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l","m",
										"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
										"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L","M",
										"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
										];
public readonly ALL_NUMBERS: string[] = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];

shortcuts: ShortcutInput[] = [];


constructor(eventService: EventService) { //},  private keyboard: KeyboardShortcutsService) {
	super(eventService);
}


ngAfterViewInit() {

	console.log("KeyCaptureComponent::ngOnInit()");
	this.registerKeyShortcuts();

}


public registerKeyShortcuts() {

	console.log("KeyCaptureComponent::registerKeyShortcuts()");

	// the prevent default is to prevent ehe edit shortcut 'e' to send an event to the first cell field 
	this.ALL_KEYS.forEach(k => this.shortcuts.push({key: k, preventDefault: true, command: e => this.keyPressed_(e)}));
	this.ALL_NUMBERS.forEach(n => this.shortcuts.push({key: n, command: e => this.numberPressed_(e)}));

}



//numberPressed_ = (event: KeyboardEvent): boolean => {
numberPressed_ = (event: ShortcutEventOutput) => {

	const key: string = Array.isArray(event.key) ? event.key[0] : event.key;
	const num = parseInt(key, 10);
	//console.log("KeyCaptureComponent::numberPressed(%s)", key);
	this.events.service.publish(new KeyPressedEvent(key, num));

	//return false;	// prevent event from bubbling

}


keyPressed_ = (event: ShortcutEventOutput) => {

	const key: string = Array.isArray(event.key) ? event.key[0] : event.key;
	//console.log("KeyCaptureComponent::keyPressed(%s) %s", key); //, event.shiftKey ? "[SHIFT]":"");
	this.events.service.publish(new KeyPressedEvent(key));

}


public unregisterKeyShortcuts() {
	this.shortcuts = [];
}


override ngOnDestroy() {

	super.ngOnDestroy();
	this.unregisterKeyShortcuts();

}


}

/*
 *	  Copyright 2024 Daniel Giribet
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
