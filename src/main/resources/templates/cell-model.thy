	"schema": 0
	,"URI": "[(${cm.URI})]"
	,"name": "[(${cm.Name})]"
	,"desc": "[(${#strings.trim(cm.Desc)})]"
	,"presentation": "[(${cm.metadata.Presentation})]"
	[# th:if="${!cm.metadata.thumb}"]
		,"thumb": "[(${cm.metadata.thumb})]"
	[/]
	[# th:if="${cm.metadata.identifier.isPresent}"]
		,"identifier": "[(${cm.metadata.identifier.get})]"
	[/]
	[# th:if="${!cm.metadata.cellPresentation}"]
		,"cellPresentation": "[(${cm.metadata.cellPresentation})]"
	[/]
	,"cellPresentationType": "[(${cm.metadata.cellPresentationType})]"
	,"cellPresentationMethod": "[(${cm.metadata.cellPresentationMethod})]"
	,"isSimple": [(${cm.isSimple})]
	,"type_": {
		"name": "[(${cm.type.name})]"
		,"isSimple": [(${cm.type.isSimple})]
		,"desc": "[# th:if="${cm.type.metadata!=null}"][(${#strings.trim(cm.type.metadata.desc)})][/]"
		[# th:if="${cm.type.regex.isPresent}"]
			,"regex":"[(${#str.jsona(cm.type.regex.get)})]"
		[/]
		[# th:if="${cm.type.possibleValues!=null && cm.type.possibleValues.size > 0}"]
		,"possibleValues":[[# th:each="pv : ${cm.type.possibleValues}"]"[(${pv})]"[# th:if="${!pvStat.last}"],[/][/]]
		[/]
	}
	,"minOccurs": [(${cm.minOccurs})]
	[# th:if="${cm.maxOccurs.isPresent}"]
		,"maxOccurs": [(${cm.maxOccurs.getAsInt})]
	[/]
	[# th:if="${cm.isComplex}"]
		,"areChildrenOrdered": [(${cm.areChildrenOrdered})]
	[/]
	[# th:if="${cm.metadata.isReadonly().orElse(false)}"]
		,"readonly": true
	[/]
	[# th:if="${cm.metadata.valueLocator.isPresent}"]
		,"valueLocator": "[(${cm.metadata.valueLocator.get})]"
	[/]
	[# th:if="${cm.getCategory().orElse(false)}"]
		,"category": "[(${cm.getCategory().get})]"
	[/]
	[# th:if="${cm.isAttribute}"]
	, "isAttribute": true
	[/]
	[# th:if="${cm.defaultValue.isPresent}"]
		,"defaultValue": "[(${cm.defaultValue.get})]"
	[/]
	,"isReference": [(${cm.isReference})]
[# th:if="${cm.isReference}"]
		,"referenceURI": "[(${cm.reference.get.getURI})]"
[/]
[# th:if="${cm.isComplex}"]
		,"attributes": [[# th:each="a : ${cm.asComplex.attributes.asList}"]
			{[# th:replace="templates/cell-model.thy (cm=${a})"][/]}[# th:if="${!aStat.last}"],[/]
		[/]]
		[# th:if="${!cm.isReference}"]
			,"children": [[# th:each="c : ${cm.asComplex.children.asList}"]
				{[# th:replace="templates/cell-model.thy (cm=${c})"][/]}[# th:if="${!cStat.last}"],[/]
			[/]]
		[/]
[/]
