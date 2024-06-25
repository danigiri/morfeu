{
	"name": "[(${v.Name})]"
	,"uri": "[(${v.URI})]"
	,"desc": "[(${v.Desc})]"
	,"kind": "[(${v.Kind})]"
	,"modelURI": "[(${v.ModelURI})]"
	,"fetchableModelURI": "[(${v.FetchableModelURI})]"
	,"contentURI": "[(${v.ContentURI})]"
	,"fetchableContentURI": "[(${v.FetchableContentURI})]"
	,"valid": [(${v.isValid})]
	[# th:if="${problem!=null}"]
		,"problem": "[(${problem})]"
	[/]
}
