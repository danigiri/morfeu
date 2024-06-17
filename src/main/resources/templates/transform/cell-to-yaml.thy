{%- set meta = cell.cellModel.metadata -%}
{%- set i = indent -%}
{%- set d =  meta.getDirectivesFor(case) -%}
[# th:if="${!(meta.getDirectivesFor(case).contains('ATTRIBUTES-ONLY') || hidename==true)}"]/*[-
	-- the include will handle the empty values 
	-]*/[(${indent})][(${cell.name})]:{% set i = concat(i,'  ') %}{%include "empty-cell-to-yaml.twig" with {'c': cell} only %}
[/]
{%- if cell.isComplex -%}
	{#- ____________ complex content case ____________ -#}
	{#- 1) the attributes -#}
	{%- if cell.asComplex.attributes.size() > 0 -%}
			{%- include "cell-attributes-to-yaml.twig" with {'cell': cell, 'attribute': a, 'case': case, 'indent': i} only -%}
	{%- endif -%}
	{#- 2) the children -#}
	{%- if cell.asComplex.children.size > 0 -%}
		{%- set liststart = true -%}
		{%- set current = '' -%}
		{%- set ilist = concat(i,'  ') -%}
		{%- for c in cell.asComplex.children.asList -%}
			{%- if c.cellModel.metadata.getDirectivesFor(case).contains('LISTS-NO-PLURAL') -%}
				{%- set name = c.name -%}
			{%- else -%}
				{%- set name = concat(c.name,'s') -%}
			{%- endif -%}
			{%- if current != c.name -%}
			{{- i }}{{name}}:{%include "empty-cell-to-yaml.twig" with {'c': c} only %}
{% set current = c.name -%}
			{%- endif -%}
			{%- if c.isComplex -%}{# FIXME: we do not assume it's a list if maxOccurs is==1 #}
{%- include "cell-start-to-yaml.twig" with {'c': c, 'indent': ilist} only -%}
{%include "cell-to-yaml.twig" with {'cell': c, 'case':case, 'indent': concat(ilist,'  '), 'hidename':true} only -%}
			{%- else -%}
				{#- ____________ simple content case ____________ -#}
				{%- include "cell-simple-to-yaml.twig" with {'cell': c, 'indent': i} only -%}
{% endif -%}
		{%- endfor -%}
	{%- endif -%}
{%- else -%}
	{#- ____________ simple content case ____________ -#}
	{%- include "cell-simple-to-yaml.twig" with {'cell': cell, 'indent': i} only -%}
{%endif -%}
