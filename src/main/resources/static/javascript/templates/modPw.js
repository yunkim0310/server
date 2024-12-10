function modOk2(){

    // const prHp = document.getElementById("prHp")
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');



    // 정규 표현식
    // const expPwText = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}/;
    const expPwText = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}/;



    if (!expPwText.test(password.value)) {
        alert('비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합하여 8-20자리로 설정해주세요.');
        // alert('비밀번호 형식을 확인하세요\n소문자,대문자,숫자,특수문자를 1개씩 꼭 입력해야합니다.');
        password.focus();
        return false;
    }

    if (userpw.value != userpw_re.value) {
        alert('비밀번호를 확인해 주세요.');
        confirmPassword.focus();
        return false;
    }


    return true;
}

// TODO 비밀번호 확인, 김윤지
function pwCheck(){
    if($('#pw1').val() == $('#pw2').val()){
        $('#pwConfirm').text('비밀번호 일치').css('color', 'green')
    }else{
        $('#pwConfirm').text('비밀번호 불일치').css('color', 'red')
    }
}