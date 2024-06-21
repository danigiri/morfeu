<#-- This template takes all the attributes of a complex cell and prints them out as yaml. --><#t>
<#macro cellattributes2yaml cell attribute case indent><#t>
<#-- Taking care of: --><#t>
<#-- a) if we are key value, we print out a fixed output --><#t>
<#-- b) otherwise, we print the fields (if we have an identifier we put it first and use the compact mapping form) --><#t>
<#if cell.cellModel.metadata.identifier.isPresent()><#t>
	<#assign indentid = indent.substring( 2, indent.length()-1)><#t>
	<#assign id = cell.cellModel.metadata.identifier.get()><#t>
	<#assign a = cell.asComplex.attributes.attribute(id)><#t>
</#if><#t>
<#-- Find out if we have key value --><#t>
<#if cell.cellModel.metadata.getDirectivesFor(case).contains('KEY-VALUE') ><#t>
	${-indentid}${a.value.get}:<#list cell.asComplex.attributes.asList as x><#if (!x.name().equals(id)) && x.value.isPresent()> ${f.yamla(x.value.get())}</#if></#list>
<#else><#t>
	<#-- hack to put the identifier first --><#t>
	<#if cell.cellModel.metadata.identifier.isPresent()><#t>
${indentid}- ${a.name()}: <#if a.value().isPresent()>${f.yamla(a.value().get())}</#if>
		<#else><#t>
		<#assign id = ''><#t>
	</#if><#t>
	<#list cell.asComplex.attributes.asList() as x><#t>
	<#if !x.name.equals(id)><#t>
${-indent}${x.name()}: <#if x.value().isPresent()>${f.yamla(x.value().get())}</#if><#-- no multiline attributes allowed --><#rt>
</#if><#t>
</#list><#t>
</#if><#t>
</#macro>