<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Mongo FLAC reference implementation :: a Spring Framework demonstration</title>


    <spring:url value="/webjars/bootstrap/2.3.0/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

    <spring:url value="/resources/css/petclinic.css" var="petclinicCss"/>
    <link href="${petclinicCss}" rel="stylesheet"/>

    <spring:url value="/webjars/jquery/1.9.0/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>

    <spring:url value="/webjars/jquery-ui/1.9.2/js/jquery-ui-1.9.2.custom.js" var="jQueryUi"/>
    <script src="${jQueryUi}"></script>

    <spring:url value="/webjars/jquery-ui/1.9.2/css/smoothness/jquery-ui-1.9.2.custom.css" var="jQueryUiCss"/>
    <link href="${jQueryUiCss}" rel="stylesheet"></link>
</head>

currentUser:
  ${currentUser}

<table style="width:300px">
    <tr>
        <td>Spring User Management considers them:
        </td>
    </tr>
    <tr>
        <td>
            <security:authorize access="isAuthenticated()">
            logged in as <security:authentication property="principal.username" />
                <c:url value="/j_spring_security_logout" var="logoutUrl" />
            <a href="${logoutUrl}">Logout</a>
            </security:authorize>

            <security:authorize access="! isAuthenticated()">
            not logged in
            </security:authorize>

        </td>
    </tr>
</table>
<table></table>


