<%@ include file="../includes/taglibs.jsp" %>

<!-- article component -->
<div class="component">
    <div class="player">
        <div class="row">
            <div class="small-12 medium-9 large-9 columns">
                <!-- Profile Picture -->
                <c:if test="${!empty player.photo}">
                    <div class="profile-picture-container">
                        <img class="player-profile" src="${player.photo}"/>
                    </div>
                </c:if>

                <div class="player-name-container">
                    <h6>${player.firstName} ${player.lastName}</h6>
                </div>

                <div class="player-bio">
                    <!-- article body -->
                    <div class="text-justify">
                        ${player.type}
                    </div>
                </div>

                <div class="player-stats">
                    <!-- currently just json -->
                    ${playerStats}
                </div>
            </div>
        </div>
    </div>
</div>
