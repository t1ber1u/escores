<%@ include file="../includes/taglibs.jsp" %>

<c:set var="fullPathToArticle">/${fn:toLowerCase(fn:replace(contentPath, " ", "-"))}</c:set>
<c:set var="articleNode" value="${cmsfn:asContentMap(cmsfn:content(contentPath, 'articles'))}"/>

<!-- article component -->
<div class="component">
    <div class="article">
        <div class="row">
            <div class="small-12 medium-9 large-9 columns">
                <c:if test="${!empty articleNode.series}">
                    <c:if test="${articleNode.series == 'gameday'}">
                        <c:set var="bannerSrc">${pageContext.request.contextPath}/.resources/img/gameday-banner.png</c:set>        
                    </c:if>
                    <img class="article-banner" src="${bannerSrc}" />
                </c:if>
                <!-- Hero Image -->
                <c:if test="${!empty articleNode.hero}">
                    <div class="top-image-container">
                        <img class="hero" src="${cmsfn:linkForWorkspace("dam", articleNode.hero)}"/>
                    </div>
                </c:if>

                <c:if test="${!empty articleNode.author}">
                    <div class="byline-container">
                        <div class="byline-img" style="height: 40px; width: 40px;">
                            <img class="authorImage" src="${authorImageLink}"/>
                        </div>
                        <h6>By ${authorFirstName} ${authorLastName}</h6>
                    </div>
                </c:if>

                <div class="article-body">
                    <h2 class="inContext" data-inContextField="title" contenteditable="true">${articleNode.title}</h2>
                    <h3 class="inContext" data-inContextField="lead" contenteditable="true">${articleNode.lead}</h3>

                    <!-- article body -->
                    <div class="inContext text-justify" data-incontextfield="body" contenteditable="true">
                        ${articleNode.body}
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
