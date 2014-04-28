<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">

<jsp:include page="fragments/headTag.jsp"/>

<body>
<div class="container">
    
    <h2>Hello World!</h2>
   
<h4>${person}</h4>

<datatables:table id="persons" data="${persons}" cdn="true" row="person" theme="bootstrap2" cssClass="table table-striped">
    <datatables:column title="firstName" property="firstName"/>
    <datatables:column title="lastName" property="lastName"/>
    <datatables:column title="classification" property="favorites.sl.classification"/>
    <datatables:column title="sci" cssStyle="width: 100px;">
            <c:forEach var="sci" items="${favorites.sl.sci}" varStatus="status">
                <c:out value="${status.index}"/> <c:out value="${sci}"/>
            </c:forEach>
    </datatables:column>
    <datatables:column title="cartoonCharacters" cssStyle="width: 100px;">
            <c:forEach var="character" items="${favorites.cartoonCharacters}">
                <c:out value="${character}"/>
            </c:forEach>
    </datatables:column>
</datatables:table>


</div>
</body>

</html>