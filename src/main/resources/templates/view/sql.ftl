<div class="card"> 
	<div class="card-body html-preview" style="background-color: grey"> 
<#if problem??><#t>
		<div class="alert alert-danger" role="alert">${problem}</div>
<#elseif v.isUpdate><#t>

<#else><#t>
		<div>
			<table class="table table-sm">
				<#list v.results as r><#t>
				<#if r?index==0><#t>
					<thead>
						<tr>
							<#list r as c><th scope="col">${c}</ht></#list><#t>
						</tr>
					</thead>
				<#else><#t>
				<tr>
					<#list r as c><td>${c}</td>{% endfor -%}</#list><#t>
				</tr>
				</#if><#t>
				</#list><#t>
			</table>
		</div>
</#if><#t>
	</div> 
</div>