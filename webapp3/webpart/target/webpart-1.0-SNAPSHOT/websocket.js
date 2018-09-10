var ws;

document.querySelector('#connectButton').addEventListener("click", function (event) {
    var input = document.querySelector("#username");
    var username = input.value;
    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + pathname + "chat/" + username + "/");
    input.disabled = true;
    event.target.disabled = true;
    ws.onmessage = function (event) {
        var history = document.querySelector("#history");
        var message = JSON.parse(event.data);
        history.innerHTML += message.sender + ": " + message.content + "\n";
        if (message.content == '/exit') {
            document.querySelector("#sendButton").disabled = true;
        }
    };
})

document.querySelector('#sendButton').addEventListener("click", function () {
    var content = document.querySelector("#msg").value;
    var json = JSON.stringify({
        "content": content
    });
    ws.send(json);
})