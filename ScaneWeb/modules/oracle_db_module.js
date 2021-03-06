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

myModule.getPath = function(id, callback) {
  var req = "SELECT * FROM PATH WHERE ID = %d";
  req = util.format(req, id);

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getPath error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
        }
    })
}

myModule.getPaths = function(callback) {
  var req = "SELECT * FROM PATH";

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getPaths error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
        }
    })
}

myModule.getPathPoints = function(id, callback) {
  var req = "SELECT * FROM PATHPOINT WHERE ASSOCIATEDPATH = %d";
  req = util.format(req, id);

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getPathPoints error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
        }
    })
}

myModule.getQCM = function(idPath, callback) {
  var req = "SELECT * FROM QCM WHERE IDPATH = %d";
  req = util.format(req, idPath);

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getQCM error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
        }
    })
}


myModule.getQuestions = function(idQcm, callback) {
  var req = "SELECT * FROM QUESTION WHERE IDQCM = %d";
  req = util.format(req, idQcm);

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getQuestions error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
        }
    })
}

myModule.getAnswers = function(idQuest, callback) {
  var req = "SELECT * FROM ANSWER WHERE IDQUESTION = %d";
  req = util.format(req, idQuest);

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getAnswers error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
        }
    })
}

myModule.getEpreuve = function(idPoi, callback) {
  var req = "SELECT * FROM EPREUVE WHERE IDPOI = %d";
  req = util.format(req, idPoi);

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getEpreuve error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
        }
    })
}

myModule.getPathPointsOfInterest = function(id, callback) {
  var req = "SELECT * FROM POINTSOFINTEREST INNER JOIN PATHPOINT ON POINTSOFINTEREST.ASSOCIATEDPATHPOINT = PATHPOINT.ID WHERE PATHPOINT.ASSOCIATEDPATH = %d";
  req = util.format(req, id);

    myModule.connect(function(err, conn) {
      if (err) {
          console.log('getPathPointsOfInterest error on dbConnexion');
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
		      myModule.execQuery(conn, req, callback);
		  }
  	});
}

myModule.insertUser = function(prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, pseudo, callback) {
	req = "INSERT INTO USERSCANE (PRENOM, NOM, DATENAISS, SPORTLEVEL, VILLE, MAIL, PASS, FC, PSEUDO) VALUES ('%s','%s', '%s', %d, '%s', '%s', '%s', %d, '%s')"
	req = util.format(req, prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, pseudo);
	console.log(req);

	myModule.connect(function(err, conn) {
  		if (err) {
          console.log('insertUser error on dbConnexion');
        } else {
		      myModule.execQuery(conn, req, callback);
		  }
  	});
}

myModule.updateUser = function(id, prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, pseudo, xp, callback) {
  req = "UPDATE USERSCANE SET PRENOM = '%s', NOM = '%s', DATENAISS = '%s', SPORTLEVEL = %d, VILLE = '%s', MAIL = '%s', PASS = '%s', FC = %d, XP = %d, PSEUDO = '%s' WHERE ID = %d"
  req = util.format(req, prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, xp, pseudo, id);
  console.log(req);

  myModule.connect(function(err, conn) {
      if (err) {
          console.log('updateUser error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
      }
    });
}

myModule.insertPath = function(id, name, callback) {
  req = "INSERT INTO PATH (ID, NAME) VALUES (%d,'%s')"
  req = util.format(req, id, name);
  console.log(req);

  myModule.connect(function(err, conn) {
      if (err) {
          console.log('insertPath error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
      }
    });
}

myModule.login = function(pseudo, pass, callback) {
  req = "SELECT * FROM USERSCANE WHERE PSEUDO = '%s' AND PASS = '%s'"
  req = util.format(req, pseudo, pass);
  console.log(req);

  myModule.connect(function(err, conn) {
      if (err) {
          console.log('login error on dbConnexion');
        } else {
          myModule.execQuery(conn, req, callback);
      }
    });
}



module.exports = myModule;