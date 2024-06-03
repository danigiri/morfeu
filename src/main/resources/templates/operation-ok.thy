{
	"result": "{{v.result}}"
	,"target": "{{v.uri}}"
	,"operation": "{{v.operation}}"
	,"operationTime": {{v.operationTime}}
	{% if defined(problem) %}
			,"problem": "{{ problem }}"
	{% endif %}	
}
