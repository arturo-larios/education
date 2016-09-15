<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<t:wrapper>
    <h1>My Events</h1>
    <table class="table">
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Total Amount</th>
            <th>Completed?</th>
            <th></th>
        </tr>
        <c:forEach var="event" items="${events}">
        <tr>
        <td>${e:forHtml(event.id)}</td>
        <td>${e:forHtml(event.name)}</td>
        <td>$${e:forHtml(event.amount)}</td>
        <td>${e:forHtml(event.completed)}</td>
        <td><a href="/event/delete/${e:forUriComponent(event.id)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>
        </tr>
        </c:forEach>
    </table>
</t:wrapper>