export const _typesPrefix = 'target/test-classes/test-resources/documents/types.xml';
export const _readonlyPrefix = 'target/test-classes/test-resources/documents/readonly.xml';

//wget -O - http://localhost:3000/dyn/content/target/test-classes/test-resources/documents/readonly.xml\?model\=target/test-classes/test-resources/models/test-model.xsd | jq > foo.json

export const _readonlyDocument = `
{
	"name": "Readonly test doc"
	,"desc": "Readonly content"
	,"kind": "xml"
	,"modelURI": "target/test-classes/test-resources/models/test-model.xsd"
	,"contentURI": "target/test-classes/test-resources/documents/readonly.xml"
}`;

export const _readonly = `
{
	"schema": 0,
	"URI": "target/test-classes/test-resources/documents/readonly.xml",
	"name": "readonly.xml",
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


export const _types = `
{
	"schema": 0,
	"URI": "target/test-classes/test-resources/documents/types.xml",
	"name": "types.xml",
	"desc": "",
	"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd",
	"isSimple": false,
	"attributes": [],
	"internalAttributes": [],
	"children": [
	{
		"schema": 0,
		"URI": "target/test-classes/test-resources/documents/types.xml/test(0)",
		"name": "test",
		"desc": "",
		"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test",
		"isSimple": false,
		"attributes": [],
		"internalAttributes": [
		{
			"schema": 0,
			"URI": "target/test-classes/test-resources/documents/types.xml/test(0)@xmlns:xsi",
			"name": "xmlns:xsi",
			"desc": "",
			"value": "http://www.w3.org/2001/XMLSchema-instance",
			"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test",
			"isSimple": true
		},
		{
			"schema": 0,
			"URI": "target/test-classes/test-resources/documents/types.xml/test(0)@xsi:noNamespaceSchemaLocation",
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
			"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)",
			"name": "row",
			"desc": "",
			"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row",
			"isSimple": false,
			"attributes": [],
			"internalAttributes": [],
			"children": [
			{
				"schema": 0,
				"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)",
				"name": "col",
				"desc": "",
				"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col",
				"isSimple": false,
				"attributes": [
				{
					"schema": 0,
					"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)@size",
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
					"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)/types(0)",
					"name": "types",
					"desc": "",
					"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types",
					"isSimple": false,
					"attributes": [
					{
						"schema": 0,
						"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)/types(0)@bool",
						"name": "bool",
						"desc": "",
						"value": "true",
						"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types@bool",
						"isSimple": true
					}
					],
					"internalAttributes": [],
					"children": []
				},
				{
					"schema": 0,
					"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)/types(1)",
					"name": "types",
					"desc": "",
					"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types",
					"isSimple": false,
					"attributes": [
					{
						"schema": 0,
						"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)/types(1)@bool",
						"name": "bool",
						"desc": "",
						"value": "false",
						"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types@bool",
						"isSimple": true
					}
					],
					"internalAttributes": [],
					"children": []
				},
				{
					"schema": 0,
					"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)/types(2)",
					"name": "types",
					"desc": "",
					"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types",
					"isSimple": false,
					"attributes": [
					{
						"schema": 0,
						"URI": "target/test-classes/test-resources/documents/types.xml/test(0)/row(0)/col(0)/types(2)@list",
						"name": "list",
						"desc": "",
						"value": "A0",
						"cellModelURI": "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types@list",
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

export const _model = `{
	"schema" : 0,
	"URI" : "target/test-classes/test-resources/models/test-model.xsd",
	"name" : "",
	"desc" : "Description of test model",
	"presentation" : "ROOT",
	"thumb" : "",
	"cellPresentation" : "",
	"cellPresentationType" : "IMG",
	"cellPresentationMethod" : "GET",
	"isSimple" : false,
	"type_" : {
		"name" : "",
		"isSimple" : false,
		"desc" : ""
	},
	"minOccurs" : 1,
	"maxOccurs" : 1,
	"isReference" : false,
	"attributes" : [ ],
	"children" : [ {
		"schema" : 0,
		"URI" : "target/test-classes/test-resources/models/test-model.xsd/test",
		"name" : "test",
		"desc" : "Root cell-model desc",
		"presentation" : "WELL",
		"thumb" : "assets/images/test-thumb.svg",
		"cellPresentation" : "assets/images/test-cell.svg",
		"cellPresentationType" : "IMG",
		"cellPresentationMethod" : "GET",
		"isSimple" : false,
		"type_" : {
			"name" : "test-type",
			"isSimple" : false,
			"desc" : ""
		},
		"minOccurs" : 1,
		"maxOccurs" : 1,
		"isReference" : false,
		"attributes" : [ {
			"schema" : 0,
			"URI" : "target/test-classes/test-resources/models/test-model.xsd/test@text",
			"name" : "text",
			"desc" : "textField desc",
			"presentation" : "CELL",
			"thumb" : "DEFAULT",
			"cellPresentation" : "DEFAULT",
			"cellPresentationType" : "IMG",
			"cellPresentationMethod" : "GET",
			"isSimple" : true,
			"type_" : {
				"name" : "textField",
				"isSimple" : true,
				"desc" : "textField desc"
			},
			"minOccurs" : 0,
			"maxOccurs" : 1,
			"isAttribute" : true,
			"isReference" : false
		} ],
		"children" : [ {
			"schema" : 0,
			"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row",
			"name" : "row",
			"desc" : "rowCell desc",
			"presentation" : "ROW-WELL",
			"thumb" : "assets/images/row-thumb.svg",
			"cellPresentation" : "assets/images/row-cell.svg",
			"cellPresentationType" : "IMG",
			"cellPresentationMethod" : "GET",
			"isSimple" : false,
			"type_" : {
				"name" : "rowCell",
				"isSimple" : false,
				"desc" : "rowCell desc"
			},
			"minOccurs" : 0,
			"isReference" : false,
			"attributes" : [ {
				"schema" : 0,
				"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row@number",
				"name" : "number",
				"desc" : "",
				"presentation" : "CELL",
				"thumb" : "DEFAULT",
				"cellPresentation" : "DEFAULT",
				"cellPresentationType" : "IMG",
				"cellPresentationMethod" : "GET",
				"isSimple" : true,
				"type_" : {
					"name" : "numberField",
					"isSimple" : true,
					"desc" : ""
				},
				"minOccurs" : 0,
				"maxOccurs" : 1,
				"isAttribute" : true,
				"isReference" : false
			} ],
			"children" : [ {
				"schema" : 0,
				"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col",
				"name" : "col",
				"desc" : "Column, can accept content",
				"presentation" : "COL-WELL",
				"thumb" : "assets/images/col-thumb.svg",
				"cellPresentation" : "DEFAULT",
				"cellPresentationType" : "IMG",
				"cellPresentationMethod" : "GET",
				"isSimple" : false,
				"type_" : {
					"name" : "colCell",
					"isSimple" : false,
					"desc" : "Column, can accept content"
				},
				"minOccurs" : 0,
				"isReference" : false,
				"attributes" : [ {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col@size",
					"name" : "size",
					"desc" : "colField desc",
					"presentation" : "COL-FIELD",
					"thumb" : "DEFAULT",
					"cellPresentation" : "DEFAULT",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : true,
					"type_" : {
						"name" : "colField",
						"isSimple" : true,
						"desc" : "colField desc"
					},
					"minOccurs" : 1,
					"maxOccurs" : 1,
					"isAttribute" : true,
					"isReference" : false
				} ],
				"children" : [ {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/stuff",
					"name" : "stuff",
					"desc" : "Large body of text",
					"presentation" : "CELL-TEXT",
					"thumb" : "assets/images/stuff-thumb.svg",
					"cellPresentation" : "DEFAULT",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : true,
					"type_" : {
						"name" : "textContent",
						"isSimple" : true,
						"desc" : "textContent is a multiple line text area for simple content"
					},
					"minOccurs" : 0,
					"isReference" : false
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data",
					"name" : "data",
					"desc" : "Globally provided description of 'data'",
					"presentation" : "CELL",
					"thumb" : "assets/images/data-thumb.svg",
					"cellPresentation" : "assets/images/data-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "testCell",
						"isSimple" : false,
						"desc" : "testCell desc"
					},
					"minOccurs" : 0,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data@text",
						"name" : "text",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "Default value for text (from global)",
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data@number",
						"name" : "number",
						"desc" : "",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "numberField",
							"isSimple" : true,
							"desc" : ""
						},
						"minOccurs" : 1,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "11",
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data2",
					"name" : "data2",
					"desc" : "Globally provided description of 'data2'",
					"presentation" : "CELL",
					"thumb" : "assets/images/data2-thumb.svg",
					"cellPresentation" : "/dyn/preview/svg/data2.svg?__header=$_NAME&$_ATTRIBUTES",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "testCell",
						"isSimple" : false,
						"desc" : "testCell desc"
					},
					"minOccurs" : 0,
					"maxOccurs" : 2,
					"isReference" : true,
					"referenceURI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data",
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data2@text",
						"name" : "text",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data2@number",
						"name" : "number",
						"desc" : "",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "numberField",
							"isSimple" : true,
							"desc" : ""
						},
						"minOccurs" : 1,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "11",
						"isReference" : false
					} ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3",
					"name" : "data3",
					"desc" : "testIdCell desc (one field identifier)",
					"presentation" : "CELL",
					"thumb" : "assets/images/data3-thumb.svg",
					"identifier" : "text",
					"cellPresentation" : "/dyn/preview/html/$\{text}?color=$\{color}",
					"cellPresentationType" : "HTML",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "testIdCell",
						"isSimple" : false,
						"desc" : "testIdCell desc (one field identifier)"
					},
					"minOccurs" : 0,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3@text",
						"name" : "text",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 1,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "Default value",
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data3@color",
						"name" : "color",
						"desc" : "hexadecimal color field (rrggbb)",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "colorField",
							"isSimple" : true,
							"desc" : "hexadecimal color field (rrggbb)",
							"regex" : "[0-9a-fA-F]{6}"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "000011",
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data4",
					"name" : "data4",
					"desc" : "testEmptyCell desc (all fields optional)",
					"presentation" : "CELL",
					"thumb" : "assets/images/data4-thumb.svg",
					"cellPresentation" : "assets/images/data4-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "testEmptyCell",
						"isSimple" : false,
						"desc" : "testEmptyCell desc (all fields optional)"
					},
					"minOccurs" : 0,
					"maxOccurs" : 1,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data4@text",
						"name" : "text",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data4@number",
						"name" : "number",
						"desc" : "",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "numberField",
							"isSimple" : true,
							"desc" : ""
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data5",
					"name" : "data5",
					"desc" : "testPostCell desc (POST preview))",
					"presentation" : "CELL",
					"thumb" : "assets/images/data3-thumb.svg",
					"identifier" : "text",
					"cellPresentation" : "/dyn/preview/html/",
					"cellPresentationType" : "HTML",
					"cellPresentationMethod" : "POST",
					"isSimple" : false,
					"type_" : {
						"name" : "testPostCell",
						"isSimple" : false,
						"desc" : "testPostCell desc (POST preview))"
					},
					"minOccurs" : 0,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data5@text",
						"name" : "text",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 1,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "Default value",
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/data5@color",
						"name" : "color",
						"desc" : "hexadecimal color field (rrggbb)",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "colorField",
							"isSimple" : true,
							"desc" : "hexadecimal color field (rrggbb)",
							"regex" : "[0-9a-fA-F]{6}"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "000011",
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/keyvalue",
					"name" : "keyvalue",
					"desc" : "Arbitrary key:value content (for key value pairs in yaml)",
					"presentation" : "CELL",
					"thumb" : "assets/images/keyvalue-thumb.svg",
					"identifier" : "key",
					"cellPresentation" : "assets/images/keyvalue-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "keyValueCell",
						"isSimple" : false,
						"desc" : "Arbitrary key:value content (for key value pairs in yaml)"
					},
					"minOccurs" : 0,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/keyvalue@value",
						"name" : "value",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/keyvalue@key",
						"name" : "key",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 1,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell",
					"name" : "holderWell",
					"desc" : "Holder well that hides its contents",
					"presentation" : "CELL-WELL",
					"thumb" : "assets/images/holder-thumb.svg",
					"cellPresentation" : "assets/images/holder-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "holderWell",
						"isSimple" : false,
						"desc" : "Holder well that hides its contents"
					},
					"minOccurs" : 0,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell@text",
						"name" : "text",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					} ],
					"children" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell/row",
						"name" : "row",
						"desc" : "rowCell desc",
						"presentation" : "ROW-WELL",
						"thumb" : "assets/images/row-thumb.svg",
						"cellPresentation" : "assets/images/row-cell.svg",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : false,
						"type_" : {
							"name" : "rowCell",
							"isSimple" : false,
							"desc" : "rowCell desc"
						},
						"minOccurs" : 0,
						"isReference" : true,
						"referenceURI" : "target/test-classes/test-resources/models/test-model.xsd/test/row",
						"attributes" : [ {
							"schema" : 0,
							"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/holderWell/row@number",
							"name" : "number",
							"desc" : "",
							"presentation" : "CELL",
							"thumb" : "DEFAULT",
							"cellPresentation" : "DEFAULT",
							"cellPresentationType" : "IMG",
							"cellPresentationMethod" : "GET",
							"isSimple" : true,
							"type_" : {
								"name" : "numberField",
								"isSimple" : true,
								"desc" : ""
							},
							"minOccurs" : 0,
							"maxOccurs" : 1,
							"isAttribute" : true,
							"isReference" : false
						} ]
					} ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly",
					"name" : "readonly",
					"desc" : "cells following this model cannot be modified or deleted",
					"presentation" : "CELL",
					"thumb" : "assets/images/readonly-thumb.svg",
					"cellPresentation" : "assets/images/readonly-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "readonlyCell",
						"isSimple" : false,
						"desc" : "cells following this model cannot be modified or deleted"
					},
					"minOccurs" : 0,
					"readonly" : true,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/readonly@number",
						"name" : "number",
						"desc" : "",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "numberField",
							"isSimple" : true,
							"desc" : ""
						},
						"minOccurs" : 1,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "333",
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/categ",
					"name" : "categ",
					"desc" : "multiple attributes grouped by semantic criteria",
					"presentation" : "CELL",
					"thumb" : "assets/images/categ-thumb.svg",
					"cellPresentation" : "assets/images/categ-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "categoriesCell",
						"isSimple" : false,
						"desc" : "multiple attributes grouped by semantic criteria"
					},
					"minOccurs" : 0,
					"category" : "X",
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/categ@value0x",
						"name" : "value0x",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"category" : "X",
						"isAttribute" : true,
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/categ@value1y",
						"name" : "value1y",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"category" : "Y",
						"isAttribute" : true,
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/categ@value1x",
						"name" : "value1x",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"category" : "X",
						"isAttribute" : true,
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/categ@value0y",
						"name" : "value0y",
						"desc" : "textField desc",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "textField",
							"isSimple" : true,
							"desc" : "textField desc"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"category" : "Y",
						"isAttribute" : true,
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types",
					"name" : "types",
					"desc" : "cells showing all sorts of different attribute types",
					"presentation" : "CELL",
					"thumb" : "assets/images/types-thumb.svg",
					"cellPresentation" : "assets/images/types-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "typesCell",
						"isSimple" : false,
						"desc" : "cells showing all sorts of different attribute types"
					},
					"minOccurs" : 0,
					"isReference" : false,
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types@list",
						"name" : "list",
						"desc" : "List of possible values",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "listField",
							"isSimple" : true,
							"desc" : "List of possible values",
							"possibleValues" : [ "A1", "A2", "A0" ]
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					}, {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/types@bool",
						"name" : "bool",
						"desc" : "Boolean value (true|false)",
						"presentation" : "BOOLEAN",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "booleanField",
							"isSimple" : true,
							"desc" : "Boolean value (true|false)"
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"defaultValue" : "false",
						"isReference" : false
					} ],
					"children" : [ ]
				}, {
					"schema" : 0,
					"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/row",
					"name" : "row",
					"desc" : "rowCell desc",
					"presentation" : "ROW-WELL",
					"thumb" : "assets/images/row-thumb.svg",
					"cellPresentation" : "assets/images/row-cell.svg",
					"cellPresentationType" : "IMG",
					"cellPresentationMethod" : "GET",
					"isSimple" : false,
					"type_" : {
						"name" : "rowCell",
						"isSimple" : false,
						"desc" : "rowCell desc"
					},
					"minOccurs" : 0,
					"isReference" : true,
					"referenceURI" : "target/test-classes/test-resources/models/test-model.xsd/test/row",
					"attributes" : [ {
						"schema" : 0,
						"URI" : "target/test-classes/test-resources/models/test-model.xsd/test/row/col/row@number",
						"name" : "number",
						"desc" : "",
						"presentation" : "CELL",
						"thumb" : "DEFAULT",
						"cellPresentation" : "DEFAULT",
						"cellPresentationType" : "IMG",
						"cellPresentationMethod" : "GET",
						"isSimple" : true,
						"type_" : {
							"name" : "numberField",
							"isSimple" : true,
							"desc" : ""
						},
						"minOccurs" : 0,
						"maxOccurs" : 1,
						"isAttribute" : true,
						"isReference" : false
					} ]
				} ]
			} ]
		} ]
	} ],
	"valid" : true
}`;
