<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- c:out ; c:foreach; c:if  -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
    <!-- formatting things like dates  -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
</head>
<body>
    <h1>Edit this group:</h1>
    <form:form action="/groups/update/${group.id}" method="post" modelAttribute="group">
    
            <p>
                <form:label path="name">Name</form:label>
                <form:errors path="name"/>
                <form:input path="name"/>
            </p>
            <p>
                <form:label path="description">Description</form:label>
                <form:errors path="description"/>
                <form:textarea path="description"/>
            </p>
            <p>
                <form:label path="vp">Select the VP:</form:label>
                <form:errors path="vp"/>
                <form:select path="vp">
                    <c:forEach items='${ allusers }' var='u'>
                        <c:choose>
                            <c:when test="${group.vp==u}">
                                <form:option value="${u.id}" selected="selected">${u.firstName}</form:option>
                            </c:when>
                            <c:otherwise>
                                <form:option value="${u.id}">${u.firstName}</form:option>

                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
                

            </p>
            
            <input type="submit" value="Submit"/>
        </form:form>    
</body>
</html>