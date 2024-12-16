// 모든 유효성 검사 통과
function validateAll() {

  const isUsernameValid = usernameValidation();
  const isPwdValid = pwdValidation();
  const isPwdMatch = isMatch();
  const isEmailValid = emailValidation();

  console.log('isUsernameValid :: ' , isUsernameValid);
  console.log('isPwdValid :: ', isPwdValid);
  console.log('isPwdMatch :: ', isPwdMatch);
  console.log('isEmailValid :: ', isEmailValid);

  // 모든 유효성 검사 통과 시 회원가입 요청
  if ( isUsernameValid && isPwdValid && isPwdMatch && isEmailValid ) {
    // 유효성 검사 통과 후, 실제 가입 요청
    const user = {
      username: $('#username').val(),
      email: $('#email').val(),
      password: $('#password').val(),
      recommendedId: $('#recommendedId').val(),
      role: $('[name="role"]').val(),
      gender: $('[name="gender"]:checked').val()
    };
    
    console.log('회원가입 객체 :: ', user);

    $.ajax({
      // url: '/api-user/signup',
      url: '/api-user/join',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(user),
      success: function(data) {
        if (data.success) {
          alert("회원가입 성공!");
          location.href='/loginView';
        } else {
          alert("회원가입 실패!");
        }
      },
      error: function(xhr, status, error) {
        console.error('Error:', error);
        alert("회원가입 중 오류가 발생했습니다.");
      }
    });
  } else {
    alert('모든 유효성 검사를 통과해야 회원가입이 가능합니다.');
    return false;
  }

} // end of validationAll



// 8글자이상 20이하 영어, 숫자, 특수문자 모두 사용
function pwdValidation() {

  // 비밀번호 값 가져오기
  var pwd = $("#password").val();

  const messageElement = $("#invalid-feedback-pwd");

  // 공백 유효성 체크
  if(pwd=='') {
    messageElement.text('비밀번호를 입력해주세요');
    messageElement.css('color', 'red');
    return false;

  } else if ( !pwd == '' ){
    messageElement.text('');
    messageElement.css('color', '');
  }

  const pwdChk = validationPwdRule(pwd);

  if( !pwdChk ) { // true
    messageElement.text('비밀번호를 확인해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return  false;
  } else {
    messageElement.text('사용 가능한 비밀번호 입니다.');
    messageElement.css({'color': 'green', 'display': 'block'});
    return true;
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

    return false;
  } else {
    messageElement.text('비밀번호가 일치합니다.');
    messageElement.css({'color': 'green', 'display': 'block'});

    return true;
  }
}



// email 유효성 검사
function emailValidation() {

  const email = $('#email').val();

  const messageElement = $("#invalid-feedback-email");

    // 이메일 형식 확인
    const emailChk = emailCheck(email);

    if( !emailChk ) { // true
    messageElement.text('이메일 형식이 아닙니다.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return;
  } else if ( emailChk ){
    messageElement.text('');
    messageElement.css({'color': '', 'display': ''});
    return true;
  }

  $.ajax({
    url: '/api-user/chkEmail',
    type: 'GET', // HTTP 메서드
    data: { email: email },


    success: function (isDuplicate) {

      // 사용불가
      if (isDuplicate) {
        console.log('이게 또 안돼?')
        messageElement.text('존재하는 이메일 입니다..');
        messageElement.css({'color': 'red', 'display': 'block'});
        return false;

      }  else if ( !isDuplicate ){
        messageElement.text('사용 가능한 이메일 입니다.');
        messageElement.css({'color': 'green', 'display': 'block'});
        return true;
      }
    }, // end of success
    error: function () {
      messageElement.text('이메일 확인 중 오류가 발생했습니다.');
      messageElement.css({'color': 'red', 'display': 'block'});
      return false;
    } // end of error
  });

}


function recomandValidation () {

  const username = $('#recommendedId').val();
  const messageElement = $("#invalid-feedback-recommendedId");

  $.ajax({
    url: '/api-user/chkDuplication',
    type: 'GET', // HTTP 메서드
    data: { username: username },

    success: function (isDuplicate) {

      // 사용불가
      if (isDuplicate) {
        messageElement.text('500 point 적립!');
        messageElement.css({'color': 'green', 'display': 'block'});
        return true;
        // 사용가능
      } else if ( username == '' ) {
        messageElement.text('');
        messageElement.css({'color': '', 'display': ''});
        return true;
        // 사용가능
      }
      else {
        messageElement.text('존재하지 않는 회원입니다.');
        messageElement.css({'color': 'red', 'display': 'block'});
        return false;
      }
    }, // end of success
    error: function () {
      messageElement.text('아이디 확인 중 오류가 발생했습니다.');
      messageElement.css({'color': 'red', 'display': 'block'});
      return false;
    } // end of error
  });

}


// username 유효성 검사
function usernameValidation() {

  // username 초기화
  const username = $('#username').val();

  // 보여줄 메시지 초기화
  // const messageElement = $(".invalid-feedback-username");
  const messageElement = $("#invalid-feedback-username");

  // username 길이 체크
  const usernameChk = validationUsernameRule(username);

  if( !usernameChk ) { // true
    messageElement.text('아이디를 확인해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return;
  } else if ( usernameChk ){
    messageElement.text('사용 가능한 아이디 입니다.');
    messageElement.css({'color': 'green', 'display': 'block'});
    return true;
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
        return false;
        // 사용가능
      } else {
        messageElement.text('사용 가능한 아이디입니다.');
        messageElement.css({'color': 'green', 'display': 'block'});
        return true;
      }
    }, // end of success
    error: function () {
      messageElement.text('아이디 확인 중 오류가 발생했습니다.');
      messageElement.css({'color': 'red', 'display': 'block'});
      return false;
    } // end of error
  });


  if (username == '') {
    messageElement.text('아이디를 입력해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return false;
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
        return false;
      // 사용가능
      } else {
        messageElement.text('사용 가능한 아이디입니다.');
        messageElement.css({'color': 'green', 'display': 'block'});
        return true;
      }
    }, // end of success
    error: function () {
      messageElement.text('아이디 확인 중 오류가 발생했습니다.');
      messageElement.css({'color': 'red', 'display': 'block'});
      return false;
    } // end of error
  }); // end of ajax

} // end of validation

// 4~12 글자 영어나 숫자 조합
// 특수문자 포함 안됨
function validationUsernameRule(username) {
  return /^[a-zA-Z](?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{3,11}$/.test(username);
}

// 8~20글자 특수문자 모두 사용
function validationPwdRule(pwd) {
  return /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,20}$/.test(pwd);
}

// 이메일 형식 확인
function emailCheck(email){

  return  /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i.test(email);

  // if( !regex.test(email) ){
  //   return false;
  // } else {
  //   return true;
  // }

}

// 프로필 사진 업로드
function photoEdit() {
  
  // 파일보기 버튼 사용 안하려고 사용
  $('#profileImg').click();

  $('#profileImg').change(function() {

    const username = $(".username").text();
    const file = event.target.files[0];
    const fileName = file.name;

    console.log("선택된 파일:", file);
    console.log("선택된 파일 22 :", fileName);

    console.log('username :: ', username);
    console.log('fileName :: ', fileName);

    // const user = {
    //   username : username,
    //   profileImg : fileName
    // }

    uploadFile(file, "user/")
      .then(result => {
        console.log(result);
        // console.log("uuid file name :: " , result.url)
        console.log("uuid file path :: " , result.filePath)
        // https://placehere.s3.ap-northeast-2.amazonaws.com/user/20241216d64806bc.jpg

        // const resultUrl = result.url;
        const resultUrl = result.filePath;
        const cvrtStr = "user/";
        const replaceFileName = resultUrl.replace(cvrtStr, '');
        
        // uuid 로 변환된 파일명만 추출
        console.log('after :: ', replaceFileName);

        // $('#profileImgView').attr("src",result.url);
        $('#profileImgView').attr("src",replaceFileName);

        const user = {
          username : username,
          profileImg : replaceFileName
        }

        return $.ajax({
          url: "/api-user/updateProfile",
          method: "POST",
          data: JSON.stringify(user),
          contentType: "application/json"
        });

      })
      .then(response => {
        // $.ajax 요청이 성공적으로 완료된 후
        console.log("Profile updated:", response);
      })
      .catch(error => {
        // 에러 처리
        console.error("Error:", error);
      });

  });
} // end of photoEdit

// 비밀번호 재설정
function resetPwd() {

  event.preventDefault();

  const username = $('#username').val();
  const password = $('#password').val();

  console.log('username :: ' + username);
  console.log('password :: ' + password);

  const user = {
    username : username,
    password : password
  }

  $.ajax({
    url: '/api-user/resetPwd',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(user),
    success: function(data) {

      console.log('data :: ',data);

      if (data === "SUCCESS") {
        alert("비밀번호가 변경되었습니다! 재로그인 해주세요.");
        location.href='/';
      } else {
        alert("실패!");
      }
    },
    error: function(xhr, status, error) {
      console.error('Error:', error);
      alert("비밀번호 변경 중 오류가 발생했습니다.");
    }

  });

  
}

// 회원탈퇴 함수
function goodBye() {

  // username 가져오기
  const username = $(".username").text();
  const role = $(".role").val();

  console.log("username :: ", username);
  console.log("role :: ", role);

  const result = confirm("탈퇴하시겠습니까?");
  console.log('result :: ', result);

  const user = {
    username : username,
    role : role
  }

  // true면 실행
  if( result ) {

    $.ajax({
      url: '/api-user/goodBye',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(user),
      success: function(data) {

        console.log('data :: ',data);

        if (data === "SUCCESS") {
          // alert("환불처리 될 예약 건수 :: "  );
          alert("탈퇴 되었습니다.");
          location.href='/';
        } else {
          alert("실패!");
        }
      },
      error: function(xhr, status, error) {
        console.error('Error:', error);
        alert("안되지롱");
      }

    });

  }
  
}