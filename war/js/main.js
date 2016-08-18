var update_flag = false;

$(function() {
    init_data();

    window.setTimeout(function() {
        console.log('wait...');
        setInterval(renew_data, 120000);
    }, 120000);
});

function renew_data() {

    if(update_flag) {
        console.log('updating, stop renew.');
        return;
    }

    $.get("recentSeat", function(data) {
        console.log("renew data..., " + data.length);

        if (data.length > 0) {
            data.forEach(function(element, index) {
                var seat_item = build_seat_item(element);
                $("#s" + element.id).remove();
                $("div#t" + element.seat + " ul.row").append(seat_item);
                $("#n" + element.id + " span").text(' [ ' + element.seat + ' 桌 ]');
            });

            show_click();
            seat_change();
            calculate_seat_guest();
        }

        console.log('renew finished');
    });
}

function calculate_seat_guest() {
    var showed = $(".showed").length;
    var total = showed + $(".normal").length;
    var percentage = showed / total;
    $('.head-info').text(" - " + showed + " / " + total + " - " + Math.round(percentage * 10000) / 100 + "%");

    $('.panel-body ul.row').each(function(idx) {
        var show = $(this).children('.showed').length;
        var sum = show + $(this).children('.normal').length;
        var index = $(this).attr('tab');
        $("#t" + index + " h4").children("span").text(show + " / " + sum);
        $("#c" + index).text(show + " / " + sum);
    });
}

function show_click() {
    $(".guest a").off('click');
    $(".guest a").on('click', function(e) {
        e.preventDefault();

        update_flag = true;

        var liItem = $(this).parent('li');
        var itemId = $(liItem).attr('id').replace("s", ""),
            newStatus = 0;

        if (liItem.hasClass('normal')) {
            newStatus = 1;
        } else if (liItem.hasClass('showed')) {
            newStatus = 2;
        } else {
            newStatus = 0;
        }

        $.ajax({
            type: 'POST',
            data: {
                'id': itemId,
                'status': newStatus
            },
            url: '/updateSeat',
            timeout: 5000,
            success: function(res, status, xhr) {
                if (newStatus == 1) {
                    liItem.removeClass('normal');
                    liItem.addClass('showed');
                } else if (newStatus == 2) {
                    liItem.removeClass('showed');
                    liItem.addClass('absense');
                } else {
                    liItem.removeClass('absense');
                    liItem.addClass('normal');
                }
                calculate_seat_guest();
                update_flag = false;
            },
            error: function(xhr, status, err) {
                alert('抱歉，網路似乎出了一點狀況，請再試試');
                update_flag = false;
            }
        });
    });
}

function seat_change() {
    $(".guest select").off('change');
    $(".guest select").on('change', function(e) {
        e.preventDefault();

        update_flag = true;

        var newSeat = $(this).val();
        var item = $(this).parent('span').parent('li');
        var itemId = item.attr('id').replace("s", "");

        // POST update seat
        $.ajax({
            type: 'POST',
            data: {
                'id': itemId,
                'seat': newSeat
            },
            url: '/updateSeat',
            timeout: 5000,
            success: function(res, status, xhr) {
                $("#s" + itemId).remove();
                $("div#t" + newSeat + " ul.row").append(item);
                $("#n" + itemId + " span").text(' [ ' + newSeat + ' 桌 ]');
                location.href = "#table" + newSeat;
                calculate_seat_guest();
                show_click();
                seat_change();
                update_flag = false;
            },
            error: function(xhr, status, err) {
                alert('抱歉，網路似乎出了一點狀況，請再試試');
                update_flag = false;
            }
        });
    });
}

function build_seat_item(item) {
    var ele = '<li id="s';

    ele += item.id + '" class="guest ';

    if (item.status == 1) {
        ele += 'showed';
    } else if (item.status == 2) {
        ele += 'absense';
    } else {
        ele += 'normal';
    }

    ele += '">';
    ele += '<a href="#" class="';

    ele += (item.isVeg) ? 'veg ' : '';
    ele += (item.isChild) ? 'child  ' : '';

    ele += '">' + item.name + '</a>';
    ele += '<span><select class="chooseTable">';

    for (var idx = 1; idx < 30; idx++) {
        ele += '<option value="' + idx + '" ';
        ele += (idx == item.seat) ? 'selected' : '';
        ele += '>' + idx + '</option>';
    }

    ele += '</select></span>';
    ele += '</li>';

    return ele;
}

function build_name_item(item) {
    var ele = '<li>';
    ele += '<a id="n' + item.id + '" href="#table' + item.seat + '" class="';

    ele += (item.isVeg) ? 'veg ' : '';
    ele += (item.isChild) ? 'child  ' : '';

    ele += '">' + item.name;
    ele += '<span>';

    ele += ' [ ' + item.seat + ' 桌 ]';

    ele += '</span></a>';
    ele += '</li>';

    return ele;
}

function init_data() {
    console.log('init...');

    $.get("weddingSeat", function(data) {
        console.log("retrieve data..., " + data.length);
        var preChar = "",
            curChar = "";

        data.forEach(function(element, index) {
            var seat_item = build_seat_item(element);
            var name_item = build_name_item(element);

            curChar = element.name.charAt(0);

            if ((curChar != preChar) && (index != 0)) {
                $("div#namePanel").append("<hr>");
            }

            $("div#t" + element.seat + " ul.row").append(seat_item);
            $("div#namePanel").append(name_item);

            preChar = element.name.charAt(0);
        });

        show_click();
        seat_change();
        calculate_seat_guest();

        console.log('init finished');
    });
}
