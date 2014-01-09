<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title></title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
        <link rel="stylesheet" href="${contextPath}/.resources/nfl-poc/css/normalize.css">
        <link rel="stylesheet" href="${contextPath}/.resources/nfl-poc/css/main.css">
        <link rel="stylesheet" href="${contextPath}/.resources/nfl-poc/css/bootstrap.min.css">
        <link rel="stylesheet" href="${contextPath}/.resources/nfl-poc/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="${contextPath}/.resources/nfl-poc/js/vendor/aloha/css/aloha.css">
        <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

        <script src="${contextPath}/.resources/nfl-poc/js/vendor/modernizr-2.6.2.min.js"></script>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->

        <!-- Add your site or application content here -->

        <#-- Full screen modal -->
        <#-- <div class="full-screen-modal" data-bind="if: showFullScreenModal">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2">
                    <div class="modal-content">
                        <div class="row">
                            <div class="col-sm-5">
                                <div class="modal-side modal-left-side">
                                    <div class="modal-side-text">Select an existing Article to Edit</div>
                                    <select name="existing-article-select" id="existing-article-select" class="form-control" data-bind="options: existingArticlesArr, optionsText: 'name', value: selectedArticle">
                                    </select>
                                    <div class="existing-article-edit-btn-wrapper">
                                        <button class="btn btn-primary btn-default existing-article-edit-btn"
                                                data-bind="click: clickEditSelectedArticle">Edit</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-2">
                                <div class="center-col">
                                    <h2 class="center-col-or">- OR -</h2>
                                </div>
                            </div>
                            <div class="col-sm-5">
                                <div class="modal-side modal-right-side">
                                    <div class="modal-side-text">Create a New Article</div>
                                    <div class="new-article-btn-wrapper">
                                        <button class="btn btn-primary btn-lg" data-bind="click: createNewArticle">Create a New Article</button>    
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div> -->
        <#-- End Full Screen Modal -->
        
        <#-- Navbar -->
        <#-- <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
          <div class="container">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="#"></a>
            </div>
            <div class="collapse navbar-collapse">
              <ul class="nav navbar-nav">
              </ul>
            </div>
          </div>
        </div> -->

        <#-- End Navbar -->

        <#-- Main Body -->

        <div id="wrapper" data-bind="style: {paddingLeft: wrapperPaddingLeft}">

            <#-- Sidebar Nav -->

            <div id="sidebar-wrapper" data-bind="style: {width: sidebarWrapperWidth}">
                <ul class="sidebar-nav" data-bind="style: {width: sidebarNavWidth}">
                    <li class="sidebar-brand"><a href="#">Wireframe</a></li>
                    <div data-bind="foreach: modesArray">
                        <div class="sidebar-mode-selector" data-bind="
                        click: $root.setWorkspace2,
                        css: {expanded: selectedModel}, 
                        style: {height: selectorHeight()}">
                            <div data-bind="template: {name: name, data: selectorData}"></div>
                        </div>
                    </div>
                </ul>
                <div class="sidebar2" data-bind="style: {width: sidebar2Width}">
                    <div class="sidebar2Inner" data-bind="visible: showSidebar2Inner">
                        <div data-bind="template: {name: currentWorkspace().sidebar2Name, data: currentWorkspace().sidebar2Data}"></div>
                    </div>
                </div>
            </div>

            <#-- End Sidebar Nav -->

            
            <div id="page-content-wrapper">
                <div class="content-header">
                    
                </div>
                <div class="page-content inset">
                    <div class="row">
                        <div class="mode-workspace col-sm-12">
                            <div data-bind="template:{name: currentWorkspace().workspace, data: currentWorkspace().data} "></div>
                        </div>    
                    </div>
                </div>
            </div>
        </div>

        <#-- End Main Body -->
        <script>
        /*Small script to remove Aloha sidebar*/
        Aloha = {};
        Aloha.settings = {sidebar: {disabled: true}};
        </script>

        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.10.2.min.js"><\/script>')</script>
        <script src="${contextPath}/.resources/nfl-poc/js/vendor/underscore-min.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/vendor/bootstrap.min.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/vendor/moment.min.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/vendor/knockout-3.0.0.min.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/vendor/aloha/lib/require.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/vendor/aloha/lib/aloha.js" 
        data-aloha-plugins="common/ui,common/format,common/highlighteditables,common/link, common/image, common/align"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/vendor/croppy.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/wireframe/wireframe-init.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/wireframe/wireframe-functions.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/wireframe/wireframe-model.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/wireframe/wireframe-api.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/wireframe/wireframe-data.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/wireframe/wireframe-viewmodel.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/plugins.js"></script>
        <script src="${contextPath}/.resources/nfl-poc/js/main.js"></script>

        <!-- Mode Selector templates -->        

        <script type="text/html" id="layout_editor">
            <h5 class="selectorTitle">Layout Editor</h5>
            <div class="internal" data-bind="visible: showInternalDiv"></div>
            
        </script>
    
        <script type="text/html" id="info">
            <h5 class="selectorTitle">Info</h5>
            <div class="internal" data-bind="visible: showInternalDiv"></div>
        </script>

        <script type="text/html" id="article">
            <h5 class="selectorTitle">Article</h5>
            <div class="internal" data-bind="visible: showInternalDiv">
                <div class="selector-container">
                    <!-- ko 'if': !$root.creatingNewArticle() -->
                        <div class="form-group">
                            <select name="existing-article-selector"
                                            id="existing-article-selector"
                                            class="form-control"
                                            data-bind="
                                                options: $root.existingArticlesArr,
                                                optionsText: 'name',
                                                value: changeSelectedArticle">
                            </select>
                        </div>
                        <h5>Author: <span data-bind="text: author"></span></h5>
                        <h5>Created: <span data-bind="text: created"></span></h5>
                        <div class="form-group">
                            <button class="btn btn-default" data-bind="click: clickCreateNewArticle">Create New Article</button>
                        </div>
                        

                    
                    <!-- /ko -->
                    <!-- ko 'if': $root.creatingNewArticle() -->
                    <div class="form">
                        <div class="form-group">
                            <label for="newArticleTitle">Title</label>
                            <input type="text" class="form-control" id="newArticleTitle">
                        </div>
                        <div class="form-group">
                            <label for="newArticleLead">Lead-in</label>
                            <input type="textarea" class="form-control" id="newArticleLead">
                        </div>
                        <div class="form-group">
                            <label for="newArticleAuthor">Author</label>
                            <select name="newArticleAuthor" id="newArticleAuthor" class="form-control" data-bind="options: $root.authorsArray, optionsText: 'name', value: selectedAuthor">
                            </select>
                        </div>
                        <div class="form-group">
                            <button class="btn btn-default" data-bind="click: confirmCreateNewMediaGallery">Create Article</button>
                        </div>
                        <div class="form-group">
                            <button class="btn btn-default" data-bind="click: clickCancelNewArticle">Cancel</button>
                        </div>
                    </div>
                    <!-- /ko -->    
                </div>
            </div>
        </script>

        
        
        <script type="text/html" id="image_editor">
            <h5 class="selectorTitle">Image Editor</h5>
            <div class="internal" data-bind="visible: showInternalDiv"></div>
        </script>

        <script type="text/html" id="photo_gallery">
            <h5 class="selectorTitle">Media Gallery</h5>
            <div class="internal" data-bind="visible: showInternalDiv, style: {width: selectorContainerWidth}">
                <div class="selector-container">
                    <!-- ko 'if': $root.mediaGalleriesRecieved() && showPhotoGallerySelector() -->
                    <div class="form-group">
                        <select name="media-gallery-selector" 
                                id="media-gallery-selector"
                                class="form-control"
                                data-bind="options: $root.mediaGalleryArray, 
                                           optionsText: 'name',
                                           value: changeSelectedMediaGallery"></select>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-default" data-bind="click: clickCreateNewMediaGallery">Create</button>
                    </div>
                    <div class="media-gallery-selector-menu">
                        <div class="sidebar-menu-container">
                            <div class="sidebar-menu">
                                <div data-bind="foreach: sidebarMenu">
                                    <div class="sidebar-menu-tab" data-bind="
                                        click: clickTab, 
                                        style: {backgroundColor: bgdColor}, 
                                        event: { mouseover: mouseOver,
                                                 mouseout: mouseOut">
                                        <div class="tab-name" data-bind="html: name"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <!-- /ko -->
                    <!-- ko 'if': showCreateNewMediaGallerySelector() -->
                        <div class="form-group">
                            <label for="newMediaGalleryName">Name</label>
                            <input type="text" class="form-control" id="newMediaGalleryName" data-bind="value: newMediaGalleryName">
                        </div>
                        <div class="form-group">
                            <label for="newMediaGalleryTitle">Title</label>
                            <input type="text" class="form-control" id="newMediaGalleryTitle" data-bind="value: newMediaGalleryTitle">
                        </div>
                        <div class="form-group">
                            <label for="newMediaGalleryLead">Lead</label>
                            <input type="text" class="form-control" id="newMediaGalleryLead" data-bind="value: newMediaGalleryLead">
                        </div>
                        <br>

                        <button class="btn btn-default" data-bind="click: confirmCreateNewMediaGallery">Create</button>
                        <button class="btn btn-default" data-bind="click: cancelCreateNewMediaGallery">Cancel</button>    

                    <!-- /ko --> 

                </div>    
            </div>
        </script>


        
        

        


        

        

        <!-- End Mode Selector templates -->


        <#-- Sidebar2 templates -->
        <script type="text/html" id="layout_editor_sidebar2">
        </script>

        <script type="text/html" id="info_sidebar2">
        </script>

        <script type="text/html" id="article_sidebar2">
            <!-- ko 'if': !$root.articleHasBeenSelected() -->
                <div class="sidebar2-container">
                    <p>No Article is Selected...yet.</p>
                    <br>
                    <p>Please select an article you would like to edit.</p>
                    <p>- OR - </p>
                    <p> Click below to create a new article</p>
                </div>    

            <!-- /ko -->
            <!-- ko 'if': $root.articleHasBeenSelected() -->
            <!-- /ko -->
        </script>
        



        <script type="text/html" id="image_editor_sidebar2">
        </script>
    
        <script type="text/html" id="photo_gallery_sidebar2">

            <!-- ko 'if': !$root.selectedMediaGallery() -->
                <div class="sidebar2-container">
                    <p>No Media gallery loaded</p>
                    <p>Please Select a Media Gallery you would like to edit below:</p>
                    <select class="form-control" name="media-gallery-select" id="media-gallery-select" data-bind="options: mediaGalleries, optionsCaption: 'Choose a Media Gallery'"></select>
                    <button class="btn btn-primary">Choose</button>
                    <p>- or -</p>
                    
                    <p>- or -</p>
                    <p> Drag an image or folder into the dashed box below to create a new media Gallery</p>
                    <div class="pgal-sidebar2-drop-zone"></div>


                </div>     

            <!-- /ko -->
            <!-- ko 'if': $root.selectedMediaGallery() -->
                <p>Media gallery toolbar here.</p>
            <!-- /ko -->
        </script>
    
        <#-- End Sidebar2 templates -->

        <!-- Workspace templates -->

        <script type="text/html" id="workspace_layout_editor">
            <h2 data-bind="click: $root.displayData">This is the Layout Editor Workspace</h2>
            <p data-bind="text: testLayoutEditor"></p>
        </script>

        <script type="text/html" id="workspace_info">
            <h2>This is the Info Workspace</h2>
        </script>

        <script type="text/html" id="workspace_article">
            <h2 class="article-editable" data-bind="html: articleTitle" data-aloha-propname="title"></h2>
            <h3 class="article-editable" data-bind="html: articleLead" data-aloha-propname="lead"></h3>
            <div class="article-editable" data-bind="html: articleContent" data-aloha-propname="body"></div>
        </script>

        <script type="text/html" id="workspace_image_editor">
            <h2>This is the Image Editor Workspace</h2>

            <div id="file_drop_zone" data-bind="
                event: {
                    dragover: dropZoneDragOver,
                    dragleave: dropZoneLeave,
                    drop: dropInDropZone
                }, 
                css: {
                    borderBlack: overDropZone
                },
                visible: showImageUpload">
                <div class="file-drop-zone-text-area">
                    <h4>Drag your media here</h4>
                    <p>-or-</p>
                    <div class="btn btn-primary" data-bind="click: clickUploadFile">upload from your computer</div>
                </div>
            </div>
            <div class="row">
                <div id="image-editor-container" data-bind="visible: !showImageUpload()">
                    <div class="image-editor-sidebar-container col-sm-2">
                        <div class="image-editor-sidebar"></div>
                    </div>
                    <div class="image-editor-canvas-container col-sm-10">
                        <canvas height="490" width="490" id="image-editor-canvas"></canvas>    
                    </div>
                </div>
            </div>
            
            <input type="file" id="hidden-file-input">
        </script>

        
        
        <script type="text/html" id="workspace_photo_gallery">
            <div class="media-gallery-container">
                <!-- ko 'if': selectedGalleryView() == 'list' -->
                    <p>List</p>

                <!-- /ko -->
                <!-- ko 'if': selectedGalleryView() == 'tile' -->
                    <p>Tile</p>
                <!-- /ko -->
            </div>
        </script>
        
        <div class="media-gallery-list-view">
            
        </div>

        <div class="media-gallery-tile-view">
            
        </div>


            
        

        


        


        
        <!-- End Workspace templates --> 

        <!-- Call to load knockout viewmodel here.  -->

        <script>
            ko.applyBindings(wireframe.vm);
        </script>

        <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
        <script>
            (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
            function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
            e=o.createElement(i);r=o.getElementsByTagName(i)[0];
            e.src='//www.google-analytics.com/analytics.js';
            r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
            ga('create','UA-XXXXX-X');ga('send','pageview');
        </script>
    </body>
</html>
    