// 데이터 초기화
const dayMapping = { "일": 0, "월": 1, "화": 2, "수": 3, "목": 4, "금": 5, "토": 6 };

const effectiveDays = [];
for (let i = 0; i < closeDayOnEffectDayList.length; i++) {
    const startDate = closeDayOnEffectDayList[i].effectDt;

    const endDate = i + 1 < closeDayOnEffectDayList.length
        ? closeDayOnEffectDayList[i + 1].effectDt
        : null;

    const closedDays = [closeDayOnEffectDayList[i].day1, closeDayOnEffectDayList[i].day2, closeDayOnEffectDayList[i].day3]
        .filter(day => day && dayMapping[day] !== undefined)
        .map(day => dayMapping[day]);

    effectiveDays.push({ startDate, endDate, closedDays });
}

document.addEventListener("DOMContentLoaded", function () {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const fourteenDaysLater = new Date();
    fourteenDaysLater.setDate(today.getDate() + 13);

    const calendarEl = document.getElementById('calendar');
    const selectedDateInput = document.getElementById('selectedDate');
    const rsrvPersonSelect = document.getElementById('rsrvPerson');
    const amountBox = document.getElementById('amount-box');
    const amountInput = document.getElementById('amount');
    const regularClosedayList = /*[[${regularClosedayList}]]*/ [] || [];
    const closedayList = /*[[${closedayList}]]*/ [] || [];
    const form = document.querySelector("form");
    const part1 = document.getElementById("phone-part1");
    const part2 = document.getElementById("phone-part2");
    const part3 = document.getElementById("phone-part3");
    const rsrvReqInput = document.getElementById("rsrvReq");
    const charCountDisplay = document.getElementById("charCount");
    storeClose.forEach(date => closedayList.push(date));

    let selectedDate = "";
    let selectedTime = "";

    const calendar = new FullCalendar.Calendar(calendarEl, {
        locale: "ko",
        initialView: 'dayGridMonth',
        timeZone: 'Asia/Seoul',
        height: 'auto',
        contentHeight: 'auto',
        dateClick: function (info) {
            if (!info.dayEl.classList.contains('fc-day-disabled') && info.date >= today && info.date <= fourteenDaysLater) {
                selectedDate = info.dateStr;

                document.querySelectorAll('.fc-day-selected').forEach(el => el.classList.remove('fc-day-selected'));
                info.dayEl.classList.add('fc-day-selected');

                fetch(`/api-reservation/getOperationByDate?storeId=${document.querySelector('[name="storeId"]').value}&effectDt=${info.dateStr}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data) {
                            const openTime = data.storeOperation.openTime;
                            const closeTime = data.storeOperation.closeTime;
                            const breakStart = data.storeOperation.breakTimeStart;
                            const breakEnd = data.storeOperation.breakTimeEnd;

                            const timeArray = generateTimeArray(
                                openTime,
                                closeTime,
                                breakStart,
                                breakEnd,
                                data.reservationTimeStatusList,
                                data.storeOperation.rsrvLimit
                            );

                            generateTimeTable(timeArray, data.storeOperation.rsrvLimit, data.reservationTimeStatusList);
                        } else {
                            alert("No operation details found for the selected date.");
                        }
                    })
                    .catch(error => console.error("Error fetching operation details:", error));
            }
        },
        dayCellDidMount: function (info) {
            applyDisabledDates(info);
        },
        datesSet: function () {
            const allCells = calendarEl.querySelectorAll('.fc-daygrid-day');
            allCells.forEach(cell => {
                const date = cell.getAttribute('data-date');
                if (date) {
                    const info = { date: new Date(date), el: cell };
                    applyDisabledDates(info);
                }
            });
        },
    });

    calendar.render();

    function applyDisabledDates(info) {
        const dateStr = info.date.toISOString().split('T')[0];

        if (closedayList.includes(dateStr)) {
            info.el.classList.add('fc-day-disabled');
        }

        const dayOfWeek = info.date.getDay();
        if (regularClosedayList.includes(dayOfWeek.toString())) {
            info.el.classList.add('fc-day-disabled');
        }

        if (info.date < today || info.date > fourteenDaysLater) {
            info.el.classList.add('fc-day-disabled');
        }
    }

    function generateTimeArray(openTime, closeTime, breakStart, breakEnd, reservationTimeStatusList, maxLimit) {
        const start = parseTime(openTime);
        const end = parseTime(closeTime);

        if (end <= start) {
            end.setDate(end.getDate() + 1);
        }

        const breakStartTime = breakStart ? parseTime(breakStart) : null;
        const breakEndTime = breakEnd ? parseTime(breakEnd) : null;
        const result = [];

        while (start < end) {
            const currentTime = formatTime(start);

            const isReservedOverLimit = reservationTimeStatusList.some(reservation => {
                const reservationTime = formatTime(parseTime(reservation.rsrvDt.split(' ')[1]));
                return reservationTime === currentTime && reservation.rsrvPerson >= maxLimit;
            });

            const isInBreakTime = breakStartTime && breakEndTime && (start >= breakStartTime && start < breakEndTime);

            if (!isInBreakTime && !isReservedOverLimit) {
                result.push(currentTime);
            }
            start.setMinutes(start.getMinutes() + 30);
        }

        return result;
    }

    function generateTimeTable(timeArray, maxLimit, reservationTimeStatusList) {
        const timeTable = document.getElementById('time-table');
        timeTable.innerHTML = '';
        const maxPerRow = 5;
        let currentRow;

        timeArray.forEach((time, index) => {
            if (index % maxPerRow === 0) {
                currentRow = document.createElement('div');
                currentRow.className = 'time-row';
                timeTable.appendChild(currentRow);
            }
            const button = document.createElement('div');
            button.className = 'time';
            button.textContent = time;

            button.addEventListener('click', function () {
                selectedTime = time;

                document.querySelectorAll('.time').forEach(el => el.style.backgroundColor = "");
                button.style.backgroundColor = "#c3d5fe";

                selectedDateInput.value = `${selectedDate}T${selectedTime}`;

                const reservedCount = reservationTimeStatusList.find(
                    res => formatTime(parseTime(res.rsrvDt.split(' ')[1])) === time
                )?.rsrvPerson || 0;

                updateRsrvPersonOptions(maxLimit - reservedCount);
            });

            currentRow.appendChild(button);
        });

        timeTable.classList.remove('no-display');
    }

    function updateRsrvPersonOptions(availableSlots) {
        rsrvPersonSelect.innerHTML = '';
        for (let i = 1; i <= availableSlots; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = i;
            rsrvPersonSelect.appendChild(option);
        }
        const selectedPersons = parseInt(rsrvPersonSelect.value, 10) || 0;
        amountInput.value = selectedPersons * pricePerPerson;
    }

    function parseTime(timeString) {
        const [hours, minutes] = timeString.split(':').map(Number);
        const date = new Date();
        date.setHours(hours, minutes, 0, 0);
        return date;
    }

    function formatTime(date) {
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${hours}:${minutes}`;
    }

    rsrvPersonSelect.addEventListener('change', function () {
        const selectedPersons = parseInt(rsrvPersonSelect.value, 10) || 0;
        const totalAmount = selectedPersons * pricePerPerson;
        amountBox.innerText = `${totalAmount.toLocaleString()} 원`;
        amountInput.value = totalAmount;
    });

    form.addEventListener("submit", function (event) {
        let isValid = true;
        let errorMessage = "예약 정보가 입력이 부족합니다.\n";

        if (!rsrvPersonSelect.value) {
            isValid = false;
            errorMessage += "- 예약 인수를 선택해주세요.\n";
        }

        const part1Value = part1.value;
        const part2Value = part2.value;
        const part3Value = part3.value;

        if (!/^\d{3,4}$/.test(part2Value) || !/^\d{4}$/.test(part3Value)) {
            alert("전화번호를 올바르게 입력하세요.");
            event.preventDefault();
            return;
        }

        if (!isValid) {
            alert(errorMessage);
            event.preventDefault();
        }

        const phoneNumber = `${part1Value}${part2Value}${part3Value}`;
        console.log("Phone Number:", phoneNumber);

        const hiddenPhoneField = document.createElement("input");
        hiddenPhoneField.type = "hidden";
        hiddenPhoneField.name = "rsrvNumber";
        hiddenPhoneField.value = phoneNumber;
        form.appendChild(hiddenPhoneField);
    });

    rsrvReqInput.addEventListener("input", function () {
        this.value = this.value.replace(/\n/g, "");

        const currentLength = rsrvReqInput.value.length;

        charCountDisplay.textContent = `${currentLength} / 100`;

        if (currentLength > 100) {
            rsrvReqInput.value = rsrvReqInput.value.slice(0, 100);
            charCountDisplay.textContent = "100 / 100";
        }
    });

    window.addEventListener('pageshow', function (event) {
        if (event.persisted) {
            window.location.reload();
        }
    });
});
