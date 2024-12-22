document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("myModal");
    const cancelBtn = document.getElementById("closeModal");

    const refusalButton = document.getElementById("refusalButton");
    const cancelStoreButton = document.getElementById("cancelStoreButton");
    const cancelUserButton = document.getElementById("cancelUserButton");
    const exampleButton = document.getElementById("exampleButton");

    // 전화번호 마스킹
    const phoneElements = document.querySelectorAll("[data-phone]");
    phoneElements.forEach(element => {
        const originalNumber = element.textContent.trim();
        if (originalNumber.length >= 7) {
            const maskedNumber = originalNumber.substring(0, 3) + "xxxx" + originalNumber.substring(7);
            element.textContent = maskedNumber;
        }
    });

    // 모달 제어 함수
    function openModal(headerText) {
        modal.style.display = "flex";
        document.querySelector('.modal-header').textContent = headerText;
    }

    function closeModal() {
        modal.style.display = "none";
    }

    // 모달 이벤트 바인딩
    if (refusalButton) {
        refusalButton.addEventListener("click", function () {
            openModal("예약을 거절 하겠습니까?");
        });
    }

    if (cancelStoreButton) {
        cancelStoreButton.addEventListener("click", function () {
            openModal("예약을 취소 하겠습니까?");
        });
    }

    if (cancelUserButton) {
        cancelUserButton.addEventListener("click", function () {
            openModal("예약을 취소 하겠습니까?");
        });
    }

    if (exampleButton) {
        exampleButton.addEventListener("click", function () {
            openModal("제목 필요한거 쓰십시오");
        });
    }

    // 모달 닫기
    if (cancelBtn) {
        cancelBtn.addEventListener("click", closeModal);
    }

    // 모달 외부 클릭 시 닫기
    window.addEventListener("click", function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });
});