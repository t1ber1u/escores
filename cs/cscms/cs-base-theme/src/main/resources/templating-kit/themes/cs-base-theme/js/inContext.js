console.log("inContext.js");

/* incontext editing */

(function ($, window, CKE, undefined, ns){

	var ns = {},
		API = window.inContextAPI,
		cke = CKE;

		ns.buttonEventListeners = {
			save: function(ev){
				var toolbar = $(ev.target).parent(),
					originalDiv = $(toolbar).data("originalDiv"),
					inputArea = $(toolbar).data("inputArea"),
					openEditor = $(document).data("openEditor"),
					editorInstance = cke.instances[openEditor.split(' ')[1]],
					partToUpdate = $(document).data("editedField"),
					obj = {};

				console.log(editorInstance.getData());

				ns.hideToolbar(toolbar);
				obj[partToUpdate] = editorInstance.getData();
				API.update(obj);
			},
			cancel: function(ev){
				var toolbar = $(ev.target).parent();
				ns.hideToolbar(toolbar);
			}
		}
		// event handlers for edit menu
		ns.clickEnableEditMode = function(){
			console.log("ns.clickEnableEditMode");
			ns.addEditors();
			ns.toggleEditModeButtons();
		}

		ns.clickCancelEditMode = function(){
			console.log("clickCancelEditMode");
			ns.removeEditors();
			ns.toggleEditModeButtons();
		}

		ns.clickSaveEditMode = function(ev){
			console.log("clickSaveEditMode");

			var toolbar = $(ev.target).parent(),
				obj = {};

				$('[contenteditable]').each(function (v){
					console.log(this);
					var editorName = $(this).attr('title').split(', ')[1],
						fieldName = $(this).attr('data-incontextfield');

					obj[fieldName] = cke.instances[editorName].getData();
				});

				// for(var key in cke.instances){
				// 	obj[key] = cke.instances[key].getData();
				// }

				console.log(obj);


				API.update(obj);

				ns.removeEditors();
				ns.toggleEditModeButtons();
		}

		ns.toggleEditModeButtons = function(){
			$('.enableEditMode').toggleClass('hide');
			$('.editModeCancelButton').toggleClass('hide');
			$('.editModeSaveButton').toggleClass('hide');
		}

		// node generation. 
		ns.EditModeMenuNode = function(){
			var newDivNode = document.createElement('div');
				$node = $(newDivNode);

			$node.addClass('editModeMenu');
			$node.append('<div class="enableEditMode button">Enable Edit Mode</div>');
			$node.append('<div class="editModeCancelButton button small hide">Exit</div>');
			$node.append('<span class="editModeSaveButton button small hide">Save</span>');

			return newDivNode;
		}

		ns.ToolbarNode = function(){
			var toolbarNode = document.createElement('div');
			$(toolbarNode).append('<span class="save button tiny" data-incontextmenubutton="save">Save</span>');
			$(toolbarNode).append('<span class="cancel button tiny" data-incontextmenubutton="cancel">Cancel</span>');
			$(toolbarNode).addClass("inContextToolbar");

			ns.addEventListenersToToolbarButtons(toolbarNode);


			return toolbarNode;
		}

		ns.focusOnContentEditable = function(ev){
			var t = ev.target,
				$t = $(ev.target);

			// $t.on("focusout", ns.editableAreaFocusOutHandler);

		}

		ns.editableAreaFocusOutHandler = function($ev){
			console.log("editableAreaFocusOutHandler");
			var $t = $(document).data("currentFocus");
				toolbar = $(document).data("currentToolbar");

			$t.off("focusout", ns.editableAreaFocusOutHandler);

			$(document).data("currentFocus", undefined);

			window.setTimeout(function(){
				console.log("timeout");
				ns.hideToolbar(toolbar);
			}, 150);
		}

		ns.addEventListenersToToolbarButtons = function(toolbar){
			console.log("addEventListenersToToolbarButtons", toolbar);

			var toolbarChildren = $(toolbar).children();

			toolbarChildren.each(function(){
				var buttonType = $(this).attr('data-incontextmenubutton');
				if(buttonType){
					$(this).on('click', ns.buttonEventListeners[buttonType]);
				}
			});
		}

		ns.displayEditModeToolbar = function(){
			console.log("displayEditModeToolbar");

			$(document.body).append(ns.ToolbarNode());
		}

		ns.saveTextEdits = function(){
			// interate through all edits and save those that have been changed. 
			console.log("saveTextEdits");
			var saveObj = {},
				fieldName,
				content;

			$(".inContext").each(function (v){
				console.log(this);
				fieldName = $(this).attr("data-incontextfield");
				content = $(this).html();

				console.log(fieldName, " : ", content);

				saveObj[fieldName] = content;
			});

			API.update(saveObj);
		}

		ns.removeEditors = function(){
			for(var key in cke.instances){
				var instance = cke.instances[key];

				instance.destroy();
			};

			$('[contenteditable]').each(function (v){
				$(this).attr('contenteditable', 'false');
			});
		}

		ns.addEditors = function(){
			$('[contenteditable]').each(function (v){
				$(this).attr('contenteditable', 'true');
				cke.inline(this);
			});	
		}

		ns.init = function(){
			console.log("init");
			var $els = $(".inContext"),
				cssArray = [],
				newEditModeMenuNode = ns.EditModeMenuNode();

			console.log(document.URL);

			$els.each(function (v){
				$(this).on('focus', ns.focusOnContentEditable);
			});

			// add edit mode fixed toolbar
			// $(newEditModeMenuNode).on('click', ns.saveTextEdits);
			$(document.body).append(newEditModeMenuNode);
			$(".enableEditMode").on('click', ns.clickEnableEditMode);
			$(".editModeCancelButton").on('click', ns.clickCancelEditMode);
			$(".editModeSaveButton").on('click', ns.clickSaveEditMode);

			console.log($("[contenteditable]"));

			console.log(cke.instances);

			ns.removeEditors();
		}


	window.inContext = ns;
})($, window, window.CKEDITOR);


