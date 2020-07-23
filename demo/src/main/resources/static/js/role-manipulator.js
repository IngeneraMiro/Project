
$(document).ready(function () {
    body();
})

$('body').on('click','button.normal',function () {
    let username = $(this).data('username');
    let str = '{"username": "'+username+'"}';
    $.ajax({
        url: "http://localhost:8080/info/normal/"+username, // A valid URL
        type: 'PUT', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        data: str ,// Some data e.g. Valid JSON as a string
        success: function(data) {
            body();
            alert(data);
        }
    });
});

$('body').on('click','button.main',function () {
    let username = $(this).data('username');
    let str = '{"username": "}'+username+'"}';
    $.ajax({
        url: "http://localhost:8080/info/main/"+username, // A valid URL
        type: 'PUT', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        data: str ,// Some data e.g. Valid JSON as a string
        success: function(data) {
            body();
            alert(data);
        }
    });

});
function body() {
    var value = $("#practice").data('value');
    $('#data').empty();
    fetch('http://localhost:8080/info/doctors/' + value).then(responce => responce.json())
        .then((json) => json.forEach((doc, idx) => {
            if(doc.authorities.toString() == 'DOCTOR'){
                var button = '<button class="main" data-username="'+doc.username+'">Главен лекар</button>';
            }else{
                var button = '<button class="normal" data-username="'+doc.username+'">Лекар</button>';
            }
            let string =
                '<div class="row w-100 mt-3 text-white" >'+
                '<div class="col w-25">' +
                doc.username +
                '</div>'+
                '  <div class="col w-25">'+
                doc.firstName + ' ' + doc.lastName +
                '</div>' +
                '<div class="col w-25">' +
                doc.authorities +
                '  </div>' +
                '<div class="col w-25">' +
                button +
                '  </div>' +
                '</div>';
            $('#data').append(string);
        }));
}