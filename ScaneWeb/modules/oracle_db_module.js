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
	req = "INSERT INTO PATHPOINT (LAT, LNG, POSITIONINPATH, ASSOCIATEDPATH) VALUES (%d, %d, %d, %d)"
	req = util.format(req, lat, lng, posInPath, associatePath);
	console.log(req);

	myModule.connect(function(err, conn) {
  		if (err) {
          console.log('insertPoint error on dbConnexion');
        } else {
		      console.log(req);
		      myModule.execQuery(conn, req, callback);
		  }
  	});
}

myModule.insertUser = function(prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, callback) {
	req = "INSERT INTO USERSCANE (PRENOM, NOM, DATENAISS, SPORTLEVEL, VILLE, MAIL, PASS, FC) VALUES ('%s','%s', '%s', %d, '%s', '%s', '%s', %d)"
	req = util.format(req, prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc);
	console.log(req);

	myModule.connect(function(err, conn) {
  		if (err) {
          console.log('insertUser error on dbConnexion');
        } else {
		      console.log(req);
		      myModule.execQuery(conn, req, callback);
		  }
  	});
}



module.exports = myModule;