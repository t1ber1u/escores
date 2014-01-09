console.log("inContext2.js");

// API stuff 

(function ($, window, undefined, ns){
	ns = {};

	ns.testThis = function(){
		console.log("testing the api....");
	}

	ns.parseQueryString = function(){

	}

	ns.update = function(nodeObj){
		console.log("update");
		console.log(document.URL);

		var qString = ns.createQueryString(nodeObj);

		console.log(qString);

		console.log(nodeObj);

		ns.execute('POST', qString, nodeObj);
	}

	ns.createQueryString = function(paramObj){
		newURL = document.URL + "&ajax=true";

		return newURL;
	}

	ns.execute = function(method, url, data, callback){

		console.log("execute");

		$.ajax({
			url: url,
			data: data,
			type: method
		})
	}

	window.inContextAPI = ns;

})($, window);