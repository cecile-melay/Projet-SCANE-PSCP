angular.module('starter.controllers', ['ngCordova'])

.controller('DashCtrl', function($scope) {
})

.controller('ChatsCtrl', function($scope, Chats) {
  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  $scope.chats = Chats.all();
  $scope.remove = function(chat) {
    Chats.remove(chat);
  };
})

.controller('ChatDetailCtrl', function($scope, $stateParams, Chats) {
  $scope.chat = Chats.get($stateParams.chatId);
})

.controller('AccountCtrl', function($scope,$cordovaDeviceMotion) {
  $scope.settings = {
    enableFriends: true
  };
  //pedometer.isStepCountingAvailable(function(){alert("step counting is available")}, function(){alert("step counting is not available")});
  //pedometer.isDistanceAvailable(function(){alert("releve distance is available")}, function(){alert("releve distance is not available")});
  //pedometer.isFloorCountingAvailable(function(){alert("floor counting is available")}, function(){alert("floor counting is not available")});
  $scope.nbSteps = -1;
  var successHandler = function (pedometerData) {
    $scope.nbSteps++;
    $scope.$apply();
    // pedometerData.startDate; -> ms since 1970
    // pedometerData.endDate; -> ms since 1970
    // pedometerData.numberOfSteps;
    // pedometerData.distance;
    // pedometerData.floorsAscended;
    // pedometerData.floorsDescended;
  };
  var onError = function (pedometerData) {
    console.log("step didn't saved")
    // pedometerData.startDate; -> ms since 1970
    // pedometerData.endDate; -> ms since 1970
    // pedometerData.numberOfSteps;
    // pedometerData.distance;
    // pedometerData.floorsAscended;
    // pedometerData.floorsDescended;
  };
  pedometer.startPedometerUpdates(successHandler, onError);

  /*
  function onSuccess(acceleration) {
    alert('Acceleration X: ' + acceleration.x + '\n' +
          'Acceleration Y: ' + acceleration.y + '\n' +
          'Acceleration Z: ' + acceleration.z + '\n' +
          'Timestamp: '      + acceleration.timestamp + '\n');
  }

  function onError() {
      alert('onError!');
  }

  var options = { frequency: 10000 };  // Update every 3 seconds

  var watchID = navigator.accelerometer.watchAcceleration(onSuccess, onError, options);*/

});
