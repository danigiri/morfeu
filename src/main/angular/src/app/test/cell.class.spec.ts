import { Cell } from '../cell.class';
import { Model } from '../model.class';

describe('Cell', () => {


	const prefix = 'target/test-classes/test-resources/documents/readonly.xml';
	let cell;

	it('should read from json', () => {

		expect(cell).toBeDefined();
		expect(cell.desc).toBe('DESC');
		expect(cell.isSimple).toBe(false);
		expect(cell.value).toBeUndefined();

	});

	it('should find cell with correct URI', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/readonly(2)';
		const foundCell = cell.findCellWithURI(uri);
		expect(foundCell).toBeDefined();
		expect(foundCell.URI).toBe(uri);

		const uri2 = prefix+'/test(111)/row(1)/col(0)/readonly(2)';
		const foundCell2 = cell.findCellWithURI(uri2);
		expect(foundCell2).toBeUndefined();

	});

	it('should generate a correct all presentation', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/data(0)';
		let data0 = cell.findCellWithURI(uri);
		expect(data0).toBeDefined();
		data0.value = 'VALUE'; 
		const presentationAllContent = data0.getPresentationAllContent();
		expect(presentationAllContent).toBe('_name=data&_value=VALUE&number=1');

	});

	it('should be able to remove', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/data(0)';
		const data0 = cell.findCellWithURI(uri);
		expect(data0).toBeDefined();
		expect(data0.canRemove()).toBe(true);

	});

	it('should not be able to remove', () => {

		const uri = prefix+'/test(0)/row(1)/col(0)/readonly(1)';
		const readonly1 = cell.findCellWithURI(uri);
		expect(readonly1).toBeDefined();
		expect(readonly1.canRemove()).toBe(false);

	});

	it('should not be able to remove as it has readonly children', () => {

		const uri = prefix+'/test(0)/row(1)';
		const row = cell.findCellWithURI(uri);
		expect(row).toBeDefined();
		expect(row.canRemove()).toBe(false);

	});


	beforeEach(() => {
	const CELL: Cell = Object.create(Cell.prototype); // to simulate static call
	const MODEL: Model = Object.create(Model.prototype); // to simulate static call
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

	const m = `{
  "schema": 0,
  "URI": "target/test-classes/test-resources/models/test-model.xsd",
  "name": "",
  "desc": "Description of test model",
  "presentation": "ROOT",
  "thumb": "",
  "cellPresentation": "",
  "cellPresentationType": "IMG",
  "cellPresentationMethod": "GET",
  "isSimple": false,
  "type_": {
    "name": "",
    "isSimple": false,
    "desc": ""
  },
  "minOccurs": 1,
  "maxOccurs": 1,
  "isReference": false,
  "attributes": [],
  "children": [
    {
      "schema": 0,
      "URI": "target/test-classes/test-resources/models/test-model.xsd/test",
      "name": "test",
      "desc": "Root cell-model desc",
      "presentation": "WELL",
      "thumb": "assets/images/test-thumb.svg",
      "cellPresentation": "assets/images/test-cell.svg",
      "cellPresentationType": "IMG",
      "cellPresentationMethod": "GET",
      "isSimple": false,
      "type_": {
        "name": "test-type",
        "isSimple": false,
        "desc": ""
      },
      "minOccurs": 1,
      "maxOccurs": 1,
      "isReference": false,
      "attributes": [
        {
          "schema": 0,
          "URI": "target/test-classes/test-resources/models/test-model.xsd/test@text",
          "name": "text",
          "desc": "textField desc",
          "presentation": "CELL",
          "thumb": "DEFAULT",
          "cellPresentation": "DEFAULT",
          "cellPresentationType": "IMG",
          "cellPresentationMethod": "GET",
          "isSimple": true,
          "type_": {
            "name": "textField",
            "isSimple": true,
            "desc": "textField desc"
          },
          "minOccurs": 0,
          "maxOccurs": 1,
          "isAttribute": true,
          "isReference": false
        }
      ],
      "children": [
        {
          "schema": 0,
          "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row",
          "name": "row",
          "desc": "rowCell desc",
          "presentation": "ROW-WELL",
          "thumb": "assets/images/row-thumb.svg",
          "cellPresentation": "assets/images/row-cell.svg",
          "cellPresentationType": "IMG",
          "cellPresentationMethod": "GET",
          "isSimple": false,
          "type_": {
            "name": "rowCell",
            "isSimple": false,
            "desc": "rowCell desc"
          },
          "minOccurs": 0,
          "isReference": false,
          "attributes": [
            {
              "schema": 0,
              "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row@number",
              "name": "number",
              "desc": "",
              "presentation": "CELL",
              "thumb": "DEFAULT",
              "cellPresentation": "DEFAULT",
              "cellPresentationType": "IMG",
              "cellPresentationMethod": "GET",
              "isSimple": true,
              "type_": {
                "name": "numberField",
                "isSimple": true,
                "desc": ""
              },
              "minOccurs": 0,
              "maxOccurs": 1,
              "isAttribute": true,
              "isReference": false
            }
          ],
          "children": [
            {
              "schema": 0,
              "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col",
              "name": "col",
              "desc": "Column, can accept content",
              "presentation": "COL-WELL",
              "thumb": "assets/images/col-thumb.svg",
              "cellPresentation": "DEFAULT",
              "cellPresentationType": "IMG",
              "cellPresentationMethod": "GET",
              "isSimple": false,
              "type_": {
                "name": "colCell",
                "isSimple": false,
                "desc": "Column, can accept content"
              },
              "minOccurs": 0,
              "isReference": false,
              "attributes": [
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col@size",
                  "name": "size",
                  "desc": "colField desc",
                  "presentation": "COL-FIELD",
                  "thumb": "DEFAULT",
                  "cellPresentation": "DEFAULT",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": true,
                  "type_": {
                    "name": "colField",
                    "isSimple": true,
                    "desc": "colField desc"
                  },
                  "minOccurs": 1,
                  "maxOccurs": 1,
                  "isAttribute": true,
                  "isReference": false
                }
              ],
              "children": [
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/stuff",
                  "name": "stuff",
                  "desc": "Large body of text",
                  "presentation": "CELL-TEXT",
                  "thumb": "assets/images/stuff-thumb.svg",
                  "cellPresentation": "assets/images/stuff-cell.svg",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": true,
                  "type_": {
                    "name": "textContent",
                    "isSimple": true,
                    "desc": "textContent is a multiple line text area for simple content"
                  },
                  "minOccurs": 0,
                  "isReference": false
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data",
                  "name": "data",
                  "desc": "Globally provided description of 'data'",
                  "presentation": "CELL",
                  "thumb": "assets/images/data-thumb.svg",
                  "cellPresentation": "assets/images/data-cell.svg",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "testCell",
                    "isSimple": false,
                    "desc": "testCell desc"
                  },
                  "minOccurs": 0,
                  "isReference": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data@text",
                      "name": "text",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "Default value for text (from global)",
                      "isReference": false
                    },
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data@number",
                      "name": "number",
                      "desc": "",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "numberField",
                        "isSimple": true,
                        "desc": ""
                      },
                      "minOccurs": 1,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "11",
                      "isReference": false
                    }
                  ],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data2",
                  "name": "data2",
                  "desc": "Globally provided description of 'data2'",
                  "presentation": "CELL",
                  "thumb": "assets/images/data2-thumb.svg",
                  "cellPresentation": "/dyn/preview/svg/data2.svg?__header=$_NAME&$_ATTRIBUTES",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "testCell",
                    "isSimple": false,
                    "desc": "testCell desc"
                  },
                  "minOccurs": 0,
                  "maxOccurs": 2,
                  "isReference": true,
                  "referenceURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data",
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data2@text",
                      "name": "text",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "isReference": false
                    },
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data2@number",
                      "name": "number",
                      "desc": "",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "numberField",
                        "isSimple": true,
                        "desc": ""
                      },
                      "minOccurs": 1,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "11",
                      "isReference": false
                    }
                  ]
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3",
                  "name": "data3",
                  "desc": "testIdCell desc (one field identifier)",
                  "presentation": "CELL",
                  "thumb": "assets/images/data3-thumb.svg",
                  "identifier": "text",
                  "cellPresentation": "/dyn/preview/html/\${text}?color=\${color}",
                  "cellPresentationType": "HTML",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "testIdCell",
                    "isSimple": false,
                    "desc": "testIdCell desc (one field identifier)"
                  },
                  "minOccurs": 0,
                  "isReference": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3@text",
                      "name": "text",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 1,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "Default value",
                      "isReference": false
                    },
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3@color",
                      "name": "color",
                      "desc": "hexadecimal color field (rrggbb)",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "colorField",
                        "isSimple": true,
                        "desc": "hexadecimal color field (rrggbb)"
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "000011",
                      "isReference": false
                    }
                  ],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data4",
                  "name": "data4",
                  "desc": "testEmptyCell desc (all fields optional)",
                  "presentation": "CELL",
                  "thumb": "assets/images/data4-thumb.svg",
                  "cellPresentation": "assets/images/data4-cell.svg",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "testEmptyCell",
                    "isSimple": false,
                    "desc": "testEmptyCell desc (all fields optional)"
                  },
                  "minOccurs": 0,
                  "maxOccurs": 1,
                  "isReference": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data4@text",
                      "name": "text",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "isReference": false
                    },
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data4@number",
                      "name": "number",
                      "desc": "",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "numberField",
                        "isSimple": true,
                        "desc": ""
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "isReference": false
                    }
                  ],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data5",
                  "name": "data5",
                  "desc": "testPostCell desc (POST preview))",
                  "presentation": "CELL",
                  "thumb": "assets/images/data3-thumb.svg",
                  "identifier": "text",
                  "cellPresentation": "/dyn/preview/html/",
                  "cellPresentationType": "HTML",
                  "cellPresentationMethod": "POST",
                  "isSimple": false,
                  "type_": {
                    "name": "testPostCell",
                    "isSimple": false,
                    "desc": "testPostCell desc (POST preview))"
                  },
                  "minOccurs": 0,
                  "isReference": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data5@text",
                      "name": "text",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 1,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "Default value",
                      "isReference": false
                    },
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data5@color",
                      "name": "color",
                      "desc": "hexadecimal color field (rrggbb)",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "colorField",
                        "isSimple": true,
                        "desc": "hexadecimal color field (rrggbb)"
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "000011",
                      "isReference": false
                    }
                  ],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/keyvalue",
                  "name": "keyvalue",
                  "desc": "Arbitrary key:value content (for key value pairs in yaml)",
                  "presentation": "CELL",
                  "thumb": "assets/images/keyvalue-thumb.svg",
                  "identifier": "key",
                  "cellPresentation": "assets/images/keyvalue-cell.svg",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "keyValueCell",
                    "isSimple": false,
                    "desc": "Arbitrary key:value content (for key value pairs in yaml)"
                  },
                  "minOccurs": 0,
                  "isReference": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/keyvalue@value",
                      "name": "value",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "isReference": false
                    },
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/keyvalue@key",
                      "name": "key",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 1,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "isReference": false
                    }
                  ],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell",
                  "name": "holderWell",
                  "desc": "Holder well that hides its contents",
                  "presentation": "CELL-WELL",
                  "thumb": "assets/images/holder-thumb.svg",
                  "cellPresentation": "assets/images/holder-cell.svg",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "holderWell",
                    "isSimple": false,
                    "desc": "Holder well that hides its contents"
                  },
                  "minOccurs": 0,
                  "isReference": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell@text",
                      "name": "text",
                      "desc": "textField desc",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "textField",
                        "isSimple": true,
                        "desc": "textField desc"
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "isReference": false
                    }
                  ],
                  "children": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell/row",
                      "name": "row",
                      "desc": "rowCell desc",
                      "presentation": "ROW-WELL",
                      "thumb": "assets/images/row-thumb.svg",
                      "cellPresentation": "assets/images/row-cell.svg",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": false,
                      "type_": {
                        "name": "rowCell",
                        "isSimple": false,
                        "desc": "rowCell desc"
                      },
                      "minOccurs": 0,
                      "isReference": true,
                      "referenceURI": "target/test-classes/test-resources/models/test-model.xsd/test/row",
                      "attributes": [
                        {
                          "schema": 0,
                          "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell/row@number",
                          "name": "number",
                          "desc": "",
                          "presentation": "CELL",
                          "thumb": "DEFAULT",
                          "cellPresentation": "DEFAULT",
                          "cellPresentationType": "IMG",
                          "cellPresentationMethod": "GET",
                          "isSimple": true,
                          "type_": {
                            "name": "numberField",
                            "isSimple": true,
                            "desc": ""
                          },
                          "minOccurs": 0,
                          "maxOccurs": 1,
                          "isAttribute": true,
                          "isReference": false
                        }
                      ]
                    }
                  ]
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly",
                  "name": "readonly",
                  "desc": "readonly desc",
                  "presentation": "CELL",
                  "thumb": "assets/images/readonly-thumb.svg",
                  "cellPresentation": "assets/images/readonly-cell.svg",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "readonlyCell",
                    "isSimple": false,
                    "desc": "readonly desc"
                  },
                  "minOccurs": 0,
                  "readonly": true,
                  "isReference": false,
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly@number",
                      "name": "number",
                      "desc": "",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "numberField",
                        "isSimple": true,
                        "desc": ""
                      },
                      "minOccurs": 1,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "defaultValue": "333",
                      "isReference": false
                    }
                  ],
                  "children": []
                },
                {
                  "schema": 0,
                  "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/row",
                  "name": "row",
                  "desc": "rowCell desc",
                  "presentation": "ROW-WELL",
                  "thumb": "assets/images/row-thumb.svg",
                  "cellPresentation": "assets/images/row-cell.svg",
                  "cellPresentationType": "IMG",
                  "cellPresentationMethod": "GET",
                  "isSimple": false,
                  "type_": {
                    "name": "rowCell",
                    "isSimple": false,
                    "desc": "rowCell desc"
                  },
                  "minOccurs": 0,
                  "isReference": true,
                  "referenceURI": "target/test-classes/test-resources/models/test-model.xsd/test/row",
                  "attributes": [
                    {
                      "schema": 0,
                      "URI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/row@number",
                      "name": "number",
                      "desc": "",
                      "presentation": "CELL",
                      "thumb": "DEFAULT",
                      "cellPresentation": "DEFAULT",
                      "cellPresentationType": "IMG",
                      "cellPresentationMethod": "GET",
                      "isSimple": true,
                      "type_": {
                        "name": "numberField",
                        "isSimple": true,
                        "desc": ""
                      },
                      "minOccurs": 0,
                      "maxOccurs": 1,
                      "isAttribute": true,
                      "isReference": false
                    }
                  ]
                }
              ]
            }
          ]
        }
      ]
    }
  ],
  "valid": true
}
	`;

		cell = CELL.fromJSON(c);
		const model = MODEL.fromJSON(m);
		cell.associateWith(model, cell.cellModelURI);

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