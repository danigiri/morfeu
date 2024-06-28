<#import "cell-to-raw.ftl" as raw><#t>
<#list (v.cells.asList()) as c><#t>
<@raw.cell2raw cell=c case='obj-to-raw' indent=''/><#t>
</#list><#t>