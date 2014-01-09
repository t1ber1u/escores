<%--@elvariable id="content" type="info.magnolia.jcr.util.ContentMap"--%>
<%@ include file="../includes/taglibs.jsp"%>

<h1>${content.heading}</h1>

<p>${cmsfn:decode(content).body}</p>
