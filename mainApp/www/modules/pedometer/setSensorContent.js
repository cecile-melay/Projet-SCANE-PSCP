$( document ).ready(function() {
	$.get("/modules/pedometer/view.html", function(data){
    $('#sensorTestContent').html(data);
});
});

$.getScript( "/modules/pedometer/PedometerIntegration.js" )
  .done(function( script, textStatus ) {
    var pedometer = new PedometerIntegration();
    pedometer.startListening();
  })
  .fail(function( jqxhr, settings, exception ) {
    alert( "Triggered ajaxError handler." );
});