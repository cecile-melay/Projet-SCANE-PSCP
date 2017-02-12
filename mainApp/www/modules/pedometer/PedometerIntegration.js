PedometerIntegration = function () {
  //this.sensor = pedometer;
  this.numberOfSteps = 0;
  this.distance = 0;
  this.floorsAscended = 0;
  this.floorsDescended = 0;
  this.onError = errorHandler;
  this.onSuccess = successHandler;

  this.getHistory = function(startDate, endDate) {
    getHistory(startDate, endDate, this.OnSuccess, this.onError);
  };

  this.startListening = function() {
    pedometer.startPedometerUpdates(this.onSuccess, this.onError);
  };

  this.stopListening = function() {
    stop(
      new function () {},
      new function () {});
  };
}

start = function (onSuccess, onError) {
  pedometer.startPedometerUpdates(onSuccess, onError);
}

stop = function (successCallback, failureCallback) {
  pedometer.stopPedometerUpdates(successCallback, failureCallback);
}

getHistory = function(start, end, onSuccess, onError) {
  var options = {
    "startDate": start,
    "endDate": end
  };
  pedometer.queryData(onSuccess, onError, options);
}

var successHandler = function (pedometerData) {
  var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');
        var step_count = parentElement.querySelector('#step_count');
        var startDate = parentElement.querySelector('#startDate');
        var endDate = parentElement.querySelector('#endDate');
        var numberOfSteps = parentElement.querySelector('#numberOfSteps');
        var distance = parentElement.querySelector('#distance');
        var floorsAscended = parentElement.querySelector('#floorsAscended');
        var floorsDescended = parentElement.querySelector('#floorsDescended');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');
        step_count.innerHTML="0";

        var successHandler = function (pedometerData) {
            // pedometerData.startDate; -> ms since 1970
            // pedometerData.endDate; -> ms since 1970
            // pedometerData.numberOfSteps;
            // pedometerData.distance;
            // pedometerData.floorsAscended;
            // pedometerData.floorsDescended;
            step_count.innerHTML = "success to listen from podometer";
            startDate.innerHTML = "startDate : " + pedometerData.startDate;
            endDate.innerHTML = "endDate : " + pedometerData.endDate;
            numberOfSteps.innerHTML = "numberOfSteps : " + pedometerData.numberOfSteps;
            distance.innerHTML = "distance : " + pedometerData.distance;
            floorsAscended.innerHTML = "floorsAscended : " + pedometerData.floorsAscended;
            floorsDescended.innerHTML = "floorsDescended : " + pedometerData.floorsDescended;
            }
}

var errorHandler = function (pedometerData) {
  //Manage error here
}