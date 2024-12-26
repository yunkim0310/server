$(document).ready(function () {
    const $favicon = $('<link>', {
        rel: 'icon',
        type: 'image/x-icon',
        href: '/images/common/smallLogo.png?v=2'
    });

    // 기존 파비콘 제거
    $('link[rel="icon"]').remove();

    // 새로운 파비콘 추가
    $('head').append($favicon);
});