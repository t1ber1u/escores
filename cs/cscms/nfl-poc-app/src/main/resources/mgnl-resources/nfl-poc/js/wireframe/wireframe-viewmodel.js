console.log("wireframe-viewmodel.js");

(function ($, window, wireframe, undefined){
	var ns = {},
		wff = wireframe.functions,
		wd = wireframe.data,
		selectorModes = wff.generateModeSelectorArray([
			{
				name: "layout_editor",
				workspace: "workspace_layout_editor",
				sidebar2Name: "layout_editor_sidebar2",
				selectorHeight: "150px"
			}, 
			{
				name: "info",
				workspace: "workspace_info",
				sidebar2Name: "info_sidebar2",
				selectorHeight: "200px"
			},
			{
				name: "article",
				workspace: "workspace_article",
				sidebar2Name: "article_sidebar2",
				selectorHeight: "400px"
			},
			{
				name: "image_editor",
				workspace: "workspace_image_editor",
				sidebar2Name: "image_editor_sidebar2",
				selectorHeight: "150px"
			},
			{
				name: "photo_gallery",
				workspace: "workspace_photo_gallery",
				sidebar2Name: "photo_gallery_sidebar2",
				selectorHeight: "340px",
				sidebarMenu: [
				{
					name: '<i class="fa fa-th"></i>',
					bgdColor: '#FAA143',
					clickTab: function(data, event){
						event.stopPropagation();
						console.log("clicked Tile View");
						console.log("data:", data);
						console.log(ko.toJS(ns.modesArray));

						var mediaGallery = _.findWhere(ns.modesArray(), {name: 'photo_gallery'});

						mediaGallery.data.selectedGalleryView('tile');

					},
					mouseOver: wff.mouseOverSidebarMenuTab,
					mouseOut: wff.mouseOutSidebarMenuTab
				}, 
				{
					name: '<i class="fa fa-list"></i>',
					bgdColor: '#F36244',
					clickTab: function(){
						event.stopPropagation();
						console.log("clicked List View");
						var mediaGallery = _.findWhere(ns.modesArray(), {name: 'photo_gallery'});

						mediaGallery.data.selectedGalleryView('list');
					},
					mouseOver: wff.mouseOverSidebarMenuTab,
					mouseOut: wff.mouseOutSidebarMenuTab
				},
				{
					name: '<i class="fa fa-folder-open"></i>',
					bgdColor: '#BE3550',
					clickTab: function(data, event){
						event.stopPropagation();
						console.log("clicked Assets Tab", data.owner);
						var isOpen = data.owner.sidebar2IsOpen();
						
						if(ns.showSidebar2()){
							ns.showSidebar2(false);	
						} else {
							ns.showSidebar2(true);	
						}

						data.owner.selectorContainerWidth((ns.showSidebar2() ? '110px': '240px'));
					}
				},
				{
					name: '<i class="fa fa-plus"></i>',
					bgdColor: '#A7264D',
					clickTab: function(data, event){
						event.stopPropagation();
						console.log("clicked Add tab");

					}
				}]
			}
		]);

	ns.sidebarWidth = ko.observable(140)

	ns.sidebar2WidthValue = ko.observable(300);

	ns.showSidebar2 = ko.observable(false);


	ns.sidebarWrapperWidth = ko.computed(function(){
		return ns.showSidebar2() ? (ns.sidebar2WidthValue() + 140) + "px"  :  ns.sidebarWidth() + "px";
	});

	ns.sidebarNavWidth = ko.computed(function(){
		return ns.sidebarWidth() + "px";
	})

	ns.wrapperPaddingLeft = ko.computed(function(){
		return ns.showSidebar2() ? (ns.sidebar2WidthValue() + 140) + "px" :  ns.sidebarWidth() + "px";
	});

	ns.showSidebar2Inner = ko.observable(false);

	ns.sidebar2Width = ko.computed(function(){
		if(ns.showSidebar2()){
			window.setTimeout(function(){
				ns.showSidebar2Inner(true);
			}, 400);
			return ns.sidebar2WidthValue() + "px";
		} else {
			ns.showSidebar2Inner(false);
			return "0px";
		}
	});

	ns.modesArray = ko.observableArray(selectorModes);

	ns.assetArray = ko.observableArray([]);

	ns.authorsArray = ko.observableArray([]);

	ns.mediaGalleryArray = ko.observableArray([]);

	ns.assetsRecieved = ko.observable(false);

	ns.mediaGalleriesRecieved = ko.observable(true);

	ns.currentSelectedMode = ko.observable("photo_gallery");

	ns.currentWorkspace = ko.observable(selectorModes[3]);

	ns.showFullScreenModal = ko.observable(false);

	ns.existingArticlesArr = ko.observableArray([]);

	ns.selectedArticle = ko.observable();

	ns.articleHasBeenSelected = ko.observable(false);

	ns.currentWorkingArticle = ko.observable();

	ns.sidebar2Template = ko.observable();

	ns.selectedMediaGallery = ko.observable();

	ns.creatingNewArticle = ko.observable(false);

	ns.loaded = ko.observable(false);



	ns.clickEditSelectedArticle = function(){
		ns.currentWorkingArticle(ns.selectedArticle());
		ns.showFullScreenModal(false);
		console.log("selectedArticle", ns.selectedArticle());
		ns.loadArticle();
		ns.loadPhotoGallery();
	}

	ns.createNewArticle = function(){
		console.log("createNewArticle");
		ns.showFullScreenModal(false);
	}

	ns.loadArticle = function(){
		console.log("loadArticle");
		var asm = _.findWhere(ns.modesArray(), {name: "article"}),
			propArr = ns.currentWorkingArticle().properties(),
			propObj = {};

		propArr.forEach(function (v, i, arr){
			propObj[v.name()] = v;
		});

		console.log("propObj", propObj);

		if(propObj.hasOwnProperty('body')){asm.data.articleContent(propObj['body'].values()[0] || '');};
		if(propObj.hasOwnProperty('title')){asm.data.articleTitle(propObj['title'].values()[0] || '');};
		if(propObj.hasOwnProperty('lead')){asm.data.articleLead(propObj['lead'].values()[0] || '');};

		
	}

	ns.loadPhotoGallery = function(){
		console.log("loadPhotoGallery");

		wd.getDAM(function (status, data){

			if(status === 200){
				console.log("Assets: ", ko.toJS(data));
				ns.assetArray(data);
				ns.assetsRecieved(true);	
			}
		})

		// wd.getDAM(ns.assetArray);

	}

	ns.setWorkspace = function(dataObj, event){
		ns.currentWorkspace(dataObj);
		ns.currentSelectedMode(dataObj.name);
		dataObj.isCurrentSelectedMode(true);
	}

	ns.setWorkspace2 = function(dataObj, event){
		console.log("setWorkspace2");
		ns.currentWorkspace(dataObj);
		ns.currentSelectedMode(dataObj.name);
		dataObj.isCurrentSelectedMode(true);
	}

	ns.expandSelectedMode = function(dataObj, event){
		console.log("expandSelectedMode");
		console.log(selectorModes);
		console.log(dataObj);
	}

	ns.init = function(){
		console.log("wireframe.vm.init");
		ns.loaded(true);

		wd.getArticles(ns.existingArticlesArr);

		wd.getAuthors(ns.authorsArray);

		ns.loadPhotoGallery();

		wd.getMediaGalleries(ns.mediaGalleryArray);

	}

	wireframe.vm = ns;
})($, window, wireframe);	