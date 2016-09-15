<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<t:wrapper>


        <div class="col-md-6">
            <h2 class="form-signin-heading">Edit Profile</h2>
            <form:form class="form-signin form-horizontal" action="/profile/${user.id}" method="post">
                <div class="form-group">
                    <label for="username" class="col-md-4 control-label">Username</label>
                    <div class="col-md-8">
                        <input class="form-control" value="${user.username}" type="text" name="username" readonly="readonly">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="email" class="col-md-4 control-label">First Name</label>
                    <div class="col-md-8">
                        <input class="form-control" type="text" name="firstname" value="${user.firstname}">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="email" class="col-md-4 control-label">Last Name</label>
                    <div class="col-md-8">
                        <input class="form-control" type="text" name="lastname" value="${user.lastname}">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="email" class="col-md-4 control-label">Email</label>
                    <div class="col-md-8">
                        <input class="form-control" type="text" name="email" value="${user.email}">
                    </div>
                </div>
                <div class="col-md-10 text-center">
            		<p>
            			<h4 class="form-signin-heading">Account Balance: <a href="/payment/balance">$${user.balance}</a> </h4>
            		</p>
        		</div>
                
                	<button class="btn btn-primary btn-block" type="submit">Update</button>
            </form:form>

        </div>
        
    <div class="col-sm-12 text-center">
    	<p></p><p>Current URL: <a href="/profile/${id}">/profile/${id}</a></p>
    </div>


</t:wrapper>