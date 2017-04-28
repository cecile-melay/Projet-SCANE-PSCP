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
      console.log('getZones error on dbQuery');
    } else {
       console.log('success');
      res.send(result.rows);
    }
  })
});