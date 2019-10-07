$(function () {
    var jInput = $('#new-vehicle-input');
    var jA = $(jInput).next('a');
    var template = jA.attr('data-href-template');

    var inputEmpty = function () {
        return jInput.val().trim().length === 0;
    };

    jInput.change(function () {
        jA.attr('href', template.replace('string-template', $(this).val().toUpperCase()));
    });

    jA.click(function (e) {
        if (inputEmpty()) {
            jInput.css('border-color', 'red').css('background-color', '#ffdddd').select();
            e.preventDefault();
        }
    })
});