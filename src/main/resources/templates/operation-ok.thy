{
	"result": "[(${v.result})]"
	,"target": "[(${v.uri})]"
	,"operation": "[(${v.operation})]"
	,"operationTime": [(${v.operationTime})]
	[# th:if="${problem!=null}"]
			,"problem": "[(${problem })]"
	[/]
}
