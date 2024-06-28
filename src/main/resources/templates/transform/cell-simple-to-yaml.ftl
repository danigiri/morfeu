<#macro cellsimple2yaml cell indent><#t>
<#if  cell.value.isPresent()><#t>
	<#if  cell.cellModel().metadata().presentation()=='CELL-TEXT' && f.isMultiline(cell.value().get())><#t>
		<#assign textindent = "$(indent}  "><#t>
${indent}- |
${textindent}${f.yamlc(f.multiline(textindent, cell.value.get()))}
	<#else><#t>
${indent}- ${ f.yamlc(cell.value.get())}
	</#if><#t>
</#if><#t>
</#macro>