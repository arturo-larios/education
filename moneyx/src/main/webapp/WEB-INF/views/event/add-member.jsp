<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<t:wrapper>
    <div class="row">
        <h2 class="form-signin-heading">Add a Member to an existing event</h2>
        <form:form class="form-signin" action="/event/add-member" modelAttribute="event" method="post">
        <div class="col-md-6">
            <h3>Select event</h3>
            <select id="eventId" name="eventId" size="3" class="form-control">
            <c:forEach var="event" items="${events}">
                <option value="${e:forHtmlAttribute(event.id)}">${e:forHtml(event.name)} (Total Amount: $${e:forHtml(event.amount)})</option>
            </c:forEach>
            </select>

            <label for="amount" class="sr-only">Amount</label>
            <div class="input-group">
                <div class="input-group-addon">$</div>
                <input type="number" id="amount" name="amount" class="form-control" placeholder="Amount" required>
            </div>

            <hr />

            <button class="btn btn-lg btn-primary btn-block" type="submit">Add</button>
        </div>
        <div class="col-md-6">
            <h3>Select users</h3>
            <select id="userId" name="userId" size="4" class="form-control">
            <c:forEach var="user" items="${users}">
                <option value="${e:forHtmlAttribute(user.id)}">${e:forHtml(user.username)} (${e:forHttml(user.firstname)} ${e:forHtml(user.lastname)})</option>
            </c:forEach>
            </select>
        </div>
        </form:form>
    </div>

</t:wrapper>