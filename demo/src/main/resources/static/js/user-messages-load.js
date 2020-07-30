$(document).ready(function () {
    counter();
})

function counter() {
    $('#messages').empty();
    var username = $("#sendername").data('value');

    $.ajax({
        url: "http://localhost:8080/info/count/" + username, // A valid URL
        type: 'GET', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        success: function (data) {
            let message = '<p>Имате ' + data + ' непрочетени съобщения!</p>';
            $("#messages").append(message);
            var count = data;
            if (count !== '0') {
                getMessage(username);
            }
        }
    });
}

function getMessage(username) {
    fetch('http://localhost:8080/info/unreaded/' + username).then(responce => responce.json())
        .then((json) => json.forEach((msg, idx) => {

            let text = '<p>От: ' + msg.leftFrom + '.Оставено на:' + msg.leftOn + '</p>' +
                '<button class="message" data-id="' + msg.id + '">Прочети</button>';
            $('#messages').append(text);
        }));
};

$('body').on('click', 'button.message', function () {
    let num = $(this).data('id');
    $.ajax({
        url: "http://localhost:8080/info/single/" + num, // A valid URL
        type: 'GET', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        success: function (data) {
            let message = 'От: ' + data.leftFrom + '\n' + data.body + '\n' + 'Оставено на: ' + data.leftOn;
            alert(message);
            counter();
        },
        error: function (Xhr, textStatus, errorThrown) {
            alert(Xhr.responseText);
        }
    });
});


function send() {
    let message = document.getElementById("message").value;
    let sender = $("#sendername").data('value');
    let recipient = $("#recipientname").data('value');

    if (jQuery.isEmptyObject(message) | message.length < 10) {
        window.alert('Съобщението трябва да съдържа поне 10 символа!')
    } else {
        if (confirm("Да изпратя ли съобщението?")) {

            let dto = {receive: recipient, sendfrom: sender, mess: message}
            $.ajax({
                url: "http://localhost:8080/info/send", // A valid URL
                type: 'PUT', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
                data: JSON.stringify(dto),
                contentType: 'application/json; charset=utf-8',// Some data e.g. Valid JSON as a string
                success: function (data) {
                    alert(data);
                    $('#message').val('');
                },
                error: function (Xhr, textStatus, errorThrown) {
                    alert(Xhr.responseText);
                }
            });
        }
    }
}
