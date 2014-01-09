console.log("wireframe-data.js");

(function ($, window, wireframe, undefined){
	var ns = {},
		api =wireframe.api;

	ns.getCatagories = function(){

		api.list("category/teams",{depth: 2} , function(stuff){
			console.log(stuff);
		});
	}

	ns.getArticles = function(callback){

		api.repoList("articles", "mgnl:article", function (status, data){
			var tempArr  = [];

			console.log(data);

			data.forEach(function (v, i, arr){
				tempArr.push(new wireframe.models.ArticleModel(v));
			});
			console.log(ko.toJS(tempArr));
			callback(tempArr);
		});
	}

	ns.updateArticleProperty = function(propName, propValue){
		var articleName = wireframe.vm.currentWorkingArticle().name(),
			paramObj = {
				value: propValue,
				type: "String",
				multiple: false
			};

		api.updateProperty('articles', articleName, propName, paramObj, function (status, data){
			console.log("status: ", status);
			console.log("data: ", data);
		})
	}

	ns.getDAM = function(callback){

		api.repoList("dam", "mgnl:asset", function (status, data){
			var tempArr = [];
			
			data.forEach(function (v, i, arr){
				tempArr.push(new wireframe.models.AssetModel(v));
			});
			callback(status, tempArr);
		})
	}

	ns.getMediaGalleries = function(callback){
		console.log("getMediaGalleries");

		api.repoList('mediagallery', 'mgnl:mediagallery', function (status, data){

			var tempArr = [];

			data.forEach(function (v, i, arr){
				tempArr.push(new wireframe.models.MediaGalleryModel(v))
			});
			console.log("mediagallery: ", ko.toJS(tempArr));
			callback(tempArr);
		});
	}

	ns.createMediaGallery = function(jcrNode, callback){
		console.log("createMediaGallery");
		console.log("jcrNode", jcrNode);
		api.create('mediagallery', jcrNode, callback);
	}

	ns.getAuthors = function(callback){

		api.repoList('authors', 'mgnl:author', function (status, data){
			var tempArr = [];

			data.forEach(function (v, i, arr){
				tempArr.push(new wireframe.models.AuthorModel(v))
			});

			callback(tempArr);
		})
	}

	ns.testSetProperty = function(){
		console.log("testSetProperty");

		// api.updateProperty()
	}



	wireframe.data = ns;
})($, window, wireframe)

/*
http://localhost:8080/magnoliaAuthor/.rest/properties/v1/articles/another-test-article?name=body&value=Hello%20there%20Broch&type=String&multiple=false
http://divisionspace.com/NFL/oneweb/CMS/ACQUIA/20131219/#p=photo_gallery_editor
*/