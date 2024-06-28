<#-- if we are empty --><#t>
<#macro emptycell2yaml c><#t>
<#if c.isSimple()><#t>
	<#if c.value.isPresent()><#t>
	<#else><#t> 
		<#if c.cellModel.maxOccurs.isPresent()><#t>
<#if c.cellModel.maxOccurs.asInt==1>  {}<#else>  []</#if><#rt>
<#else>  []</#if><#rt>
	</#if><#t>
<#elseif c.isComplex()><#t>
	<#if !(c.asComplex().attributes().hasAttributes()) && !(c.asComplex().children().hasChildren())>
		<#if c.cellModel.maxOccurs.isPresent()><#t>
<#if c.cellModel.maxOccurs.asInt==1>  {}<#else>  []</#if><#rt>
<#else>  []</#if><#rt>
	</#if><#t>
</#if><#t>
</#macro>