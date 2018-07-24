onRowSelect = (function onselect(rowId, selId) {
    var context = '/';
    /*[+
    context = [[@{/}]];
    +]*/
    var rows = document.getElementsByClassName('selectable_row');
    for (var i = rows.length - 1; i >= 0; --i) {
        rows[i].className = 'selectable_row';

    }
    var selRow = document.getElementById(rowId);
    if (selRow != null) {
        selRow.className += ' bg-primary';
    }
    document.selId = selId;
    var del = document.getElementById("delete_element");
    if (del != null) {
        del.action = context + url + "?action=delete&id=" + selId;
    }

});
showedit = (function update(url) {
    var context = '/';
    /*[+
        context = [[@{/}]];
    +]*/
    $.ajax({
        url: context + url,
        cache: false,
        contentType: false,
        processData: false,
        type: 'GET',
        success: function (result) {
            $('#edit_div').html(result);
            if ($('#frag_message').val()) {
                $('#message_box').text($('#frag_message').val());
                $('#frag_message').val('');
                $('#messageModal').modal('show');
            } else {
                $('#editModal').modal('show');
            }
        },
        error: function (xhr) {
            var json = xhr.responseJSON;
            $('#editModal').modal('hide');
            $('#message_box').text(json ? json.message : xhr.responseText);
            $('#messageModal').modal('show');
        }
    });
});
showview = (function update(url) {
    var context = '/';
    /*[+
        context = [[@{/}]];
    +]*/
    $.ajax({
        url: context + url,
        cache: false,
        contentType: false,
        processData: false,
        type: 'GET',
        success: function (result) {
            $('#view_div').html(result);
            if ($('#frag_message').val()) {
                $('#message_box').text($('#frag_message').val());
                $('#frag_message').val('');
                $('#messageModal').modal('show');
            } else {
                $('#viewModal').modal('show');
            }
        },
        error: function (xhr) {
            var json = xhr.responseJSON;
            $('#editModal').modal('hide');
            $('#message_box').text(json ? json.message : xhr.responseText);
            $('#messageModal').modal('show');
        }
    });
});
postdata = (function update(url, form, div_name) {
    var divName = "table_div";
    if (div_name){
        divName = div_name;
    }
    var context = '/';
    /*[+
        context = [[@{/}]];
    +]*/
    if (form) {
        form.validate({
            lang: 'ru',
            errorPlacement: function (error, element) {
                error.appendTo('#errordiv');
            }

        });
        if (form.valid()) {
            var data = new FormData(form[0]);

            $.ajax({
                url: context + url,
                data: data,
                cache: false,
                contentType: false,
                processData: false,
                type: 'POST',
                success: function (result) {
                    $('#'+divName).html(result);
                    $('#editModal').modal('hide');
                    if ($('#frag_message').val()) {
                        $('#message_box').text($('#frag_message').val());
                        $('#frag_message').val('');
                        $('#messageModal').modal('show');
                    } else {
                        $('#messageModal').modal('hide');
                    }
                },
                error: function (xhr) {
                    var json = xhr.responseJSON;
                    $('#editModal').modal('hide');
                    $('#message_box').text(json ? json.message : xhr.responseText);
                    $('#messageModal').modal('show');
                }
            });
        }
    } else {
        $.ajax({
            url: context + url,
            cache: false,
            contentType: false,
            processData: false,
            type: 'GET',
            success: function (result) {
                $('#table_div').html(result);
                $('#editModal').modal('hide');
                if ($('#frag_message').val()) {
                    $('#message_box').text($('#frag_message').val());
                    $('#frag_message').val('');
                    $('#messageModal').modal('show');
                } else {
                    $('#messageModal').modal('hide');
                }
            },
            error: function (xhr) {
                var json = xhr.responseJSON;
                $('#editModal').modal('hide');
                $('#message_box').text(json ? json.message : xhr.responseText);
                $('#messageModal').modal('show');
            }
        });
    }
});

function initCanvas(canvas) {
    if (window.G_vmlCanvasManager && window.attachEvent && !window.opera) {
        canvas = window.G_vmlCanvasManager.initElement(canvas);
    }
    return canvas;
}

getchart = (function chart(url, form) {
    var context = '/';
    /*[+
        context = [[@{/}]];
    +]*/
    var ctx = initCanvas(document.getElementById("chart_div")).getContext('2d');
    form.validate({
        errorPlacement: function (error, element) {
            error.appendTo('#errordiv');
        }

    });
    if (form.valid()) {
        var data = new FormData(form[0]);

        $.ajax({
            url: context + url,
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function (data) {
                var arr = [];
                var colors = ["rgba(250, 0, 0", "rgba(0, 250, 0", "rgba(0, 0, 250", "rgba(250, 250, 0", "rgba(0, 250, 250", "rgba(250, 0, 250"];
                var c = 0;
                $.each(data.datasets, function (i, item) {
                    if (c > 6) {
                        c = 0;
                    }
                    c++;
                    arr.push({
                        label: item.label,
                        data: item.data,
                        backgroundColor: colors[c] + ",0.3)",
                        borderColor: colors[c] + ",1)"
                    });
                });
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: data.days,
                        datasets: arr
                    },
                    options: {
                        scales: {
                            yAxes: [{
                                ticks: {
                                    beginAtZero: true
                                },
                                stepSize: 1
                            }]
                        },
                        responsive: true
                    }
                });
                $('#editModal').modal('hide');
                if ($('#frag_message').val()) {
                    $('#message_box').text($('#frag_message').val());
                    $('#frag_message').val('');
                    $('#messageModal').modal('show');
                } else {
                    $('#messageModal').modal('hide');
                }
            },
            error: function (xhr) {
                var json = xhr.responseJSON;
                $('#editModal').modal('hide');
                $('#message_box').text(json ? json.message : xhr.responseText);
                $('#messageModal').modal('show');
            }
        });
    }

});
printElement = (function printElement(element) {
    var divToPrint = element;
    var newWin = window.open('', 'Print-Window');
    newWin.document.open();
    newWin.document.write('<html><body onload="window.print()">' + divToPrint.innerHTML + '</body></html>');
    newWin.document.close();
    setTimeout(function () {
        newWin.close();
    }, 10);

});

