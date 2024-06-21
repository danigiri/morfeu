<#import "cell-to-yaml.ftl" as c><#t>
---
<#list v.cells.asList()[0].asComplex().children().asList() as cell><#t>
	<@cell2yaml cell=cell case='obj-to-yaml' indent=''/><#t>
</#list><#t>