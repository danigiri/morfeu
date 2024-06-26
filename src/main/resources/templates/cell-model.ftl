<#macro cellmodel cm><#t>
	"schema": 0
	,"URI": "${cm.URI}"
	,"name": "${cm.name}"
	,"desc": "${cm.desc}"
	,"presentation": "${cm.metadata.presentation}"
	<#if cm.metadata.thumb??><#t>
		,"thumb": "${cm.metadata.thumb}"
	</#if><#t>
	<#if cm.metadata.identifier.isPresent()><#t>
		,"identifier": "${cm.metadata.identifier.get()}"
	</#if><#t>
	<#if cm.metadata.cellPresentation??><#t>
		,"cellPresentation": "${cm.metadata.cellPresentation}"
	</#if><#t>
	,"cellPresentationType": "${cm.metadata.cellPresentationType}"
	,"cellPresentationMethod": "${cm.metadata.cellPresentationMethod}"
	,"isSimple": ${cm.isSimple()?c}
	,"type_": {
		"name": "${cm.type.name}"
		,"isSimple": ${cm.type.isSimple()?c}
		,"desc": "<#if cm.type.metadata??>${cm.type.metadata.desc}</#if>"
		<#if cm.type.regex.isPresent()><#t>
			,"regex":"${f.jsona(cm.type.regex.get())}"
		</#if><#t>
		<#if (cm.type.hasPossibleValues())!false>
		<#list cm.type.possibleValues><#t>
		,"possibleValues":[<#items as pv>"${pv}"<#sep>,</#items>]
		</#list>
		</#if>
	}
	,"minOccurs": ${cm.minOccurs}
	<#if cm.maxOccurs.isPresent()><#t>
		,"maxOccurs": ${cm.maxOccurs.getAsInt()}
	</#if><#t>
	<#if cm.isComplex()><#t>
		,"areChildrenOrdered": ${cm.areChildrenOrdered()?c}
	</#if><#t>
	<#if cm.metadata.isReadonly().orElse(false)><#t>
		,"readonly": true
	</#if><#t>
	<#if cm.metadata.valueLocator.isPresent()><#t>
		,"valueLocator": "${cm.metadata.valueLocator.get()}"
	</#if><#t>
	<#if cm.getCategory().isPresent()><#t>
		,"category": "${cm.getCategory().get()}"
	</#if><#t>
	<#if cm.isAttribute()><#t>
	, "isAttribute": true
	</#if><#t>
	<#if cm.defaultValue.isPresent()><#t>
		,"defaultValue": "${cm.defaultValue.get()}"
	</#if><#t>
	,"isReference": ${cm.isReference()?c}
	<#if cm.isReference()>
		,"referenceURI": "${cm.getReference().get().getURI()}"
	</#if><#t>
<#if cm.isComplex()><#t>
		,"attributes": [
			<#list (cm.asComplex().attributes().asList()) as a><#t>
			{<@cellmodel a/>}<#sep>,</#list>
			]
		<#if !cm.isReference()><#t>
			,"children": [
				<#list (cm.asComplex().children().asList()) as c><#t>
				{<@cellmodel c/>}<#sep>,</#list>
			]
		</#if><#t>
</#if><#t>
</#macro><#t>