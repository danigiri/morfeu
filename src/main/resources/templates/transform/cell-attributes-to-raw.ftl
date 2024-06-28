<#macro cellattributes2raw cell case indent>
<#if cell.cellModel.metadata.identifier.isPresent()><#t>
	<#assign id = cell.cellModel().metadata().identifier.get()><#t>
	<#assign a = cell.asComplex().attributes.attribute(id)><#t>
<#else><#t>
	<#assign id = ''><#t>
</#if>
<#-- Find out if we have key value --><#t>
<#-- FIXME: for some reason, we are passing a cell that has null metadata --><#t>
<#if cell?? && cell.metadata?? && ((cell.cellModel.getMetadata()).getDirectivesForCaseContain(case,'KEY-VALUE'))><#t>
 ${a.name}=${a.value.get()}<#list (cell.asComplex.attributes.asList()) as x><#if x.name!=id && x.value.isPresent()> ${x.value.get()}</#if></#list>
<#else><#t>
	<#-- hack to put the identifier first --><#t>
	<#list (cell.asComplex().attributes().asList()) as x><#t>
		<#if x.name!=id><#if x.value.isPresent()> ${x.value.get()}</#if></#if><#t>
	</#list><#lt>
</#if><#t>
</#macro>