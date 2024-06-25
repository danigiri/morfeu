<#import "cell-model.ftl" as xx><#t>
{
	<@xx.cellmodel v/>
	,"valid": <#if problem??>false<#else>true</#if>
	<#if problem??>
		,"problem": "${problem}"
	</#if>
}
