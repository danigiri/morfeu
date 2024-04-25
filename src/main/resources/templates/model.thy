{
	[# th:replace="templates/cell-model.thy (cm=${v})"][/]
	,"valid": [# th:if="${problem!=null}"] [(${"false"})] [/][# th:if="${problem==null}"] [(${"true"})] [/]
	[# th:if="${problem!=null}"]
		,"problem": "[(${problem})]"
	[/]
}
