var myModule = new Object();
var oracledb = require('oracledb');

//fonction de connexion a utiliser sur chaque requete
myModule.connect = function(callback) {
    oracledb.getConnection(
      {
        user          : "scane",
        password      : "i77^45GqK-*xN",
        connectString : "localhost/XE"
      },
      callback);
  };

//fonction de fermeture de la connexion a réutiliser après chaque requête
myModule.dorelease = function(conn) {
    conn.close(function (err) {
      if (err)
        console.error(err.message);
    });
  };

//fonction executant une requête, a réutiliser pour chaque requête 
myModule.execZoneQuery = function (conn, req, callback) {
    conn.execute(req, function(err, result)
      {
      	myModule.dorelease(conn);
      	callback(err, result);
      });
};

//exemple de méthode pour une requête, à reproduire pour chaque différente requête (appelée dans une route définire dans index.js)
myModule.getZones = function(callback) {
	var req = "SELECT * FROM POINTSOFINTEREST ";

  	myModule.connect(function(err, conn) {
  		if (err) {
          console.log('getZones error on dbConnexion');
        } else {
          myModule.execZoneQuery(conn, req, callback);
        }
  	})
}

module.exports = myModule;