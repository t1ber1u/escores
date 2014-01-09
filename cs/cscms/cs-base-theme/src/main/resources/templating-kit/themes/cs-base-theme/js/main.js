console.log("main.js");

// require(['lib/aura'], function(Aura) {
//   Aura({debug: { enable: false }})
//     .use('vendor/plugins')
//     .start({ components: 'body' }).then(function() {
//       console.warn('Aura started...');
//     });
// });

$(document).foundation();

window.onload = function(){
	console.log("onload");

	inContext.init();



};