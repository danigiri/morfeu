// CODE - EDITOR . COMPONENT . TS

import { Component, ElementRef, Input, OnInit, ViewChild, AfterViewInit } from '@angular/core';

import * as ace from 'ace-builds/src-min-noconflict/ace';
import 'ace-builds/src-min-noconflict/mode-sql';
import 'ace-builds/src-min-noconflict/theme-github';
import 'ace-builds/src-min-noconflict/theme-chrome';
import 'ace-builds/src-min-noconflict/theme-clouds';
//import 'ace-builds/src-min-noconflict/worker-javascript.js'
import 'ace-builds/src-noconflict/ext-language_tools';

import { EventListener } from '../../events/event-listener.class';
import { EventService } from '../../services/event.service';


@Component({
	selector: 'code-editor',
	template: `<div class="code-editor form-control" #code></div>`
})

export class CodeEditorComponent extends EventListener implements OnInit, AfterViewInit {

@Input() code: string;

@ViewChild('code') editor: ElementRef;

private codeEditor: ace.Ace.Editor;

ngOnInit() {

}


ngAfterViewInit() {

Promise.resolve(null).then(() => {
	console.debug('foo');

	// : Partial<ace.Ace.EditorOptions>
 	const editorOptions = {
			highlightActiveLine: true,
			minLines: 10,
			maxLines: Infinity,
			cursorStyle: 'wide',
			enableBasicAutocompletion: true,
			enableLiveAutocompletion: true
	};
	this.codeEditor = ace.edit(this.editor.nativeElement, editorOptions);

	this.codeEditor.setTheme('ace/theme/clouds');
	this.codeEditor.getSession().setMode('ace/mode/sql');
	this.codeEditor.setValue(this.code);
	this.codeEditor.resize();
});

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
