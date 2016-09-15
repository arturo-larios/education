<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<t:wrapper>
	<div class="col-md-12">
		<div class="wrapper text-center">
			<div class="btn-group text-center">
				<a href="/payment/make-payment" class="btn btn-sm btn-primary">Make
					Payment</a> <a href="/get-public-users" class="btn btn-sm btn-primary">Find
					Friends</a> <a href="/event/add" class="btn btn-sm btn-primary">Create
					Event</a> <a href="/payment/balance" class="btn btn-sm btn-primary">Fund
					Account</a>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-6">
			<h3>Shared With Me</h3>
			<table class="table table-condensed table-striped">
				<tr>
					<th>Event</th>
					<th>Pay</th>
					<th>From</th>
				</tr>
				<c:forEach var="event" items="${events}">
					<tr>
						<td>${e:forHtml(event.value.name)}</td>
						<td><a href="/payment/make-payment?event=${e:forUriComponent(event.value.id)}">$<c:out
									value="${e:forHtmlAttribute(event.key.amount)}" /></a></td>
						<td>${e:forHtml(users[event.value][0].username)}</td>
					</tr>
				</c:forEach>
			</table>
		</div>

		<div class="col-md-6">
			<h3>My Shared Events</h3>

			<table class="table table-condensed table-striped">
				<tr>
					<th>Event</th>
					<th>Total</th>
					<th>To</th>
				</tr>
				<c:forEach var="o" items="${owned}">
					<tr>
						<td>${e:forHtml(o.name)}</td>
						<td>$${e:forHtml(o.amount)}</td>
						<td><c:forEach var="user" items="${users[o]}">
								<c:out value="${e:forHtmlAttribute(user.username)}" />
							</c:forEach></td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="3" align="right"><a href="/event/add"><span
							class="glyphicon glyphicon-plus" aria-hidden="true"
							title="Add Event"></span></a></td>
				</tr>
			</table>
		</div>
	</div>

	<hr />
	<div class="row">
		<div class="col-md-6">
			<h3>Sent</h3>
			<table class="table table-condensed table-striped">
				<tr>
					<th>Event</th>
					<th>Amount</th>
					<th>To</th>
				</tr>
				<c:forEach var="s" items="${sent}">
					<tr>
						<td>${e:forHtml(s.event.name)}</td>
						<td>$${e:forHtml(s.amount)}</td>
						<td>${e:forHtml(s.receiver.username)}</td>
					</tr>
				</c:forEach>
			</table>
		</div>

		<div class="col-md-6">
			<h3>Received</h3>
			<table class="table table-condensed table-striped">
				<tr>
					<th>Event</th>
					<th>Amount</th>
					<th>To</th>
				</tr>
				<c:forEach var="r" items="${received}">
					<tr>
						<td>${e:forHtml(r.event.name)}</td>
						<td>$${e:forHtml(r.amount)}</td>
						<td>${e:forHtml(r.sender.username)}"</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>

	<hr />

</t:wrapper>