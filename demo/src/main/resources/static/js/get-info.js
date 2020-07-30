$(document).ready(function () {
    let type1 = 'Профилактични прегледи';
    let type2 = 'Диспансерна дейност';
    let type3 = 'Амбулаторна дейност';
    $.ajax({
        url: "http://localhost:8080/information/getinfo/"+type1, // A valid URL
        type: 'GET', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        success: function (data) {
            $('#info1').append(data);
        },
        error: function (Xhr, textStatus, errorThrown) {
            alert(Xhr.responseText);
        }
    })
    $.ajax({
        url: "http://localhost:8080/information/getinfo/"+type2, // A valid URL
        type: 'GET', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        success: function (data) {
            $('#info2').append(data);
        },
        error: function (Xhr, textStatus, errorThrown) {
            alert(Xhr.responseText);
        }
    })
    $.ajax({
        url: "http://localhost:8080/information/getinfo/"+type3, // A valid URL
        type: 'GET', // Use POST with X-HTTP-Method-Override or a straight PUT if appropriate.
        success: function (data) {
            $('#info3').append(data);
        },
        error: function (Xhr, textStatus, errorThrown) {
            alert(Xhr.responseText);
        }
    })
})