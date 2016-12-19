$(function () {
    $('#new-vehicle-input').keyup(function () {
        var ja = $(this).next('a');
        var tempate = ja.attr('data-href-template');
        var newHref = tempate.replace('string-template', $(this).val());
        ja.attr('href', newHref)
    })
});