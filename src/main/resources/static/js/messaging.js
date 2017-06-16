(function () {
    window.onload = function () {
        subscribeToNewMessages();
        addCustomFormSubmission();
    };

    function subscribeToNewMessages(){
        var eventSource = new EventSource("/newMessages");
        var messagesDiv = document.getElementById("messages");
        eventSource.onmessage = function(e) {
            var paragraph = document.createElement("p");
            var node = document.createTextNode(e.data);
            paragraph.appendChild(node);
            messagesDiv.appendChild(paragraph);
        };
        eventSource.onerror = function (e) {
            console.log(e);
        }
    }

    function addCustomFormSubmission() {
        var messageForm = document.getElementById("messageForm");
        messageForm.addEventListener("submit", function (e) {
            e.preventDefault();
            var text = document.getElementById("message").value;
            var data = {text: text};
            $.ajax({
                data: data,
                url: "/send",
                type: "POST"
            });
        });
    }

})();




