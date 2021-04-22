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
    <h1>Welcome ${loggedinuser.firstName}!</h1>

    <table class="table">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Creator</th>
            <th scope="col">Vice President</th>
            <th scope="col">Number of Members</th>
            <th scope="col">Action</th>


          </tr>
        </thead>
        <tbody>
            <c:forEach items='${ allgroups }' var='g'>
          <tr>
            
            <td><a href="/groups/${g.id}">${g.name}</a></td>
            <td>${g.creator.firstName} ${g.creator.lastName}</td>
            <td>${g.vp.firstName}</td>
            <td>${g.members.size()}</td>
            <td>
                <c:choose>
                    <c:when test="${g.members.contains(loggedinuser)}">
                        <a href="/leave/${g.id}">Leave</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/join/${g.id}">Join</a>

                    </c:otherwise>
                </c:choose>
            
            </td>

          </tr>
            </c:forEach>
        </tbody>
      </table>


      <h3>Groups Im not in yet</h3>
      <table class="table">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Creator</th>
            <th scope="col">Vice President</th>
            <th scope="col">Number of Members</th>
            <th scope="col">Action</th>


          </tr>
        </thead>
        <tbody>
            <c:forEach items='${ notmygroups }' var='g'>
          <tr>
            
            <td><a href="/groups/${g.id}">${g.name}</a></td>
            <td>${g.creator.firstName} ${g.creator.lastName}</td>
            <td>${g.vp.firstName}</td>
            <td>${g.members.size()}</td>
            <td>
                <c:choose>
                    <c:when test="${g.members.contains(loggedinuser)}">
                        <a href="/leave/${g.id}">Leave</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/join/${g.id}">Join</a>

                    </c:otherwise>
                </c:choose>
            
            </td>

          </tr>
            </c:forEach>
        </tbody>
      </table>


      <a href="/groups/new"><button class="btn btn-primary">Add a new group!</button></a>




    
    <a href="/logout">Logout</a>
</body>
</html>