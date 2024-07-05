<#import "cell-to-yaml.ftl" as c><#t>
---
<#list ((v.cells.asList()[0]).asComplex().children().asList()) as cell><#t>
	<@c.cell2yaml cell 'obj-to-yaml' '' false 0/><#t>
</#list><#t>