<%@ include file="../includes/taglibs.jsp" %>

<!-- dlc component -->
<div class="component">
    <div class="player">
        <div class="row">
            <div class="small-12 medium-9 large-9 columns">
                <!-- List DCL Content -->
                <c:if test="${!empty player.photo}">
                    <div class="profile-picture-container">
                        <img class="player-profile" src="${player.photo}"/>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>