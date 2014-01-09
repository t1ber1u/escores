console.log("croppy.js");

(function ($, window, undefined){
	var ns = {};

	ns.init = function(canvasID){
		console.log("croppyInit");
	}

	ns.createCroppyCanvas = function(file){
		console.log("createCroppyCanvas");
		
		var canvas = document.getElementById('image-editor-canvas'),
			ctx = canvas.getContext('2d'),
			image = new Image();

			console.log(file.target.result);

			image.src = file.target.result;

			console.log(image.height);

			$("#image-editor-canvas").attr('height', image.height);
			$("#image-editor-canvas").attr('width', image.width);

			console.log($("#image-editor-canvas").attr('height'));
			console.log($("#image-editor-canvas").attr('width'));


			// $("#image-editor-canvas").height(image.height + 'px').width(image.width + "px");


			ctx.drawImage(image, 0, 0);

	}

	window.croppy = ns;
})($, window);