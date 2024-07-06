{
	"name": "${v.name}"
	,"uri": "${v.URI}"
	,"desc": "${v.desc}"
	,"kind": "${v.kind}"
	,"modelURI": "${v.modelURI}"
	,"fetchableModelURI": "${v.fetchableModelURI}"
	,"contentURI": "${v.contentURI}"
	,"fetchableContentURI": "${v.fetchableContentURI}"
	,"valid": ${v.isValid()?c}
	<#if problem??><#t>
		,"problem": "${problem}"
	</#if><#t>
}
