<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<t:wrapper>
    <h1>Shared Events</h1>
    <h6><i>Who do I owe money to?</i></h6>
    <table class="table">
        <tr>
            <th>#</th>
            <th>Owner</th>
            <th>My Share</th>
			<th>Total</th>
            <th>Event</th>
            <th>Date</th>
            <th>Pay Now</th>
        </tr>
        <c:forEach var="event" items="${events}">
        <tr>
        <td>${e:forHtml(event.id)}</td>
        <td>${e:forHtml(users.get(event.id).firstname)} ${e:forHtml(users.get(event.id).lastname)}</td>
        <td>$${e:forHtml(memberships.get(event.id).amount)}
        <td>$${e:forHtml(event.amount)}</td>
        <td>${e:forHtml(event.name)}</td>
        <td>${e:forHtml(event.created)}</td>
        <td><a href="/payment/make-payment?event=${e:forUriComponent(event.id)}"><span class="glyphicon glyphicon-usd" aria-hidden="true"></span></a></td>
        </tr>
        </c:forEach>
    </table>
</t:wrapper>