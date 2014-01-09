console.log("wireframe-model.js");

(function ($, window, wireframe, undefined){
	var ns = {},
		wf = wireframe.functions;

	ns.JCRProperty = function(data){
		var self = this,
			obj = {
				name: data.name,
				type: data.type,
				multiple: 'false',
				values: []
			}

		data.values.forEach(function (v, i, arr){
			obj.values.push(v)
		});

		return obj;
	}

	ns.JCRNode = function(data){
		var self = this,
			obj = {
				name: data.name,
				type: data.type,
				properties: []
			};

		if(data.path){obj.path = data.path};

		data.properties.forEach(function (v, i, arr){
			obj.properties.push(ns.JCRProperty(v))
		});

		return obj;
	}

	ns.SelectorModel = function(data){
		console.log("data", data);
		var self = this;

		this.name = data.name;
		this.workspace = data.workspace;
		this.sidebar2Name = data.sidebar2Name;

		this.selectedModel = ko.observable(false);

		this.selectorHeight = ko.computed(function(){
			return self.selectedModel() ? self.openHeight : "60px";
		});

		this.openHeight = data.selectorHeight;

		this.isCurrentSelectedMode = ko.computed({
			read: function(){
				return self.selectedModel();
			},
			write: function(value){
				var vm = wireframe.vm;

				wf.disableAloha('.article-editable');

				vm.modesArray().forEach(function (v, i, arr){
					if(v.name == vm.currentSelectedMode()){
						v.selectedModel(true);

						if(v.selectorData.hasOwnProperty('showInternalDiv')){
							window.setTimeout(function(){
								v.selectorData.showInternalDiv(true);
							}, 400);
						};
						if(vm.currentSelectedMode() !== 'photo_gallery'){
							vm.showSidebar2(false);
							vm.sidebarWidth(140);
						}
						if(vm.currentSelectedMode() == 'article'){
							console.log("selected Article");
							if(!vm.articleHasBeenSelected()){
								console.log("no article selected");
								// vm.showSidebar2(true);
								vm.sidebarWidth(240);
							}
							wf.enableAloha('.article-editable', 500);
						}
						
						if(vm.currentSelectedMode() == 'photo_gallery' && vm.assetsRecieved() === true){
							console.log("photo's dude.");
							vm.sidebarWidth(240);
							// vm.showSidebar2(true);
						}
					} else{
						v.selectedModel(false);	
						if(v.selectorData.hasOwnProperty('showInternalDiv')){v.selectorData.showInternalDiv(false)};
					} 
				})
				self.selectedModel(value);
			},
			owner: self
		});
	}

	ns.selectorTemplates = {
		layout_editor: function(data){
			var self = this;

			this.showInternalDiv = ko.observable(false);
		},
		info: function(data){
			var self = this;

			this.showInternalDiv = ko.observable(false);
		},
		article: function(data){
			var self = this;

			this.showInternalDiv = ko.observable(false);

			this.clickSelectArticleBtn = function(){
				console.log("clickSelectArticleBtn");
				wireframe.vm.loadArticle();

			}

			this.clickCreateNewArticle = function(){
				console.log("clickCreateNewArticle");
				wireframe.vm.creatingNewArticle(true);
			}

			this.clickCancelNewArticle = function(){
				wireframe.vm.creatingNewArticle(false);
			}

			this.selectedAuthor = ko.observable();

			this.authorObj = ko.observable();

			this.author = ko.observable();
			this.title = ko.observable();
			this.created = ko.observable();


			this.changeSelectedArticle = ko.computed({
				read: function(){
					// return wireframe.vm.selectedArticle();
					return "nothing.";
					
				},
				write: function(value){
					var vm = wireframe.vm;

					vm.selectedArticle(value);
					vm.currentWorkingArticle(value);
					if(vm && vm.loaded()){

						value.properties().forEach(function (v, i, arr){
							if(v.name() == "author"){
								vm.authorsArray().forEach(function (va, ia, arra){
									if(va.identifier() == v.values()[0]){
										self.authorObj(va);
										self.author(va.name());
									}
								})
							}
							if(v.name() == 'mgnl:created'){
								console.log("created: ", v.values()[0]);

								self.created(moment(v.values()[0]).format("MM-DD-YY hh:mm A"));
							}
						})
						
						vm.loadArticle();	
					}
				},
				owner: this
			});

		},
		image_editor: function(data){
			var self = this;

			this.showInternalDiv = ko.observable(false);
		},
		photo_gallery: function(data){
			var self = this,
				tempArr = [];


			console.log("photo_gallery data: ", data);

			this.showInternalDiv = ko.observable(false);
			this.newMediaGalleryName = ko.observable();
			this.newMediaGalleryTitle = ko.observable();
			this.newMediaGalleryLead = ko.observable();
			this.selectorContainerWidth = ko.observable('240px');

			this.showPhotoGallerySelector = ko.observable(true);

			this.sidebar2IsOpen = ko.observable(false);

			this.showCreateNewMediaGallerySelector = ko.observable(false);

			this.changeSelectedMediaGallery = ko.computed({
				read: function(){
					return "nothing";
				},
				write: function(value){
					var vm = wireframe.vm;

					if(vm && vm.loaded()){
						vm.selectedMediaGallery(value);

					}
				}

			});

			

			data.sidebarMenu.forEach(function (v, i, arr){
				v.owner = self;
				tempArr.push(new ns.sidebarMenuTabModel(v));
			});

			this.sidebarMenu = ko.observableArray(tempArr);



			this.clickCreateNewMediaGallery = function(data, event){
				event.stopPropagation();
				self.showPhotoGallerySelector(false);
				self.showCreateNewMediaGallerySelector(true);

			}

			this.confirmCreateNewMediaGallery = function(data, event){
				var wd = wireframe.data,
					mediaGalleryObj = new wireframe.models.JCRNode({
						type: 'mgnl:mediagallery',
						name: self.newMediaGalleryName(),
						properties: [
							{
								name: 'title',
								type: 'String',
								values: [ self.newMediaGalleryTitle()]
							},
							{
								name: 'lead',
								type: 'String',
								values: [ self.newMediaGalleryLead()]
							}
						]
					});

				event.stopPropagation();
				self.showPhotoGallerySelector(true);
				self.showCreateNewMediaGallerySelector(false);

				wireframe.data.createMediaGallery(mediaGalleryObj, function (status, returnedData){
					
					if(status === 200){
						wd.getMediaGalleries(wireframe.vm.mediaGalleryArray);
					}
				});

			}

			this.cancelCreateNewMediaGallery = function(data, event){
				event.stopPropagation();
				self.showPhotoGallerySelector(true);
				self.showCreateNewMediaGallerySelector(false);
			}
		}

	}
	// Object that contains all the 
	ns.WorkspaceTemplates = {
		layout_editor: function(data){
			var self = this;
			this.testLayoutEditor = ko.observable('layoutEditor, man...');
		},
		info: function(data){
			var self = this;
			this.testInfo = ko.observable("info, man.");
		},
		article: function(data){
			var self = this;
			this.articleContent = ko.observable();
			this.articleTitle = ko.observable();
			this.articleLead = ko.observable();
		},
		image_editor: function(data){
			var self = this;

			this.overDropZone = ko.observable(false);

			this.showImageUpload = ko.observable(true);

			this.dropZoneDragOver = function(){
				console.log("dropZoneDragOver");

				if(!self. overDropZone()){
					self.overDropZone(true);
				}
			}

			this.dropZoneLeave = function(){
				console.log("dropZoneLeave");
				self.overDropZone(false);
			}

			this.dropInDropZone = function(dataObj, ev){
				console.log("you dropped something...");
				self.overDropZone(false);
				self.showImageUpload(false);

				// for now this is only going to work for one file, 
				// but we can add support for multiple files (folder) easily.
				var file = ev.originalEvent.dataTransfer.files[0],
					reader = new FileReader;

					reader.onload  = function(theFile){
						console.log("reader onload");
						croppy.createCroppyCanvas(theFile);
					};

					// reader.readAsBinaryString(file);
					// reader.readAsArrayBuffer(file);
					reader.readAsDataURL(file);

				

				//TODO: add type checking to make sure we are only handling img files
			}

			this.clickUploadFile = function(){
				console.log("clickUploadFile");
				$('#hidden-file-input').click();
			}
		},
		
		photo_gallery: function(data){
			var self = this;

			this.selectedGalleryView = ko.observable('list');


		
		}
	}

	ns.Sidebar2Templates = {

		layout_editor: function(data){
			var self = this;
		},
		info: function(data){
			var self = this;
		},
		article: function(data){
			var self = this,
				vm = wireframe.vm;
		},
		image_editor: function(data){
			var self = this;
		},
		photo_gallery: function(data){
			var self = this,
				vm = wireframe.vm;

			this.test = ko.observable("This is a test");

			this.mediaGalleries = ko.observableArray(["1", "2", "3"]);


		}

	}

	ns.ArticleModel = function(data){
		console.log("data: ", data);

		var self = this;

		this.identifier = ko.observable(data.identifier);
		this.name = ko.observable(data.name);
		this.path = ko.observable(data.path);
		this.type = ko.observable(data.type);
		this.properties = ko.observableArray([]);

		data.properties.forEach(function (v, i, arr){
			self.properties.push(new ns.ArticlePropertyModel(v));
		})
	}

	ns.ArticlePropertyModel = function(data){
		var self = this;

		this.name = ko.observable(data.name);
		this.type = ko.observable(data.type);
		this.values = ko.observableArray(data.values);
	}

	ns.AssetModel = function(data){
		var self = this;

		this.identifier = ko.observable(data.identifier);
		this.name = ko.observable(data.name);
		this.path = ko.observable(data.path);
		this.type = ko.observable(data.type);
		this.properties = ko.observableArray([]);

		data.properties.forEach(function (v, i, arr){
			self.properties.push(new ns.PropertyModel(v));
		});

	}

	ns.AuthorModel = function(data){
		var self = this;

		this.identifier = ko.observable(data.identifier);
		this.name =  ko.observable(data.name);
		this.path = ko.observable(data.path);
		this.properties = ko.observableArray([]);

		data.properties.forEach(function (v, i, arr){
			self.properties.push(new ns.PropertyModel(v));
		});
	}

	ns.PropertyModel = function(data){
		var self = this;

		this.name = ko.observable(data.name);
		this.type = ko.observable(data.type);
		this.values = ko.observableArray(data.values);
	}

	ns.MediaGalleryModel = function(data){
		var self = this;

		this.identifier = ko.observable(data.identifier);
		this.name =  ko.observable(data.name);
		this.path = ko.observable(data.path);
		this.properties = ko.observableArray([]);

		data.properties.forEach(function (v, i, arr){
			self.properties.push(new ns.PropertyModel(v));
		});	
	}

	ns.sidebarMenuTabModel = function(data){
		var self = this;

		this.name = ko.observable(data.name);
		this.bgdColor = ko.observable(data.bgdColor);
		this.clickTab = data.clickTab;
		this.owner = data.owner;
		this.mouseOver = data.mouseOver;
		this.mouseOut = data.mouseOut;

	}

	wireframe.models = ns;
})($, window, wireframe);