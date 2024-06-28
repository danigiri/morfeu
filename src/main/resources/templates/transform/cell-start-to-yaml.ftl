<#-- we check if we have data, attributes or children, then we check if we do not have an identifier and only then put the - and a linefeed --><#t>
<#macro cellstart2yaml cell indent><#t>
<#if cc?? &&(c.value.isPresent() || c.asComplex().attributes().hasAttributes() || c.asComplex().children().hasChildren())><#if !(c.cellModel().metadata().identifier().isPresent())>${ indent }-
</#if></#if><#t>
</#macro>