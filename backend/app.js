const express = require('express');
const {spawn} = require('child_process');
const app = express()
const port = 3000
app.get('/', (req,res) => {
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
app.listen(port, () => console.log(`Example app listening on port 
        ${port}!`))