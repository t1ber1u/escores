<%@ include file="../includes/taglibs.jsp" %>

<div class="carousel carousel-fade" data-autoplay="" data-transition="fade" data-paginate="">
    <c:forEach items="${articles}" var="article">
        <c:set var="fullPath">/${fn:toLowerCase(fn:replace(article.value, " ", "-"))}</c:set>
        <c:set var="articleNode" value="${cmsfn:asContentMap(cmsfn:content(fullPath, 'articles'))}"/>

        <c:set var="linkUrl">
            ${pageContext.request.contextPath}/article?contentPath=${fn:replace(fn:toLowerCase(article.value), " ", "-")}
        </c:set>

        <c:if test="${!empty articleNode.hero}">
            <div>
                <img class="hero" src="${cmsfn:linkForWorkspace("dam", articleNode.hero)}"/>
                <a class="title" href="${linkUrl}">${articleNode.title}</a>
                <a class="lead" href="${linkUrl}">${articleNode.lead}</a>
            </div>
        </c:if>
        
    </c:forEach>
</div>