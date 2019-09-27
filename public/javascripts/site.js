$(function () {
    $('#new-vehicle-input').change(function () {
        var ja = $(this).next('a');
        var template = ja.attr('data-href-template');
        var newHref = template.replace('string-template', $(this).val());
        ja.attr('href', newHref)
    })
});