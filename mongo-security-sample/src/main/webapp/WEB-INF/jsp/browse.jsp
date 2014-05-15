<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">

<jsp:include page="fragments/headTag.jsp"/>

<body>
<div class="container">
    
    <h2>Person List</h2>
   
<h4>${person}</h4>

<datatables:table id="persons" data="${persons}" cdn="true" paginationType="full_numbers" info="true" row="person"  theme="bootstrap2" cssClass="table table-striped">
    <datatables:column title="firstName" property="firstName" sortable="true"/>
    <datatables:column title="lastName" property="lastName"/>
    <datatables:column title="country">
      <c:out value="${person.country.value}"/>
      <small class="muted">(<c:out value="${person.country.securityLabel}"/>)</small>
    </datatables:column>
    
    <datatables:column title="ssn">
      <c:out value="${person.ssn.value}"/>
      <small class="muted">(<c:out value="${person.ssn.securityLabel}"/>)</small>
    </datatables:column>
    
   
    <datatables:column title="cartoonCharacters">
            <c:forEach var="character" items="${person.favorites.cartoonCharacters}">
                <c:out value="${character}"/>
                <br/>
            </c:forEach>
    </datatables:column>
</datatables:table>


</div>
</body>

</html>