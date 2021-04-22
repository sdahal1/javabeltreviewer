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
    <h1>Here is info about this group: ${groupToshow.name}</h1>
    <p>Creator: ${groupToshow.creator.firstName}</p>
    <p>VP: ${groupToshow.vp.firstName}</p>
    <p>Description: ${groupToshow.description}</p>
    
    <h3>Users who are members of this group</h3>
    <table class="table">
        <thead>
          <tr>
           
            <th scope="col">Name</th>
            <th scope="col">Number of groups the person belongs to</th>
          </tr>
        </thead>
        <tbody>
            <c:forEach items='${ groupToshow.members }' var='user'>
          <tr>
            <td>${user.firstName}</td>
            <td>${user.groupsBelongTo.size()}</td>
          </tr>
            </c:forEach>
            
         
        </tbody>
    </table>

    <!-- if loggedinuser is the creator of the group  -->
    <c:choose>
        <c:when test="${loggedinuser == groupToshow.creator}">
            <a href="/edit/${groupToshow.id}"><button class="btn btn-info">Edit</button></a>
            <a href="/delete/${groupToshow.id}"><button class="btn btn-danger">Delete</button></a>
        </c:when>
        <c:otherwise>

        </c:otherwise>
    </c:choose>


</body>
</html>