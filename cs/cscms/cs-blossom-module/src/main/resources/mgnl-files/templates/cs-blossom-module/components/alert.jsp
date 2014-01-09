<%@ include file="../includes/taglibs.jsp" %>

<c:set var="fullPath">/${fn:toLowerCase(fn:replace(contentPath, " ", "-"))}</c:set>
<c:set var="articleNode" value="${cmsfn:asContentMap(cmsfn:content(fullPath, 'articles'))}"/>

<div class="breakingnews clearfix">
	<div class="title">Breaking News</div>
	<ul class="alerts">
	    <c:forEach items="${articles}" var="article">
            <c:if test="${article.key == 'title'}">
                <c:set var="linkUrl">
                    ${pageContext.request.contextPath}/article?contentPath=${fn:toLowerCase(fn:replace(article.value, " ", "-"))}
                </c:set>
            </c:if>
            <c:if test="${article.key == 'siteAlertalertText'}">
                <c:set var="alertText">
                    ${article.value}
                </c:set>
            </c:if>
	    </c:forEach>
        <li><a href="${linkUrl}">${alertText}</a></li>
	</ul>
</div>