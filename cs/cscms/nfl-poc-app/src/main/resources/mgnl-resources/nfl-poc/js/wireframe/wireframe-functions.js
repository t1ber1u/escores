console.log("wireframe-functions.js");

(function ($, window, wireframe, undefined){
	var ns = {};

	ns.generateModeSelectorArray = function(modes){
		console.log("generateModeSelectorArray");

		var modesArray = [];

		modes.forEach(function (v, i, arr){
			var obj = new wireframe.models.SelectorModel(v);

			obj.selectorData = new wireframe.models.selectorTemplates[v.name](v);

			obj.data = new wireframe.models.WorkspaceTemplates[v.name]();

			obj.sidebar2Data = new wireframe.models.Sidebar2Templates[v.name]();

			modesArray.push(obj);
		});

		console.log("modesArray", modesArray);

		return modesArray;
	}

	ns.enableAloha = function(element, timeout){
		window.setTimeout(function(){
			Aloha.jQuery(function(){
		    	Aloha.jQuery(element).aloha();
		    });	
		}, timeout || 1);
	}

	ns.disableAloha = function(element, timeout){
		window.setTimeout(function(){
			Aloha.jQuery(function(){
		    	Aloha.jQuery(element).mahalo();
		    });	
		}, timeout || 1);
	}

	ns.mouseOverSidebarMenuTab = function(data, event){
		console.log("mouseOverSidebarMenuTab");
	}

	ns.mouseOutSidebarMenuTab = function(data, event){
		console.log("mouseOutSidebarMenuTab");
	}

	ns.toggleSidebar2 = function(){
		var vm = wireframe.vm;

		if(vm.showSidebar2()){
			vm.showSidebar2(false);	
		} else {
			vm.showSidebar2(true);	
		}
	}

	wireframe.functions = ns;
})($, window, wireframe)