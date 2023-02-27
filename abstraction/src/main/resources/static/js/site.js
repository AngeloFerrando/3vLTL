// Delay loading any function until the html dom has loaded. All functions are
// defined in this top level function to ensure private scope.
jQuery(document).ready(function () {
  // Installs error handling.
  jQuery.ajaxSetup({
  error: function(resp, e) {
    if (resp.status == 0){
      alert('You are offline!!\n Please Check Your Network.');
      } else if (resp.status == 404){
        alert('Requested URL not found.');
      } else if (resp.status == 500){
        alert('Internel Server Error:\n\t' + resp.responseText);
      } else if (e == 'parsererror') {
        alert('Error.\nParsing JSON Request failed.');
      } else if (e == 'timeout') {
        alert('Request timeout.');
      } else {
        alert('Unknown Error.\n' + resp.responseText);
      }
    }
  });  // error:function()


  var generate_btn = jQuery('#generate_btn');
  var svg_div = jQuery('#graphviz_svg_div');
  var graphviz_data_textarea = jQuery('#graphviz_data');

  function UpdateGraphviz() {
	svg_div.html("");
    var data = graphviz_data_textarea.val();
    // Generate the Visualization of the Graph into "svg".
    var svg = Viz(data, "svg");
    svg_div.html(svg);
  }

  // Startup function: call UpdateGraphviz
  jQuery(function() {
	// The buttons are disabled, enable them now that this script
	// has loaded.
    generate_btn.removeAttr("disabled")
                .text("Generate Original Model Graph");
  });

  // Bind actions to form buttons.
  generate_btn.click(UpdateGraphviz);
  
  

  // Must
  
  var must_generate_btn = jQuery('#must_generate_btn');
  var must_svg_div = jQuery('#must_graphviz_svg_div');
  var must_graphviz_data_textarea = jQuery('#must_graphviz_data');

  function MustUpdateGraphviz() {
	must_svg_div.html("");
    var data = must_graphviz_data_textarea.val();
    // Generate the Visualization of the Graph into "svg".
    var svg = Viz(data, "svg");
    must_svg_div.html(svg);
  }

  // Startup function: call UpdateGraphviz
  jQuery(function() {
	// The buttons are disabled, enable them now that this script
	// has loaded.
    must_generate_btn.removeAttr("disabled")
                .text("Generate Must Model Graph");
  });

  // Bind actions to form buttons.
  must_generate_btn.click(MustUpdateGraphviz);

  
  // May	
  var may_generate_btn = jQuery('#may_generate_btn');
  var may_svg_div = jQuery('#may_graphviz_svg_div');
  var may_graphviz_data_textarea = jQuery('#may_graphviz_data');

  function MayUpdateGraphviz() {
	may_svg_div.html("");
    var data = may_graphviz_data_textarea.val();
    // Generate the Visualization of the Graph into "svg".
    var svg = Viz(data, "svg");
    may_svg_div.html(svg);
  }

  // Startup function: call UpdateGraphviz
  jQuery(function() {
	// The buttons are disabled, enable them now that this script
	// has loaded.
    may_generate_btn.removeAttr("disabled")
                .text("Generate May Model Graph");
  });

  // Bind actions to form buttons.
  may_generate_btn.click(MayUpdateGraphviz);


});
