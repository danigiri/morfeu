<#import "empty-cell-to-yaml.ftl" as e><#t>
<#import "cell-attributes-to-yaml.ftl" as ca><#t>
<#import "cell-start-to-yaml.ftl" as s><#t>
<#import "cell-simple-to-yaml.ftl" as si><#t>
<#macro cell2yaml cell, case, indent hidename><#t>
<#assign meta = cell.cellModel.metadata><#t>
<#assign i = indent><#t>
<#assign d = meta.getDirectivesFor(case)><#t>
<#if !(meta.directivesForCaseContain(case, 'ATTRIBUTES-ONLY') || hidename==true)><#t>
	<#-- the include will handle the empty values --><#t>
	${indent}${cell.name}:<#assign i = "${i}  "><@e.emptycell2yaml c=cell/>
</#if><#t>
<#if cell.isComplex()>
	<#-- ____________ complex content case ____________ --><#t>
	<#-- 1) the attributes --><#t>
	<#if cell.asComplex().attributes().size() gt 0><#t>
			<@ca.cellattributes2yaml cell case i/><#--  FIXME: this \n makes no sense -->

</#if><#t>
	<#-- 2) the children --><#t>
	<#if (cell.asComplex().children().size()) gt 0><#t>
		<#assign liststart = true><#t>
		<#assign current = ''><#t>
		<#assign ilist = "${i}  "><#t>
		<#list (cell.asComplex().children().asList()) as c><#t>
			<#if ((c.cellModel.metadata).directivesForCaseContain(case,'LISTS-NO-PLURAL'))><#t>
				<#assign name = c.name><#t>
			<#else><#t>
				<#assign name ="${c.name}s"><#t>
			</#if><#t>
			<#if current !=c.name><#t>
${i}${name}:<@e.emptycell2yaml c=c/>
				<#assign current = c.name><#t>
			</#if><#t>
			<#if c.isComplex()><#-- FIXME: we do not assume it's a list if maxOccurs is==1 --><#t>
				<@s.cellstart2yaml c ilist/><#t>
<@cell2yaml c case "${ilist}  " true/>
			<#else><#t>
				<#-- ____________ simple content case ____________ --><#t>
				<@si.cellsimple2yaml cell=c indent=i/><#t>
</#if><#t>
		</#list><#t>
	</#if><#t>
<#else><#t>
	<#-- ____________ simple content case ____________ --><#t>
	<@si.cellsimple2yaml cell=c indent=i/><#t>
</#if><#t>
</#macro>