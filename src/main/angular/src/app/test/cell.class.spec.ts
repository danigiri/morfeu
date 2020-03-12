import { Cell } from '../cell.class';

describe('Cell', () => {

	const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
	//wget -O - http://localhost:3000/dyn/content/target/test-classes/test-resources/documents/readonly.xml\?model\=target/test-classes/test-resources/models/test-model.xsd | jq > foo.json
	const c = `
{
  "schema": 0,
  "URI": "target/test-classes/test-resources/documents/readonly.xml",
  "name": "",
  "desc": "DESC",
  "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd",
  "isSimple": false,
  "attributes": [],
  "internalAttributes": [],
  "children": [
    {
      "schema": 0,
      "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)",
      "name": "test",
      "desc": "",
      "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test",
      "isSimple": false,
      "attributes": [],
      "internalAttributes": [
        {
          "schema": 0,
          "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)@xmlns:xsi",
          "name": "xmlns:xsi",
          "desc": "",
          "value": "http://www.w3.org/2001/XMLSchema-instance",
          "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test",
          "isSimple": true
        },
        {
          "schema": 0,
          "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)@xsi:noNamespaceSchemaLocation",
          "name": "xsi:noNamespaceSchemaLocation",
          "desc": "",
          "value": "../models/test-model.xsd",
          "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test",
          "isSimple": true
        }
      ],
      "children": [
        {
          "schema": 0,
          "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(0)",
          "name": "row",
          "desc": "",
          "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row",
          "isSimple": false,
          "attributes": [],
          "internalAttributes": [],
          "children": [
            {
              "schema": 0,
              "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(0)/col(0)",
              "name": "col",
              "desc": "",
              "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col",
              "isSimple": false,
              "attributes": [
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(0)/col(0)@size",
                  "name": "size",
                  "desc": "",
                  "value": "12",
                  "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col@size",
                  "isSimple": true
                }
              ],
              "internalAttributes": [],
              "children": [
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(0)/col(0)/data(0)",
                  "name": "data",
                  "desc": "",
                  "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data",
                  "isSimple": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(0)/col(0)/data(0)@number",
                      "name": "number",
                      "desc": "",
                      "value": "0",
                      "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data@number",
                      "isSimple": true
                    }
                  ],
                  "internalAttributes": [],
                  "children": []
                }
              ]
            }
          ]
        },
        {
          "schema": 0,
          "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)",
          "name": "row",
          "desc": "",
          "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row",
          "isSimple": false,
          "attributes": [],
          "internalAttributes": [],
          "children": [
            {
              "schema": 0,
              "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)",
              "name": "col",
              "desc": "",
              "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col",
              "isSimple": false,
              "attributes": [
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)@size",
                  "name": "size",
                  "desc": "",
                  "value": "12",
                  "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col@size",
                  "isSimple": true
                }
              ],
              "internalAttributes": [],
              "children": [
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/data(0)",
                  "name": "data",
                  "desc": "",
                  "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data",
                  "isSimple": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/data(0)@number",
                      "name": "number",
                      "desc": "",
                      "value": "1",
                      "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data@number",
                      "isSimple": true
                    }
                  ],
                  "internalAttributes": [],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/readonly(1)",
                  "name": "readonly",
                  "desc": "",
                  "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly",
                  "isSimple": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/readonly(1)@number",
                      "name": "number",
                      "desc": "",
                      "value": "2",
                      "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly@number",
                      "isSimple": true
                    }
                  ],
                  "internalAttributes": [],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/readonly(2)",
                  "name": "readonly",
                  "desc": "",
                  "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly",
                  "isSimple": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/readonly(2)@number",
                      "name": "number",
                      "desc": "",
                      "value": "3",
                      "cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly@number",
                      "isSimple": true
                    }
                  ],
                  "internalAttributes": [],
                  "children": []
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
	`;
	let cell;
	beforeEach(() => cell = CELL.fromJSON(c));

	it('should read from json', () => {

		expect(cell).toBeDefined();
		expect(cell.desc).toBe('DESC');
		expect(cell.isSimple).toBe(false);
		expect(cell.value).toBeUndefined();

	});


	it('should find cell with correct URI', () => {

		const uri = 'target/test-classes/test-resources/documents/readonly.xml/test(0)/row(1)/col(0)/readonly(2)';
		const foundCell = cell.findCellWithURI(uri);
		expect(foundCell).toBeDefined();
		expect(foundCell.URI).toBe(uri);

		const uri2 = 'target/test-classes/test-resources/documents/readonly.xml/test(111)/row(1)/col(0)/readonly(2)';
		const foundCell2 = cell.findCellWithURI(uri2);
		expect(foundCell2).toBeUndefined();

	});


});

/*
 *    Copyright 2020 Daniel Giribet
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