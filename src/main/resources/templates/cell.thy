{	"schema": 0
	,"URI": "[(${cell.URI})]"
	,"name": "[(${cell.Name})]"
	,"desc": "[(${#strings.trim(cell.Desc)})]"
	[# th:if="${cell.value.isPresent}"] ,"value": "[(${#strings.trim(cell.value.get)})]"[/]
	,"cellModelURI": "[(${cell.CellModel.URI})]"
	,"isSimple": [(${cell.isSimple})]
	[# th:if="${cell.isComplex}"]
	,"attributes": [[# th:each="a : ${cell.asComplex.attributes.asList}"]
				[# th:replace="templates/cell.thy (cell=${a})"][/] [# th:if="${!aStat.last}"],[/]
			[/]]
	,"internalAttributes": [[# th:each="a : ${cell.asComplex.internalAttributes.asList}"]
				[# th:replace="templates/cell.thy (cell=${a})"][/] [# th:if="${!aStat.last}"],[/]
			[/]]
	,"children": [[# th:each="c : ${cell.asComplex.children.asList}"]
				[# th:replace="templates/cell.thy (cell=${c})"][/] [# th:if="${!cStat.last}"],[/]
			[/]]
	[/]
}
