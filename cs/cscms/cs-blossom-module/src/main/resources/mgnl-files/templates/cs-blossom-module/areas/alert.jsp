<%--@elvariable id="components" type="java.util.Collection"--%>
<%@ include file="../includes/taglibs.jsp" %>

<div id="alert">
    <c:forEach items="${components}" var="component">
        <cms:component content="${component}" />
    </c:forEach>
</div>