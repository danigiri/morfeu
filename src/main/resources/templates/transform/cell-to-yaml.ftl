<#import "empty-cell-to-yaml.ftl" as e><#t>
<#import "cell-attributes-to-yaml.ftl" as ca><#t>
<#import "cell-start-to-yaml.ftl" as s><#t>
<#import "cell-simple-to-yaml.ftl" as si><#t>
<#macro cell2yaml cell case indent hidename level><#t>
<#local meta = cell.cellModel.metadata><#t>
<#local i = indent>
<#local d = meta.getDirectivesFor(case)><#t>
<#if (meta.directivesForCaseContain(case, 'ATTRIBUTES-ONLY') || hidename==true)><#t>
<#else><#t>
	<#-- the include will handle the empty values --><#t>
${indent}${cell.name}:<#local i = "${i}  "><@e.emptycell2yaml c=cell/>
</#if><#t>
<#if cell.isComplex()>
	<#-- ____________ complex content case ____________ --><#t>
	<#-- 1) the attributes --><#t>
	<#if cell.asComplex().attributes().size() gt 0><#t>
<@ca.cellattributes2yaml cell case i/><#--  FIXME: a \n makes no sense -->
	</#if><#t>
	<#-- 2) the children --><#t>
	<#if (cell.asComplex().children().size()) gt 0><#t>
		<#local liststart = true><#t>
		<#local current = ''><#t>
		<#local ilist = "${i}  "><#t>
		<#list (cell.asComplex().children().asList()) as c><#t>
			<#if ((c.cellModel.metadata).directivesForCaseContain(case,'LISTS-NO-PLURAL'))><#t>
				<#local name = c.name><#t>
			<#else><#t>
				<#local name ="${c.name}s"><#t>
			</#if><#t>
			<#if current !=c.name><#t>
${i}${name}:<@e.emptycell2yaml c/>
				<#local current = c.name><#t>
			</#if><#t>
			<#if c.isComplex()><#-- FIXME: we do not assume it's a list if maxOccurs is==1 --><#t>
<@s.cellstart2yaml c ilist level/><#t>
<@cell2yaml c case "${ilist}  " true level+1/><#rt>
			<#else><#t>
				<#-- ____________ simple content case ____________ --><#t>
<@si.cellsimple2yaml c i/><#t>
			</#if><#t>
		</#list><#t>
	</#if><#t>
<#else><#t>
	<#-- ____________ simple content case ____________ --><#t>
<@si.cellsimple2yaml c i/><#t>
</#if><#t>
</#macro>