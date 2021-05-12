var express = require("express");
var app = express();
const {spawn} = require('child_process');
var multer, storage, path, crypto;
multer = require('multer')
path = require('path');
crypto = require('crypto');

var form = "<!DOCTYPE HTML><html><body>" +
"<form method='post' action='/upload' enctype='multipart/form-data'>" +
"<input type='file' name='upload'/>" +
"<input type='submit' /></form>" +
"</body></html>";

app.get('/', function (req, res){
  res.writeHead(200, {'Content-Type': 'text/html' });
  res.end(form);

});

// Include the node file module
var fs = require('fs');

storage = multer.diskStorage({
    destination: './uploads/',
    filename: function(req, file, cb) {
      return crypto.pseudoRandomBytes(16, function(err, raw) {
        if (err) {
          return cb(err);
        }
        return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
      });
    }
  });


// Post files
app.post(
  "/upload",
  multer({
    storage: storage
  }).single('upload'), function(req, res) {
    console.log(req.file);
    console.log(req.body);
    res.redirect("/uploads/" + req.file.filename);
    console.log(req.file.filename);
    return res.status(200).end();
  });

app.get('/uploads/:upload', function (req, res){
  file = req.params.upload;
  console.log(req.params.upload);
  var img = fs.readFileSync(__dirname + "/uploads/" + file);
  res.writeHead(200, {'Content-Type': 'image/png' });
  res.end(img, 'binary');

});

app.get('/compute/',function(req,res){
    const python = spawn('python', ['./controllers/game_recognition_model/integrated.py']);
    var dataToSend;
    python.stdout.on('data', function (data) {
        console.log('Pipe data from python script ...');
        //console.log(data)
        dataToSend = data.toString();
       });
       python.stderr.on('data', function (data) {
        console.log('stderr: ' + data);
      });  
       python.on('close', (code) => {
        console.log(`child process close all stdio with code ${code}`);
        // send data to browser
        res.send(dataToSend)
        });
});

app.get('/clear/',function(req,res){
  const python = spawn('python', ['./controllers/game_recognition_model/clear.py']);
  var dataToSend;
  python.stdout.on('data', function (data) {
      console.log('Pipe data from python script ...');
      //console.log(data)
      dataToSend = data.toString();
     });
     python.stderr.on('data', function (data) {
      console.log('stderr: ' + data);
    });  
     python.on('close', (code) => {
      console.log(`child process close all stdio with code ${code}`);
      // send data to browser
      res.send(dataToSend)
      });
});


app.listen(3000);