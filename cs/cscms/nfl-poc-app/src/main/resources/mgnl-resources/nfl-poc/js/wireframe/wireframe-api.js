console.log("wireframe-api.js");


(function ($, window, wireframe, undefined){
	var ns = {},
		//TODO: Get rid of these hardcoded endpoints.
		endpoint = "http://localhost:8080/magnoliaAuthor/.rest/repo/v1/",
		propEndpoint = "http://localhost:8080/magnoliaAuthor/.rest/properties/v1/",
		putEndpoint = "http://localhost:8080/magnoliaAuthor/.rest/nodes/v1/";

	var parseParams = function(paramObj){
		var paramString = paramObj ? '?' : '';

		for(var key in paramObj){
			paramString = paramString == "?" ? paramString : paramString + '&';
			paramString += key + '=' + paramObj[key];
		}
		return paramString;
	}

	var execute = function(url, method, data, successCallback, errorCallback){
		
		if(typeof data != 'string'){
			data = JSON.stringify(data);
		}

		return $.ajax({
			type: method,
			Accept: 'application/json',
			data: data,
			url: url,
			contentType: 'application/json',
			success: successCallback,
			error: errorCallback
		});
	}

	ns.create = function(model, data, callback){
		if(model === 'mediagallery'){model = 'mediagallery/untitled'};


		var url = putEndpoint + model;

		execute(url, 'PUT', data, 
			function (res, status, req){
				callback(req.status, res);
			},
			function (req, status, err){
				callback(req.status, err);
			});
	}

	ns.list = function(model, paramObj, callback){

		var params = parseParams(paramObj);
			url = 'localhost:8080/magnoliaAuthor/.rest/nodes/v1/' + model + params;
	}

	ns.repoList = function(model, dataType, callback){
		var url = endpoint + model + "/" + dataType;

		execute(url, "GET", "", 
			function (res, status, req){
				callback(req.status, res);
			},
			function (req, status, err){
				callback(req.status, err);
			}
		);
	}

	ns.show = function(){

	}

	ns.update = function(){

	}

	ns.destroy = function(){

	}

	ns.updateProperty = function(model, path, property, paramObj, callback){
		var params = parseParams(paramObj),
			url = propEndpoint + model + "/" + path + "/" + property + params;
		console.log("params: ",params);
		console.log("url", url);

		execute(url, "POST", '', 
			function (res, status, req){
				callback(req.status, res);
			}, 
			function (req, status, err){
				callback(req.status, err);
			}
		);
	}

	ns.testExecute = function(){
		execute();
	}

	wireframe.api = ns;
})($, window, wireframe)

