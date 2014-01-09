<%@ include file="../includes/taglibs.jsp" %>

<div class="newsComponent">
    <h3>News</h3>
    <ol>
        <c:forEach items="${articles}" var="article">
            <c:forEach items="${article.value}" var="property">
                <c:if test="${property.key == 'title'}">
                    <c:set var="linkUrl">
                        ${pageContext.request.contextPath}/article?contentPath=${fn:toLowerCase(fn:replace(property.value, " ", "-"))}
                    </c:set>
                    <c:set var="title">${property.value}</c:set>
                </c:if>
                <c:if test="${property.key == 'lead'}">
                    <c:set var="lead">${property.value}</c:set>
                </c:if>
                <c:if test="${property.key == 'hero'}">
                    <c:set var="hero">${property.value}</c:set>
                </c:if>
            </c:forEach>

            <li>
                <a href="${linkUrl}">
                    <div class="newsHero"><img class="hero" src="${cmsfn:linkForWorkspace("dam", hero)}"/></div>
                </a>
                <div class="newsTitle">${title}</div>
                <c:if test="${!empty lead}">
                    <div class="newsLead">
                        <a href="${linkUrl}">${lead}</a>
                    </div><a class="newsReadMore" href="${linkUrl}">Read</a>
                </c:if>
            </li>
        </c:forEach>
    </ol>
</div>