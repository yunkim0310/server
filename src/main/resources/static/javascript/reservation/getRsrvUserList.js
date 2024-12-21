document.addEventListener("DOMContentLoaded", function () {
    const reservationItems = document.querySelectorAll(".reservation-item");
    const reservationListCount = document.querySelectorAll(".reservation-item").length;

    const pagingContainer = document.querySelector(".paging-container");

    reservationItems.forEach(item => {
        item.addEventListener("click", function (event) {
            const reservationNumber = this.querySelector(".reservation-number").textContent.trim();

            if (event.target.classList.contains("review-button")) {
                const review = `/review/addReview`;
                return window.location.href = review;
            }

            const destination = `/reservation/getRsrv?rsrvNo=${reservationNumber}`;
            window.location.href = destination;
        });
    });

    if (pagingContainer) {
        let brCount = 1;

        if (reservationListCount > 0) {
            brCount = reservationListCount * 14;
        }

        for (let i = 0; i < brCount; i++) {
            const br = document.createElement("br");
            pagingContainer.insertAdjacentElement("beforebegin", br);
        }
    }

    const phoneElements = document.querySelectorAll("[data-phone]");
    phoneElements.forEach(element => {
        const originalNumber = element.textContent.trim();
        if (originalNumber.length >= 7) {
            const maskedNumber = originalNumber.substring(0, 3) + "xxxx" + originalNumber.substring(7);
            element.textContent = maskedNumber;
        }
    });
});

function search(page) {
    document.querySelector("input[name='page']").value = page;
    document.querySelector("form[name='searchForm']").submit();
}

function autoSubmitForm() {
    document.getElementById("searchForm").submit();
}

pageNavigator(search);