var ws;
var panel;


function connectAgent() {
    console.log('????');
    var usersMap = new Map();

    var username = document.getElementById("username").value;
    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + pathname + "chat/" + username + "/");
    var millisecondsToWait = 100;
    setTimeout(function () {
        sendSlots();
    }, millisecondsToWait);
    alert("connected to the system");
    document.getElementById("username").disabled = true;
    document.getElementById("connectBtn").disabled = true;
    usersMap.set(username, "");
    ws.onmessage = function (event) {
        var message = JSON.parse(event.data);
        console.log(!usersMap.has(message.from));
        if (!usersMap.has(message.from)) {
            usersMap.set(message.from, message.from);
            console.log('!!!!!');
        }

        if ("Disconnected!" == message.content) {
            usersMap.delete(message.from);
            removeTab("" + message.from);
        }
        var log = document.getElementById("ta" + message.from);
        log.innerHTML += message.from + " : " + message.content + "\n";

    };
}

function sendSlots() {
    var content = document.getElementById("slots").value;
    var json = JSON.stringify({
        "content": content
    });
    ws.send(json);
}

function leave() {
    var content = "/leave";
    var json = JSON.stringify({
        "content": content
    });
    ws.send(json);
}

function sendA(to) {
    var content = document.getElementById("msg" + to).value;
    var username = document.getElementById("username").value;
    var json = JSON.stringify({
        "to": to + "",
        "content": content
    });
    var log = document.getElementById("ta" + to);
    log.innerHTML += username + " : " + content + "\n";

    ws.send(json);
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content": content
    });
    ws.send(json);
}

function removeTab(name) {
    var panel1 = document.getElementById("accordion");
    var child = document.getElementById("pandef" + name);
    panel1.removeChild(child);
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
        log.innerHTML += message.from + " : " + message.content + "\n";

    };
}