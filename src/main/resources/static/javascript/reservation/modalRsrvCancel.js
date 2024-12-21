document.addEventListener("DOMContentLoaded", function () {
    const reasonTextarea = document.getElementById("reasonTextarea");
    const charCounter = document.getElementById("charCounter");
    const confirmBtn = document.querySelector(".confirm-btn");

    reasonTextarea.addEventListener("input", function () {
        // 줄바꿈 제거
        this.value = this.value.replace(/\n/g, "");

        const currentLength = reasonTextarea.value.length;
        const remaining = 50 - currentLength;

        if (currentLength < 10) {
            charCounter.textContent = `최소 10자 이상 입력하세요 (${remaining}자 남음)`;
            charCounter.className = "error";
            confirmBtn.disabled = true;
        } else {
            charCounter.textContent = `${remaining}자 남음`;
            charCounter.className = "success";
            confirmBtn.disabled = false;
        }
    });
});