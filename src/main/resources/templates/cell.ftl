<#macro cell cell>
{	"schema": 0
	,"URI": "${cell.URI}"
	,"name": "${cell.Name}"
	,"desc": "${f.trim(cell.Desc)}"
	<#if cell.value.isPresent()> ,"value": "${f.trim(cell.value.get())}"</#if>
	,"cellModelURI": "${cell.CellModel.URI}"
	,"isSimple": ${cell.isSimple}
	<#if cell.isComplex()>
		,"attributes": [
			<#list cell.asComplex.attributes.asList() as a>
			<@cell cell=a><#sep>,</#list>
						]
		,"internalAttributes": [
			<#list cell.asComplex.internalAttributes.asList() as a>
					<@cell cell=a><#sep>,</#list><#t>
								]
		,"children": [<#list cell.asComplex.children.asList() as c>
					<@cell cell=c><#sep>,</#list><#t>
					 ]
	</#if>
}
</#macro>