<#macro cell c>
{	"schema": 0
	,"URI": "${c.URI}"
	,"name": "${c.name}"
	,"desc": "${c.desc}"
	<#if c.value.isPresent()> ,"value": "${c.value.get()}"</#if>
	,"cellModelURI": "${c.cellModel.URI}"
	,"isSimple": ${c.isSimple()?c}
	<#if c.isComplex()>
		,"attributes": [
			<#list (c.asComplex().attributes().asList()) as a>
			<@cell a/><#sep>,</#list>
						]
		,"internalAttributes": [
			<#list (c.asComplex().internalAttributes().asList()) as a>
					<@cell a/><#sep>,</#list><#t>
								]
		,"children": [<#list (c.asComplex().children().asList()) as child>
					<@cell child/><#sep>,</#list><#t>
					 ]
	</#if>
}
</#macro>