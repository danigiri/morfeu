{#- if we are empty -#}
{%- if c.isSimple -%}
	{%- if c.getValue.isPresent -%}
	{%- else -%} 
		{%- if c.cellModel.maxOccurs.isPresent -%}
			{%- if c.cellModel.maxOccurs.asInt==1 %}  {}{%- else %}  []{% endif -%}
		{%- else %}  []{% endif -%}
	{%- endif -%}
{%- elseif c.isComplex -%}
	{%- if (not (c.asComplex.attributes.hasAttributes)) and (not (c.asComplex.children.hasChildren)) -%} 
		{%- if c.cellModel.maxOccurs.isPresent -%}
			{%- if c.cellModel.maxOccurs.asInt==1 %}  {}{%- else %}  []{% endif -%}
		{%- else %}  []{% endif -%}
	{%- endif -%}
{%- endif %}