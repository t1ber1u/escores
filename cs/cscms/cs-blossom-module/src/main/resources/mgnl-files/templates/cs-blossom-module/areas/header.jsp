<%--@elvariable id="components" type="java.util.Collection"--%>
<%@ include file="../includes/taglibs.jsp" %>

   <div class="fixed show-for-small">
            <nav class="top-bar" data-topbar>
                <ul class="title-area">
                      <li class="toggle-topbar menu-icon"><a href="#"><span></span></a></li>
                      <li class="name">
                        <img src="${pageContext.request.contextPath}/.resources/img/logo_mobile.png" />
                        <h1><a href="#">Steelers</a></h1>
                      </li>
                </ul>
                <section class="top-bar-section">
                    <!-- Right Nav Section -->
                    <ul>
                                <li><a href="#"><span>Watch Games</span></a></li>
                                <li><a href="#"><span>News</span></a></li>
                                <li><a href="#"><span>Video & Audio</span></a></li>
                                <li><a href="#"><span>Team</span></a></li>
                                <li><a href="#"><span>Schedule</span></a></li>
                                <li><a href="#"><span>Tickets & Stadium</span></a></li>
                                <li><a href="#"><span>Community</span></a></li>
                                <li><a href="#"><span>Fan Zone</span></a></li>
                                <li><a href="#"><span>Shop</span></a></li>
                    </ul>
                </section>
            </nav>

    </div>


    <div class="row ads">
        <div class="small-12 large-9 columns">
            <img class="ad1" src="${pageContext.request.contextPath}/.resources/img/ad_720.png" />
        </div>
        <div class="large-3 columns">
            <img class="ad2 hide-for-small hide-for-medium" src="${pageContext.request.contextPath}/.resources/img/ad_chip.png" />
        </div>
    </div>


    <div class="hide-for-small">
        <div class="row">
            <div class="medium-8 large-5 columns">
                <h1><a href="/" class="logo"></a></h1>
            </div>
            <div class="large-5 hide-for-small hide-for-medium columns">
                <div class="player "></div>
            </div>
            <div class="medium-4 large-2 columns">
                <div class="shop"><img src="${pageContext.request.contextPath}/.resources/img/shopnow.jpg" /></div>
            </div>
        </div>

        <div class="row">
            <div class="medium-12 columns">            
                <nav>
                    <div class="button-bar">
                        <ul class="button-group">
                            <li><a href="#"><span>Watch Games</span></a></li>
                            <li><a href="#"><span>News</span></a></li>
                            <li><a href="#"><span>Video & Audio</span></a></li>
                            <li><a href="#"><span>Team</span></a></li>
                            <li><a href="#"><span>Schedule</span></a></li>
                            <div class="hide-for-small hide-for-medium">
                                <li><a href="#"><span>Tickets & Stadium</span></a></li>
                                <li><a href="#"><span>Community</span></a></li>
                                <li><a href="#"><span>Fan Zone</span></a></li>
                                <li><a href="#"><span>Shop</span></a></li>
                            </div>
                        </ul>
                    </div>
                </nav>
            </div>
        </div>
    </div>

<!-- <ul class="alert">
    <li class="alert-message"><a href="#"><span>Alert Message</span></a></li>
    <li><a href="#"><span>Link</span></a></li>
    <li><span>Messaging for Alert</span></li>
    <li class="close"><a href="#"><span>Close</span></a></li>
</ul> -->