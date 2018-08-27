var ws;

function connectAgent() {
    var usersMap = new Map();

    var username = document.getElementById("username").value;
    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + pathname + "chat/" + username + "/");
    document.getElementById("username").disabled = true;
    document.getElementById("connectBtn").disabled = true;
    usersMap.set(username, "");
    ws.onmessage = function (event) {
        var log = document.getElementById("log");
        var message = JSON.parse(event.data);
        console.log(message);
        console.log(event.data);
        if (!usersMap.has(message.from)) {
            usersMap.set(message.from, message.from);
        }
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function connectClient() {
    var username = document.getElementById("username").value;
    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + pathname + "chat/" + username + "/");
    document.getElementById("username").disabled = true;
    document.getElementById("connectBtn").disabled = true;
    ws.onmessage = function (event) {
        var log = document.getElementById("log");
        var message = JSON.parse(event.data);
        console.log(message);
        console.log(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function sendMessageToClient() {
    var content = document.getElementById("msg").value;
    var username = document.getElementById("username").value;
    var json = JSON.stringify({
        "content": content
    });
    var log = document.getElementById("log");
    log.innerHTML += username + " : " + content + "\n";
    ws.send(json);
}

function sendMessageToAgent() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content": content
    });
    ws.send(json);
}