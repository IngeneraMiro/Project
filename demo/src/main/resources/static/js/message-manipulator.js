$(document).ready(function () {
    counter();
})

function counter() {
    $('#doctor4').empty();
    var username = $("#username").data('value');

    $.ajax({
        url: "http://localhost:8080/info/count/" + username, // A valid URL
        type: 'GET', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        success: function (data) {
            let message = '<p>Имате ' + data + ' непрочетени съобщения!</p>';
            $("#doctor4").append(message);
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

            let text =
                '<button class="message" data-id="'+msg.id+'"><p>От:'+msg.leftFrom+'</p><p>Оставено на:'+msg.leftOn+'</p></button>';
            $('#doctor4').append(text);
        }));
};

$('body').on('click','button.message',function () {
    let num = $(this).data('id');
    $.ajax({
        url: "http://localhost:8080/info/single/" + num, // A valid URL
        type: 'GET', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        success: function (data) {
            let message = 'От: '+ data.leftFrom +'\n' + data.body +'\n'+ 'Оставено на: ' + data.leftOn;
            alert(message);
            counter();
        },
        error: function( Xhr, textStatus, errorThrown ){
            alert( Xhr.responseText);
        }
    });
});

function send() {
    let name = document.getElementById("firstName").value;
    let surname = document.getElementById("middleName").value;
    let lastname = document.getElementById("lastName").value;
    let message = document.getElementById("message").value;
    let sender = $("#username").data('value');

    if(jQuery.isEmptyObject(message) || jQuery.isEmptyObject(surname) || jQuery.isEmptyObject(name) || jQuery.isEmptyObject(lastname)){
        window.alert('Попълнете всички полета!')
    }else {
        if (confirm("Да изпратя ли съобщението?")) {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            let dto = {fname: name, sname: surname, tname: lastname, sendfrom: sender, mess: message}
            $.ajax({
                url: "http://localhost:8080/info/new", // A valid URL
                type: 'PUT', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
                data: JSON.stringify(dto),
                contentType: 'application/json; charset=utf-8',// Some data e.g. Valid JSON as a string
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (data) {
                    alert(data);
                    $('#firstName').val('');
                    $('#middleName').val('');
                    $('#lastName').val('');
                    $('#message').val('');
                },
                error: function (Xhr, textStatus, errorThrown) {
                    alert(Xhr.responseText);
                }
            });
        }
    }
}