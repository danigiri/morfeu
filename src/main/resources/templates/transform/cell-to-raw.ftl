<#import "cell-attributes-to-raw.ftl" as ca><#t>
<#macro cell2raw cell case indent><#t>
<#local meta = cell.cellModel.metadata><#t>
<#local i = indent><#t>
<#if cell.isComplex()><#t>
	<#-- ____________ complex content case ____________ --><#t>
	<#-- 1) the attributes (check key value and also identifiers first) --><#t>
	<#if cell.asComplex().attributes().size() gt 0><#t>
${i}<#if cell.value.isPresent()>${cell.value.get()}</#if><@ca.cellattributes2raw cell case i/>
	</#if><#t>
	<#-- 2) the children --><#t>
		<#list (cell.asComplex().children().asList()) as c>
<@cell2raw c case "${i}  "/>
		</#list><#t>
<#else><#t>
	<#-- ____________ simple content case ____________ --><#t>
	<#if cell.value.isPresent()>${i}${cell.value.get()}</#if>
</#if><#t>
</#macro><#t>