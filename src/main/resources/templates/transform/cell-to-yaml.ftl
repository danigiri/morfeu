<#import "empty-cell-to-yaml.ftl" as e><#t>
<#import "cell-attributes-to-yaml.ftl" as c><#t>
<#import "cell-start-to-yaml.ftl" as s><#t>
<#import "cell-simple-to-yaml.ftl" as si><#t>
<#macro cell2yaml cell, case, indent hidename><#t>
<#assign meta = cell.cellModel().metadata()><#t>
<#assign i = indent><#t>
<#assign d = meta.getDirectivesFor(case)><#t>
<#if !(meta.getDirectivesFor(case).contains('ATTRIBUTES-ONLY') || hidename==true)><#t>
	<#-- the include will handle the empty values --><#t>
	${indent}${cell.name}:<#assign i = "${i}  "><@e.emptycell2yaml c=cell/>
</#if><#t>
<#if cell.isComplex()>
	<#-- ____________ complex content case ____________ --><#t>
	<#-- 1) the attributes --><#t>
	<#if cell.asComplex.attributes.size() gt 0><#t>
			<@c.cellattributes2yaml cell=cell attribute=a case=case indent=i/><#t>
	</#if><#t>
	<#-- 2) the children --><#t>
	<#if cell.asComplex.children.size gt 0><#t>
		<#assign liststart = true><#t>
		<#assign current = ''><#t>
		<#assign ilist = "${i}  "><#t>
		<#list  cell.asComplex.children.asList as c><#t>
			<#if c.cellModel.metadata.getDirectivesFor(case).contains('LISTS-NO-PLURAL')><#t>
				<#assign name = c.name()><#t>
			<#else><#t>
				<#assign name ="${c.name}s"><#t>
			</#if><#t>
			<#if !current.equals(c.name())><#t>
${i}${name}:<@e.emptycell2yaml c=c/><#rt>
				<#assign current = c.name()><#t>
			</#if><#t>
			<#if c.isComplex()><#-- FIXME: we do not assume it's a list if maxOccurs is==1 --><#t>
				<@s.cellstart2yaml cell=c indent=ilist/><#t>
<@cell2yamlcell cell=c case=case indent="${ilist}  " hidename=true/>
			<#else><#t>
				<#-- ____________ simple content case ____________ -->><#t>
				<@si.cellsimple2yaml cell=c indent=i/><#t>
</#if><#t>
		</#list><#t>
	</#if><#t>
<#else><#t>
	<#-- ____________ simple content case ____________ --><#t>
	<@si.cellsimple2yaml cell=c indent=i/><#t>
</#if><#t>
</#macro>