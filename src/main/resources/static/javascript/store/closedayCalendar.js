// 휴무일 달력 함수
document.addEventListener('DOMContentLoaded', function () {

    const calendarEl = document.getElementById('closedayCalendar');

    const calendar = new FullCalendar.Calendar(calendarEl, {

        initialView: 'dayGridMonth',
        locale: 'ko',
        events: function (info, successCallback, failureCallback) {
            $.ajax({
                url: '/api-store/getClosedayByMonth',
                type: 'GET',
                data: {
                    storeId: 1,
                    startDate: info.startStr,
                    endDate: info.endStr
                },
                success: function (data) {
                    const events = data.map(closeday => ({
                        title: '휴무일',
                        start: closeday.closeday,
                        allDay: true,
                        id: closeday.closedayId,
                        backgroundColor: '#4880FF'
                    }));
                    successCallback(events);
                },
                error: function (xhr, status, error) {
                    console.error('휴무일 데이터를 가져오는 중 오류 발생:', error);
                    failureCallback(error);
                }
            });
        },
        eventContent: function (arg) {

            const deleteButton = document.createElement('span');

            deleteButton.classList.add('delete-btn');
            deleteButton.textContent = 'x';
            deleteButton.style.marginLeft = '10px';
            deleteButton.style.color = '#ff6868';
            deleteButton.style.cursor = 'pointer';

            // 삭제 버튼 클릭 이벤트 추가
            deleteButton.addEventListener('click', function (event) {

                event.stopPropagation();
                const closedayId = arg.event.id;
                const closeday = arg.event.start;

                // AJAX 요청으로 삭제
                if (confirm(`휴무일 ${closeday} 를 삭제하시겠습니까?`)) {
                    $.ajax({
                        url: '/api-store/removeCloseday',
                        type: 'POST',
                        data: {closedayId: closedayId},
                        success: function () {

                            alert('휴무일이 삭제되었습니다.');
                            calendar.refetchEvents(); // 달력 다시 로드
                        },
                        error: function (xhr, status, error) {

                            console.error('휴무일 삭제 중 오류 발생:', error);
                            alert('휴무일 삭제에 실패했습니다.');
                        }
                    });
                }

            });

            const customEventContent = document.createElement('div');

            customEventContent.textContent = arg.event.title;
            customEventContent.appendChild(deleteButton);
            customEventContent.style.padding = '10px';
            customEventContent.style.borderRadius = '10px';
            customEventContent.style.textAlign = 'center';

            return { domNodes: [customEventContent] };
        }
    });

    calendar.render();
});