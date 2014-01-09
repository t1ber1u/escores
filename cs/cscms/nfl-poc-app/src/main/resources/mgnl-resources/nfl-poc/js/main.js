console.log("main.js");

window.onload = function(){

	croppy.init();

	wireframe.vm.init();

	
    // Aloha.jQuery(function(){
    // 	Aloha.jQuery('.article-editable').aloha();
    // });

}

// handler for saving changes
Aloha.ready(function() {
	Aloha.require( ['aloha', 'aloha/jquery'], function( Aloha, jQuery) {
		
		// save all changes after leaving an editable
		Aloha.bind('aloha-editable-deactivated', function(){
			var content = Aloha.activeEditable.getContents(),
				contentId = Aloha.activeEditable.obj[0].id,
				pageId = window.location.pathname,
				propname = $(Aloha.activeEditable.obj[0]).attr('data-aloha-propname');
				
			
			console.log("aloha-editable-deactivated");
			// console.log(Aloha.activeEditable.obj[0]);
			// console.log($(Aloha.activeEditable.obj[0]).attr('data-aloha-propname'));


			console.log(ko.toJS(wireframe.vm.modesArray()));

			wireframe.data.updateArticleProperty(propname, content);
		});
	});
});