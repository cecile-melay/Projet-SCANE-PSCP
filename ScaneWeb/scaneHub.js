var moduleDb = require('./modules/oracle_db_module')
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

//Insertion d'un point d'un chemin
app.get('/insertUser/:prenom/:nom/:dateNaiss/:lvlSport/:fc/:ville/:mail/:pass', function (req, res) {
console.log("/insertUser");

var prenom = req.params.prenom;
var nom = req.params.nom;
var dateNaiss = req.params.dateNaiss;
var lvlSport = req.params.lvlSport;
var ville = req.params.ville;
var mail = req.params.mail;
var pass = req.params.pass;
var fc = req.params.fc;
moduleDb.insertUser(prenom, nom, dateNaiss, lvlSport, ville, mail, pass, fc, function(err, result) {
  if (err) {
      console.log('insertUser error on dbQuery : ' + err);
      res.send('insertUser error on dbQuery : ' + err);
    } else {
      console.log('success');
      res.send(result);
    }
});
});