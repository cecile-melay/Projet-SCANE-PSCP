var moduleDb = require('./modules/oracle_db_module')
var parseJson = require('parse-json');
var express = require('express');
var app = express();
var cors = require('cors')
const PORT=1337;

app.use(cors())

app.listen(PORT, function () {
  console.log('SCANE server side app listening on port '+PORT);
});

//app.get permet d'ajouter une route, à reproduire autant de fois que l'on nécessite de requête différente (en Get)
app.get('/getZones', function (req, res) {
console.log("/getZones");

//ici j'appelle ma requête en lui passant une fonction en callback (function(err, result))
moduleDb.getZones(function(err, result)
  {
    if (err) {
      console.log('getZones error on dbQuery : ' + err);
      res.send('getZones error on dbQuery : ' + err);
    } else {
      console.log('success');
      res.send(result.rows);
    }
  })
});


//Insertion d'un point d'un chemin
app.get('/insertPoint/:lat/:lng/:posInPath/:associatePath', function (req, res) {
console.log("/insertPoint");

var lat = req.params.lat;
var lng = req.params.lng;
var posInPath = req.params.posInPath;
var associatePath = req.params.associatePath;
moduleDb.insertPoint(lat, lng, posInPath, associatePath, function(err, result) {
  if (err) {
      console.log('insertPoint error on dbQuery : ' + err);
      res.send('insertPoint error on dbQuery : ' + err);
    } else {
      console.log('success');
      res.send(result);
    }
});
});

//Insertion d'un utilisateur
app.get('/insertUser/:prenom/:nom/:dateNaiss/:lvlSport/:fc/:ville/:mail/:pass/:pseudo', function (req, res) {
console.log("/insertUser");

var prenom = req.params.prenom;
var nom = req.params.nom;
var dateNaiss = req.params.dateNaiss;
var lvlSport = req.params.lvlSport;
var ville = req.params.ville;
var mail = req.params.mail;
var pass = req.params.pass;
var fc = req.params.fc;
var pseudo = req.params.pseudo;

moduleDb.insertUser(prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, pseudo, function(err, result) {
  if (err) {
      console.log('insertUser error on dbQuery : ' + err);
      res.send('insertUser error on dbQuery : ' + err);
    } else {
      console.log('success');
      res.send(result);
    }
});
});

//Insertion d'un utilisateur
app.get('/updateUser/:id/:prenom/:nom/:dateNaiss/:lvlSport/:fc/:ville/:mail/:pass/:pseudo/:xp', function (req, res) {
console.log("/insertUser");

var id = req.params.id;
var prenom = req.params.prenom;
var nom = req.params.nom;
var dateNaiss = req.params.dateNaiss;
var lvlSport = req.params.lvlSport;
var ville = req.params.ville;
var mail = req.params.mail;
var pass = req.params.pass;
var fc = req.params.fc;
var pseudo = req.params.pseudo;
var xp = req.params.xp;

moduleDb.updateUser(id, prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, pseudo, xp, function(err, result) {
  if (err) {
      console.log('updateUser error on dbQuery : ' + err);
      res.send('updateUser error on dbQuery : ' + err);
    } else {
      console.log('success');
      res.send(result);
    }
});
});

//Insertion d'un chemin
app.get('/insertPath/:id/:name', function (req, res) {
console.log("/insertPath");

var id = req.params.id;
var name = req.params.name;

moduleDb.insertPath(id, name, function(err, result) {
  if (err) {
      console.log('insertPath error on dbQuery : ' + err);
      res.send('insertPath error on dbQuery : ' + err);
    } else {
      console.log('success');
      res.send(result);
    }
});
});

//Insertion d'un chemin
app.get('/login/:pseudo/:pass', function (req, res) {
console.log("/login");

var pseudo = req.params.pseudo;
var pass = req.params.pass;

moduleDb.login(pseudo, pass, function(err, result) {
  if (err) {
      console.log('login error on dbQuery : ' + err);
      res.send('login error on dbQuery : ' + err);
    } else {
      console.log('success');
      console.log(result);
      res.send(result);
    }
});
});

app.get('/getPath/:id', function (req, res) {
console.log("/getPath");

var id = req.params.id;

moduleDb.getPath(id, function(err, result) {
  if (err) {
      console.log('getPath error on dbQuery : ' + err);
      res.send('getPath error on dbQuery : ' + err);
    } else {
      console.log('success');
      console.log(result);
      res.send(result);
    }
});
});

app.get('/getPaths', function (req, res) {
console.log("/getPaths");

moduleDb.getPaths(function(err, result) {
  if (err) {
      console.log('getPaths error on dbQuery : ' + err);
      res.send('getPaths error on dbQuery : ' + err);
    } else {
      console.log('success');
      console.log(result);
      res.send(result);
    }
});
});

app.get('/getPathPoints/:id', function (req, res) {
console.log("/getPath");

var id = req.params.id;

moduleDb.getPathPoints(id, function(err, result) {
  if (err) {
      console.log('getPathPoints error on dbQuery : ' + err);
      res.send('getPathPoints error on dbQuery : ' + err);
    } else {
      console.log('success');
      console.log(result);
      res.send(result);
    }
});
});

app.get('/getPathPointsOfInterest/:id', function (req, res) {
console.log("/getPath");

var id = req.params.id;

moduleDb.getPathPointsOfInterest(id, function(err, result) {
  if (err) {
      console.log('getPathPointsOfInterest error on dbQuery : ' + err);
      res.send('getPathPointsOfInterest error on dbQuery : ' + err);
    } else {
      console.log('success');
      console.log(result);
      res.send(result);
    }
});
});