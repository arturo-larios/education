<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:wrapper>
    <div class="row">
        <div class="col-md-10">
            <h2 class="form-signin-heading">Search Events</h2>
            
            <form:form class="form-signin" action="/event/search" modelAttribute="event" method="get">

            <input type="text" id="q" name="q" class="form-control" placeholder="Search Terms" value="${q}" required autofocus>
            
            </form:form>
        </div>
    </div>
    <div class="row">
		<div class="col-md-10">           
            <h3>My Events</h3>
    		<table class="table">
		        <tr>
		            <th>Name</th>
		            <th>Total Amount</th>
		            <th>Completed?</th>
		            <th></th>
		        </tr>
		        <c:forEach var="event" items="${my_events}">
		        <tr>
		        <td>${event.name}</td>
		        <td>$${event.amount}</td>
		        <td>${event.completed}</td>
		        <td><a href="/event/delete/${event.id}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>
		        </tr>
		        </c:forEach>
		    </table>
		    
            <hr />
        </div>
    </div>
    <div class="row">
        <div class="col-md-10">
            		    
		    <h3>To Pay</h3>
		    <table class="table">
		        <tr>
		            <th>Date</th>
		            <th>Owner</th>
		            <th>Pay</th>
					<th>Total</th>
		            <th>Event</th>
		        </tr>
		        <c:forEach var="me" items="${m_events}">
		        <tr>
		        <td>${me.key.created}</td>
		        <td>${m_users[me.key.id].firstname} ${m_users[me.key.id].lastname} (${m_users[me.key.id].username}) </td>
		        <td><a href="/payment/make-payment?event=${me.value.id}">$${me.key.amount}</a></td>
		        <td>$${me.value.amount}</td>
		        <td>${me.value.name}</td>
		        </tr>
		        </c:forEach>
		    </table>

        </div>
    </div>

</t:wrapper>