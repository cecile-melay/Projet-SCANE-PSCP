var myModule = new Object();
var oracledb = require('oracledb');
var util = require('util');

//fonction de connexion a utiliser sur chaque requete
myModule.connect = function(callback) {
	oracledb.autoCommit = true;
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
myModule.insertPoint = function(lat, lng, posInPath, associatePath, callback) {
	reqGetLastId = "SELECT * FROM (SELECT ID FROM PATHPOINTS ORDER BY ID DESC) WHERE ROWNUM = 1";
	req = "INSERT INTO PATHPOINTS (ID, LAT, LNG, POSITIONINPATH, ASSOCIATEDPATH) VALUES (%d, %d, %d, %d, %d)"
	console.log(req);

	myModule.connect(function(err, conn) {
  		if (err) {
          console.log('insertPoint error on dbConnexion');
        } else {
        	conn.execute(reqGetLastId, function(err, result)
		      {
		      	//On récupère le dernier Id qu'on incrémente afin d'assurer un Id unique
		      	var lastId = 0;
		      	if(err) {
		      		console.log("insertPoint getLastId error : " + err);
		      	} else {
		      		lastId = result.rows[0];

		      		//test si c'est Nan pour prévoir le cas où la base n'a aucun point
		      		if(isNaN(lastId)) {
		      			lastId = 0;
		      		}
		      	}
		      	var id = Number(lastId) + 1;

		      	req = util.format(req, id, lat, lng, posInPath, associatePath);
		      	console.log(req);
		      	myModule.execQuery(conn, req, callback);
		      });
        }
  	})
}

module.exports = myModule;