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

import { HotkeysService, Hotkey } from 'angular2-hotkeys';

import { Widget } from './widget.class';

import { EventService } from './events/event.service';

export class HotkeyWidget extends Widget {
    
    
private readonly keyMap: {[key: number]: string} = {39:"'", 97:"a", 98:"b", 99:"c", 100:"d", 101:"e", 102:"f"
                                                    ,103:"g", 104:"h", 105:"i", 106:"j", 107:"k", 108:"l"
                                                    ,109:"m", 110:"n", 111:"o", 112:"p", 113:"q", 114:"r"
                                                    ,115:"s", 116:"t", 117:"u", 118:"v", 119:"w", 120:"x"
                                                    ,121:"y", 122:"z"};
protected numberHotkey: Hotkey | Hotkey[];
protected commandHotkey: Hotkey | Hotkey[];


constructor(eventService: EventService, protected hotkeysService: HotkeysService) {
    super(eventService);
}


protected registerNumberHotkey(hk: Hotkey) {
    this.numberHotkey = this.hotkeysService.add(hk);
}



protected registerCommandHotkey(hk: Hotkey) {
    this.numberHotkey = this.hotkeysService.add(hk);
}


protected unregisterKeyShortcuts() {
        
        this.hotkeysService.remove(this.numberHotkey);   
        this.hotkeysService.remove(this.commandHotkey);   

}


protected translateNumberKeyboardEvent(event: KeyboardEvent): number {
    return (event.key) ? parseInt(event.key, 10) : event.charCode-48;
} 


protected translateCommandKeyboardEvent(event: KeyboardEvent) {
    return (event.key) ? event.key : this.keyMap[event.charCode]
}


}