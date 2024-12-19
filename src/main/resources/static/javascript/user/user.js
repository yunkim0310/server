// 모든 유효성 검사 통과
function validateAll() {

  event.preventDefault();

  // username
  const usernameMsgElement = $("#invalid-feedback-username");
  const usernameValid = usernameMsgElement.css('color') === 'rgb(0, 128, 0)' ? true : false;
  console.log('usernameValid ::: ', usernameValid);

  // pwd
  const pwdMsgElement = $("#invalid-feedback-pwd");
  const pwdValid = pwdMsgElement.css('color') === 'rgb(0, 128, 0)' ? true : false;
  console.log('pwdValid :: ', pwdValid);

  // pwd match
  const isPwdMatch = isMatch();
  console.log('isPwdMatch ::: ', isPwdMatch);

  // email
  const emailMsgElement = $("#invalid-feedback-email");
  const emailValid = emailMsgElement.css('color') === 'rgb(0, 128, 0)' ? true : false;
  console.log('emailValid :: ', emailValid);

  // role
  const role = $('[name="role"]').val();
  console.log('role :: ', role);

  // gender
  const gender = $("input[name='gender']:checked").val();
  console.log('gender :: ' , gender);

  let yyyy = '';
  let mm = '';
  let dd = '';

  let rec = $('#recommendedId').val();

  // birth
  if (role === 'ROLE_USER') {

    console.log('ROLE_USER :: ');
    yyyy = document.getElementById('birthY').value;
    mm = document.getElementById('birthM').value.padStart(2, '0'); // 2자리 보장
    dd = document.getElementById('birthD').value.padStart(2, '0');   // 2자리 보장
  } else {
      rec = '';
  }

  let birth='';

  if (yyyy.length === 4 && mm.length === 2 && dd.length === 2) {
    birth = `${yyyy}-${mm}-${dd}`; // "YYYY-MM-DD" 형태로 합침
    console.log("생년월일:", birth);
    // return birth;
  }



  // 모든 유효성 검사 통과 시 회원가입 요청
  if ( usernameValid && pwdValid && isPwdMatch && emailValid ) {
    // 유효성 검사 통과 후, 실제 가입 요청
    const user = {
      username: $('#username').val(),
      email: $('#email').val(),
      password: $('#password').val(),
      recommendedId: $('#recommendedId').val(),
      role: $('[name="role"]').val(),
      gender: gender,
      birth: birth
    };
    
    console.log('회원가입 객체 :: ', user);

    $.ajax({
      // url: '/api-user/signup',
      url: '/api-user/join',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(user),
      success: function(data) {
        if (data === "SUCCESS") {
          alert("회원가입 성공!");
          location.href='/user/login';
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
    }

  $.ajax({
    url: '/api-user/chkEmail',
    type: 'POST', // HTTP 메서드
    data: { email: email },


    success: function (result) {


      // 사용불가
      if (result === true) {
        console.log('이게 또 안돼?')
        messageElement.text('존재하는 이메일 입니다..');
        messageElement.css({'color': 'red', 'display': 'block'});

      }  else {
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


// // username 유효성 검사
// function usernameValidation() {
//
//   // username 초기화
//   const username = $('#username').val();
//
//   // 보여줄 메시지 초기화
//   // const messageElement = $(".invalid-feedback-username");
//   const messageElement = $("#invalid-feedback-username");
//
//   // username 길이 체크
//   const usernameChk = validationUsernameRule(username);
//
//
//   if (username == '') {
//     messageElement.text('아이디를 입력해주세요.');
//     messageElement.css({'color': 'red', 'display': 'block'});
//     return false;
//   }
//
//   if( !usernameChk ) { // true
//     messageElement.text('아이디를 확인해주세요.');
//     messageElement.css({'color': 'red', 'display': 'block'});
//     // return false;
//   } else {
//
//     ajaxValidationUsername(username, messageElement, function (isValid) {
//       if (isValid) {
//         console.log('사용 가능한 아이디입니다.');
//       } else {
//         console.log('이미 사용 중인 아이디입니다.');
//       }
//     });
//   }
//
// } // end of validation
//
// function ajaxValidationUsername(username, messageElement, callback) {
//   $.ajax({
//     url: '/api-user/chkDuplication',
//     type: 'GET', // HTTP 메서드
//     data: { username: username },
//
//     success: function (isDuplicate) {
//
//       // 사용불가
//       if (isDuplicate) {
//         messageElement.text('이미 사용 중인 아이디입니다.');
//         messageElement.css({'color': 'red', 'display': 'block'});
//         // return false;
//         callback(false);
//         // 사용가능
//       } else {
//         messageElement.text('사용 가능한 아이디입니다.');
//         messageElement.css({'color': 'green', 'display': 'block'});
//         // return true;
//         callback(true);
//       }
//     }, // end of success
//     error: function () {
//       messageElement.text('아이디 확인 중 오류가 발생했습니다.');
//       messageElement.css({'color': 'red', 'display': 'block'});
//       // return false;
//       callback(false);
//     } // end of error
//   }); // end of ajax
//
// }


function ajaxValidationUsername(username, messageElement) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: '/api-user/chkDuplication',
      type: 'GET',
      data: { username: username },

      success: function (isDuplicate) {
        if (isDuplicate) {
          messageElement.text('이미 사용 중인 아이디입니다.');
          messageElement.css({'color': 'red', 'display': 'block'});
          resolve(false);
        } else {
          messageElement.text('사용 가능한 아이디입니다.');
          messageElement.css({'color': 'green', 'display': 'block'});
          resolve(true);
        }
      },
      error: function () {
        messageElement.text('아이디 확인 중 오류가 발생했습니다.');
        messageElement.css({'color': 'red', 'display': 'block'});
        reject(false);
      }
    });
  });
}

function usernameValidation() {
  const username = $('#username').val();
  const messageElement = $("#invalid-feedback-username");

  if (username == '') {
    messageElement.text('아이디를 입력해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return;
  }

  const usernameChk = validationUsernameRule(username);
  if (!usernameChk) {
    messageElement.text('아이디를 확인해주세요.');
    messageElement.css({'color': 'red', 'display': 'block'});
    return;
  }

  return ajaxValidationUsername(username, messageElement)
    .then((isValid) => {
      if (isValid) {
        console.log('사용 가능한 아이디입니다.');
        console.log(isValid);
        return true;
      } else {
        console.log('이미 사용 중인 아이디입니다.');
        return false;
      }
    })
    .catch((error) => {
      console.error('오류 발생:', error);
      return false;
    });
}

// 이후 usernameValidation 호출 후 처리
usernameValidation().then((result) => {
  if (result) {
    console.log('최종 확인: 아이디 사용 가능');
    return true;
  } else {
    console.log('최종 확인: 아이디 사용 불가');
    return false;
  }
});

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

    uploadFile(file, "user/")
      .then(result => {
        console.log(result);
        console.log("uuid file name :: " , result.url);
        // 결과
        // https://placehere-bucket.s3.ap-northeast-2.amazonaws.com/user/202412183ef50d9b.jpg
        // console.log("uuid file path :: " , result.filePath);

        // const resultUrl = result.url;
        const resultUrl = result.filePath;
        const cvrtStr = "user/";
        const replaceFileName = resultUrl.replace(cvrtStr, '');
        
        // uuid 로 변환된 파일명만 추출
        console.log('after :: ', replaceFileName);

        $('#profileImgView').attr("src",result.url);
        // $('#profileImgView').attr("src",replaceFileName);

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
// function resetPwd() {
//
//   event.preventDefault();
//
//   const username = $('#username').val();
//   const password = $('#password').val();
//
//   console.log('username :: ' + username);
//   console.log('password :: ' + password);
//
//   const user = {
//     username : username,
//     password : password
//   }
//
//   $.ajax({
//     url: '/api-user/resetPwd',
//     type: 'POST',
//     contentType: 'application/json',
//     data: JSON.stringify(user),
//     success: function(data) {
//
//       console.log('data :: ',data);
//
//       if (data === "SUCCESS") {
//         alert("비밀번호가 변경되었습니다! 재로그인 해주세요.");
//         location.href='/';
//       } else {
//         alert("실패!");
//       }
//     },
//     error: function(xhr, status, error) {
//       console.error('Error:', error);
//       alert("비밀번호 변경 중 오류가 발생했습니다.");
//     }
//
//   });
//
//
// }

// 회원탈퇴 함수
function goodBye() {

  // username 가져오기
  const username = $(".username").text();
  const role = $(".role").val();

  console.log("username :: ", username);
  console.log("role :: ", role);

  const firstYn = confirm("탈퇴하시겠습니까?");
  console.log('result :: ', firstYn);

  const user = {
    username : username,
    role : role
  }

  // true면 실행
  if( firstYn ) {

    $.ajax({
      url: '/api-user/goodByeRsrvCnt',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(user),
      success: function(data) {

        console.log('data :: ',data);

        const status = data.status;
        const rsrvCnt = data.rsrvCnt;
        const phoneRsrvCnt = data.phoneRsrvCnt;
        const role = data.role;

        let secondYn;

        if (status === "SUCCESS") {

          // 일반회원일 때 컨펌창
          if(role === "ROLE_USER") {


            secondYn = confirm(`
                          환불처리 될 예약 건수 :: ${rsrvCnt} 입니다.
                          진행하시겠습니까?
                      `);


          // 점주 회원일 때 컨펌창
          } else {

            secondYn = confirm(`
                              환불처리 될 예약 건수
                              - 전화예약 : ${phoneRsrvCnt} ,
                              - 일반예약 :  ${rsrvCnt} 입니다.
                              진행하시겠습니까?"
                        `);

          }
          
          // 확인 누른다면
          if ( secondYn ) {

            console.log("secondYn :: " + secondYn);

            // cnt 받은 다음 시작
            $.ajax({
              url: '/api-user/goodBye',
              type: 'POST',
              contentType: 'application/json',
              data: JSON.stringify(user),
              success: function(data) {

                console.log('data :: ',data);

                if (data === "SUCCESS") {
                  alert("탈퇴 되었습니다.");
                  location.href='/';
                } else {
                  alert("탈퇴 도중 문제가 발생하였습니다.");
                }
              },
              error: function(xhr, status, error) {
                console.error('Error:', error);
              }
            }); // end of ajax2

          } else {
            alert('취소되었습니다.');
            return;
          }
        }

      },
    }); // end of ajax2

    // cnt 받은 다음 시작
    // $.ajax({
    //   url: '/api-user/goodBye',
    //   type: 'POST',
    //   contentType: 'application/json',
    //   data: JSON.stringify(user),
    //   success: function(data) {
    //
    //     console.log('data :: ',data);
    //
    //     if (data === "SUCCESS") {
    //       // alert("환불처리 될 예약 건수 :: "  );
    //       alert("탈퇴 되었습니다.");
    //       location.href='/';
    //     } else {
    //       alert("실패!");
    //     }
    //   },
    //   error: function(xhr, status, error) {
    //     console.error('Error:', error);
    //     alert("안되지롱");
    //   }
    // }); // end of ajax2

  }
  
}

// 로그인
function login() {

  event.preventDefault();

  const username = $("#username").val();
  const password = $("#password").val();

  console.log('username :: ', username);
  console.log('password :: ', password);

  const user = {
    username : username,
    password : password
  }

  $.ajax({
      url: '/api-user/login',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(user),
      success: function(data) {

        console.log('data :: ',data);

        if (data === "SUCCESS") {
          alert("로그인 완료");
          location.href='/';
        } else if(data === "INACTIVE") {
          alert("휴면계정 입니다.");
          const result = confirm("전환하시겠습니까?");

          if (result) {
            console.log('YEEEEEEEEEES');
            location.href='/user/resetPwdValidation';

          } else {
            console.log('NOOOOOOOOOOO');
            location.href='/';
          }


          location.href='/';
        } else if(data === "DELETED") {
          alert("탈퇴  회원입니다.");
          location.href='/';
        } else if(data === "ADMIN_SUCCESS") {
          alert("관리자 로그인 성공");
          location.href='http://localhost:3000/react/getUserList';
        } else {
          alert("계정 정보가 존재하지 않습니다.");
          location.href='/user/login';
        }
      },
      error: function(xhr, status, error) {
        console.error('Error:', error);
        alert("문제가 발생하였습니다.");
      }
    });

}

// 비밀번호 재설정
// 이메일과 아이디를 입력하면 확인되었습니다 얼럿 띄우기
function resetPwdValidation() {

  event.preventDefault();

  const email = $("#email").val();
  const username = $("#username").val();
  const messageElement = $("#invalid-feedback-email");
  // console.log(email);
  // console.log(username);

  const emailChk = emailCheck(email);

  if( emailChk ) { // false

    messageElement.text('');
    messageElement.css({'color': '', 'display': 'block'});
    return
  } else {
    messageElement.text('이메일 형식이 아닙니다.');
    messageElement.css({'color': 'red', 'display': 'block'});
  }

}

function resetPwdValidationSend() {

  event.preventDefault();

  const email = $("#email").val();
  const username = $("#username").val();

  console.log(email);
  console.log(username);

  const user = {
    username : username,
    email: email
  }

  $.ajax({
    url: '/api-user/resetPwdValidation',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(user),
    success: function(data) {

      console.log('data :: ',data);

      if (data === "SUCCESS") {
        alert("인증 되었습니다.");

        console.log("Before redirect");
        location.href = '/user/resetPwd';
        console.log("After redirect");
      } else {
        alert("없는 정보 입니다.");
      }
    },
    error: function(xhr, status, error) {
      console.error('Error:', error);
    }
  }); // end of ajax2


}

function resetPwd() {


  event.preventDefault();

  const password = $("#password").val();
  const passwordConfirm = $("#passwordConfirm").val();
  const username = $("#username").val();
  const messageElement = $("#invalid-feedback-pwdConfirm").val();

  console.log(password);
  console.log(passwordConfirm);
  console.log(username);

  const user = {
    username: username,
    password: password
  }

  const pwdChk = validationPwdRule(password);

  if (password !== passwordConfirm) {
    alert('비밀번호가 일치하지 않습니다.');
    return;
  } else{
    if(pwdChk) {

      console.log('11');

      $.ajax({
        url: '/api-user/resetPwd',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(user),
        success: function(data) {

          console.log('data :: ',data);

          if (data === "SUCCESS") {
            alert("변경 되었습니다.");

            console.log("Before redirect");
            location.href = '/';
            console.log("After redirect");
          } else {
            alert("에러가 발생했습니다.");
          }
        },
        error: function(xhr, status, error) {
          console.error('Error:', error);
        }
      }); // end of ajax2

    } else{
      alert('비밀번호 형식에 맞지 않습니다.');
    }

  }

}