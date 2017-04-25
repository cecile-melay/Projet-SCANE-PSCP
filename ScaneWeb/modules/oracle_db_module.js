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
myModule.execQuery = function (conn, req, callback) {
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
          myModule.execQuery(conn, req, callback);
        }
  	})
}

//exemple pour guillaume
myModule.insertPoint(lat, long, id) {
	//fake req à corriger suviant le schéma de données
	req = "Insert into Points (lat, long, id) values (" + lat;
	req += " , " + long;
	req += " , " + id + ")";
	console.log(req);

	myModule.connect(function(err, conn) {
  		if (err) {
          console.log('insertZone error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, function(err, result) {
          	if(err) {
          		console.log('insertZone error on insert')
          	} else {
          		console.log('insertZone success');
          	}
          });
        }
  	})
}

module.exports = myModule;