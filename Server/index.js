const express = require("express");
const app = express();
const port = 3000;
const morgan = require("morgan");
const http = require("http");
const ws = require("ws");
const { Console } = require("console");
const server = http.createServer(app);
const wss = new ws.Server({ server });
app.use(morgan("combined"));

const map = new Map();

wss.on('connection', function connection(wss) {
  console.log("New connection")
  wss.send("Successfully")
  wss.on('message', function message(data) {
    const json = JSON.parse(data)
    console.log(json);
    if (json.id) {
      if (map.has(json.id)) {
        console.log("Da co id " + json.id + " roi")
      }
      else {
        map.set(json.id, wss)
        console.log("Da them " + json.id)
      }
    }
    else {
      const client = map.get("2")
      if (client) {
        switch (json.move) {
          case "f":
            client.send("f")
            break;
          case "d":
            client.send("d")
            break;
          case "r":
            client.send("r")
            break;
          case "l":
            client.send("l")
            break;
        }
      }
      else
        console.log("Khong co 2")
    }
  })
  wss.on('close', function close(code, reason) {
    try {
      const data = JSON.parse(reason);
      if (data.id) {
        console.log("Disconnected: " + data.id);
        map.delete(data.id);
      }
    } catch (e) {
      console.log("Cannot parse close reason or missing ID.");
    }
  });
})

wss.on('error', function error() {
  console.log("error")
})
app.get("/", (req, res) => {
  res.send("Hello World!");
});

server.listen(port, '0.0.0.0', () => {
  console.log(`Example app listening on port ${port}`);
});
