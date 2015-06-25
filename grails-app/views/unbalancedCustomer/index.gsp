
<%@ page import="py.dahsboard.processor.UnbalancedCustomer" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'unbalancedCustomer.label', default: 'UnbalancedCustomer')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
		<!-- TODO Sacara a css -->
		<style type="text/css">

			#grailsLogo {
    			background-color: #fff059;
			}

			.footer {
    			background: #fff059 none repeat scroll 0 0;
			}

			.pagination a, .pagination .currentStep {
			    color: #03b;
			}

			h1 {
			    color: #03b;
			    margin: 0.8em 1em;
			}

			th{
				color: #03b !important;
				text-align: center;
			}

			th a:link, th a:visited {
			    color: #03b !important;
			}

			td {
				color: #900;
				text-align: right;
			}

			.odd {
			    background: #f4f4f4 none repeat scroll 0 0;
			}

			.content h1 {
			    margin: 0.8em 1em 0.8em;
			}

			.pagination a:hover, .pagination a:focus, .pagination .currentStep {
			    background-color: #999999;
			    color: #ffffff;
			    outline: medium none;
			}

			tr:hover {
				background: #c4cae4;
			}

			th:hover{
				background: #c4cae4 !important;
			}
		</style>
	</head>
	<body>
		<div id="list-unbalancedCustomer" class="content scaffold-list" role="main">
			<h1><g:message code="py.dashboard.processor.default.list.label" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="cust_id" title="${message(code: 'unbalancedCustomer.cust_id.label', default: 'Custid')}" />
					
						<g:sortableColumn property="new_balance" title="${message(code: 'unbalancedCustomer.new_balance.label', default: 'Newbalance')}" />
					
						<g:sortableColumn property="old_balance" title="${message(code: 'unbalancedCustomer.old_balance.label', default: 'Oldbalance')}" />

						<g:sortableColumn property="difference" title="${message(code: 'unbalancedCustomer.difference.label', default: 'difference')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${unbalancedCustomerInstanceList}" status="i" var="unbalancedCustomerInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td>${fieldValue(bean: unbalancedCustomerInstance, field: "cust_id")}</td>
					
						<td>${fieldValue(bean: unbalancedCustomerInstance, field: "new_balance")}</td>
					
						<td>${fieldValue(bean: unbalancedCustomerInstance, field: "old_balance")}</td>

						<td>${fieldValue(bean: unbalancedCustomerInstance, field: "difference")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${unbalancedCustomerInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
