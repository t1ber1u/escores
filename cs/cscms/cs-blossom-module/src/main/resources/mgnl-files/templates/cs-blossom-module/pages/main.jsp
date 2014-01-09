<%@ page import="info.magnolia.module.ModuleRegistry" %>
<%@ page import="info.magnolia.module.templatingkit.STKModule" %>
<%@ page import="info.magnolia.module.templatingkit.sites.Site" %>
<%@ page import="info.magnolia.module.templatingkit.sites.SiteManager" %>
<%@ page import="info.magnolia.module.templatingkit.style.Theme" %>
<%@ page import="info.magnolia.objectfactory.Components" %>
<%@ page import="info.magnolia.module.templatingkit.style.CssFile" %>
<%--@elvariable id="content" type="info.magnolia.jcr.util.ContentMap"--%>
<%--@elvariable id="navigation" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%@ include file="../includes/taglibs.jsp"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>${content.title}</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

        <script src="${pageContext.request.contextPath}/.resources/js/vendor/modernizr-2.6.2.min.js"></script>
		<cms:init />
        <%
                Site site = Components.getComponent(SiteManager.class).getCurrentSite();
                STKModule stkModule = (STKModule) Components.getComponent(ModuleRegistry.class).getModuleInstance("standard-templating-kit");
                Theme theme = stkModule.getTheme(site.getTheme().getName());
                for(CssFile cssFile : theme.getCssFiles()) {
                    out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssFile.getLink() + "\" media=\"" + cssFile.getMedia() + "\" />");
                }
        %>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->

        <!-- Add your site or application content here -->
        <header>
			<jsp:include page="../areas/header.jsp" />
		</header>
		
		<nav>
            <ul>
            <c:forEach items="${navigation}" var="navigationEntry">
                <li><a href="${pageContext.request.contextPath}${navigationEntry.key}.html">${navigationEntry.value}</a></li>
            </c:forEach>
            </ul>		
		</nav>
		
		<section>
            <div class="row">
                <div class="siteAlert">
                    <cms:area name="alert" />
                </div>
                <div class="small-12 medium-9 columns">
			         <cms:area name="main" />
                </div>
                <aside>
                    <div class="small-12 medium-3 columns">
                        <cms:area name="promos" />      
                    </div>
                </aside>
            </div>
		</section>
		
		
		<footer>
            <jsp:include page="../areas/footer.jsp" />
		</footer>
		

		<!--[if lt IE 9]>
			<script src="${pageContext.request.contextPath}/.resources/js/vendor/html5shiv.js"></script>
		<![endif]-->
        <script src="${pageContext.request.contextPath}/.resources/js/vendor/jquery-1.10.2.min.js"></script>
        <script src="${pageContext.request.contextPath}/.resources/js/vendor/foundation.min.js"></script>
        <script src="VAADIN/widgetsets/info.magnolia.ui.vaadin.gwt.MagnoliaWidgetSet/ckeditor/ckeditor.js"></script>
        <script src="${pageContext.request.contextPath}/.resources/js/inContext2.js"></script>
        <script src="${pageContext.request.contextPath}/.resources/js/inContext.js"></script>
        <script src="${pageContext.request.contextPath}/.resources/js/vendor/responsive-carousel.js"></script>
        <script src="${pageContext.request.contextPath}/.resources/js/vendor/responsive-carousel.autoplay.js"></script>               
        <script src="${pageContext.request.contextPath}/.resources/js/vendor/responsive-carousel.pagination.js"></script>               

        <script src="${pageContext.request.contextPath}/.resources/js/main.js"></script>
        <!-- // <script data-main="${pageContext.request.contextPath}/.resources/js/main" src="${pageContext.request.contextPath}/.resources/js/bower_components/requirejs/require.js"></script> -->


        <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->

    </body>
</html>