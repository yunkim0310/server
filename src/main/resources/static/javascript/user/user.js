
// 8글자이상 20이하 영어, 숫자, 특수문자 모두 사용
function pwdValidation() {

  // 비밀번호 값 가져오기
  var pwd = $("#password").val();

  const messageElement = $("#invalid-feedback-pwd");

  // 공백 유효성 체크
  if(pwd=='') {
    messageElement.text('비밀번호를 입력해주세요');
    messageElement.css('color', 'red');
    return;

  } else if ( !pwd == '' ){
    messageElement.text('');
    messageElement.css('color', '');
  }

  const pwdChk = validationPwdRule(pwd);

  if( !pwdChk ) { // true
    messageElement.text('비밀번호를 확인해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return;
  } else {
    messageElement.text('사용 가능한 비밀번호 입니다.');
    messageElement.css({'color': 'green', 'display': 'block'});
  }
  

}


// 비밀번호1. 비밀번호2 일치 유효성
function isMatch () {

  var pwd1 = $("#password").val();
  var pwd2 = $("#passwordConfirm").val();

  const messageElement = $("#invalid-feedback-pwdConfirm");

  if (pwd1 !== pwd2 ) {
    messageElement.text('비밀번호가 일치하지 않습니다.');
    messageElement.css({'color': 'red', 'display': 'block'});
  } else {
    messageElement.text('비밀번호가 일치합니다.');
    messageElement.css({'color': 'green', 'display': 'block'});
  }
}



// email 유효성 검사
function emailValidation() {

  const email = $('#email').val();
  console.log('>> email :: ', email);

  const messageElement = $("#invalid-feedback-email");

  $.ajax({
    url: '/api-user/chkEmail',
    type: 'GET', // HTTP 메서드
    data: { email: email },

    success: function (isDuplicate) {

      // 사용불가
      if (isDuplicate) {
        messageElement.text('존재하는 이메일 입니다..');
        messageElement.css({'color': 'red', 'display': 'block'});
        // 사용가능
      } else {
        messageElement.text('사용 가능한 이메일 입니다.');
        messageElement.css({'color': 'green', 'display': 'block'});
      }
    }, // end of success
    error: function () {
      messageElement.text('이메일 확인 중 오류가 발생했습니다.');
      messageElement.css({'color': 'red', 'display': 'block'});
    } // end of error
  });

}








// username 유효성 검사
function validation() {

  // username 초기화
  const username = $('#username').val();

  // 보여줄 메시지 초기화
  // const messageElement = $(".invalid-feedback-username");
  const messageElement = $("#invalid-feedback-username");



  // username 입력 안했을 때
  // if (!username) {
  //   messageElement.text('');
  //   messageElement.css('color', '');
  //   return;
  // }

  if (username == '') {
    messageElement.text('아이디를 입력해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return;
  }

  // username 길이 체크
  const usernameChk = validationUsernameRule(username);

  if( !usernameChk ) { // true
    messageElement.text('아이디를 확인해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return;
  }
  

  $.ajax({
    url: '/api-user/chkDuplication',
    type: 'GET', // HTTP 메서드
    data: { username: username },

    success: function (isDuplicate) {

      // 사용불가
      if (isDuplicate) {
        messageElement.text('이미 사용 중인 아이디입니다.');
        messageElement.css({'color': 'red', 'display': 'block'});
      // 사용가능
      } else {
        messageElement.text('사용 가능한 아이디입니다.');
        messageElement.css({'color': 'green', 'display': 'block'});
      }
    }, // end of success
    error: function () {
      messageElement.text('아이디 확인 중 오류가 발생했습니다.');
      messageElement.css({'color': 'red', 'display': 'block'});
    } // end of error
  });

} // end of validation

// 4~12 글자 영어나 숫자 조합
// 특수문자 포함 안됨
function validationUsernameRule(username) {
  return /^[a-zA-Z](?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{3,11}$/g.test(username);
}

// 8~20글자 특수문자 모두 사용
function validationPwdRule(pwd) {
  return /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,20}$/.test(pwd);
}

