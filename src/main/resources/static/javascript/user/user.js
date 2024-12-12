

function pwdValidation() {

  console.log("비밀번호 재설정 validation ");

  var email = $("#email").val();
  var username = $("#username").val();

  console.log(email);
  console.log(username);


  if (email == '' || username == '') {
    alert("필수값을 입력하세요.");
    return;
  }


  $.ajax({
    type: "post",
    async: true,
    url: "http://localhost:8080/user/resetPwdValidation",
    dataType: "text",
    data: {
      "username": username,
      "email": email
    },

    success: function (data, textStatus) {
      <!-- 서버에서 전송된 결과에 따라 메시지를 표시 -->
      if (data == true) {

        alert("test 성공");
        location.href='user/resetPwd';

      } else {

        alert(" 정보가 일치하지 않습니다. ");
        // $('#message').text("사용할 수 없는 ID입니다.");
      }
    },
    error:function(data,textStatus) {
      alert("에러가 발생했습니다.");
    }

  }); // end ajax

}


function changePwd() {

  console.log("비밀번호 재설정 ");

  // var _id = $("#t_id").val(); <!-- 텍스트 박스에 입력한 ID를 가져옴 -->
  var email = $("#email").val(); <!-- 텍스트 박스에 입력한 ID를 가져옴 -->
  var username = $("#username").val();

  console.log(email);
  console.log(username);


  if (email == '' || username == '') {
    alert("필수값을 입력하세요.");
    return;
  }

  $.ajax({
    type: "post",
    async: true,
    url: "http://localhost:8080/user/resetPwdValidation",
    dataType: "text",
    data: { id: _id}, <!-- ID를 서블릿으로 전송 -->

    success: function (data, textStatus) {
      <!-- 서버에서 전송된 결과에 따라 메시지를 표시 -->
      if (data == true) {

        alet("test 성공");
        location.href='user/resetPwd';

      } else {

        alert(" 정보가 일치하지 않습니다. ");
        // $('#message').text("사용할 수 없는 ID입니다.");
      }
    },
    error:function(data,textStatus) {
      alert("에러가 발생했습니다.");
    }

  }); // end ajax

}

function fn_process() {

  console.log("비밀번호 재설정 validation ");

  var _id = $("#t_id").val(); <!-- 텍스트 박스에 입력한 ID를 가져옴 -->
  if (_id == '') {
    alert("ID를 입력하세요."); <!-- ID를 입력하지 않으면 오류 메시지를 출력 -->
    return;
  }
  $.ajax({
    type: "post",
    async: true,
    url: "http://localhost:8090/pro16/mem",
    dataType: "text",
    data: { id: _id}, <!-- ID를 서블릿으로 전송 -->
    success: function (data, textStatus) {

      <!-- 서버에서 전송된 결과에 따라 메시지를 표시 -->
      if (data == 'usable') {
        $('#message').text("사용할 수 있는 ID입니다.");
        $('#btn_duplicate').prop("disabled", true); <!-- 사용할 수 있는 ID면 버튼을 비활성화 시킴 -->
      } else {
        $('#message').text("사용할 수 없는 ID입니다.");

      }
    },
    error:function(data,textStatus) {
      alert("에러가 발생했습니다.");
    },
    complete:function(data,textStatus) {
      // alert("작업을 완료했습니다.");
    }
  }); // end ajax

}

// username 유효성 검사
function validation() {

  // username 초기화
  const username = $('#username').val();  // jQuery로 username 값을 가져옴

  // 보여줄 메시지 초기화
  const messageElement = $(".invalid-feedback-username");

  console.log("username", username);

  // messageElement가 존재하는지 확인
  if (messageElement.length === 0) {
    console.error('invalid-feedback 요소가 존재하지 않습니다.');
    return;
  }

  // username 입력 안했을 때
  if (!username) {
    messageElement.text('');
    messageElement.css('color', '');
    return;
  }

  // username 길이 체크
  const usernameChk = validationUsername(username);

  if( usernameChk ) { // true
    console.log("input username :: ", username);
    console.log(" result :: ", usernameChk)
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
    },
    error: function () {
      messageElement.text('아이디 확인 중 오류가 발생했습니다.');
      messageElement.css({'color': 'red', 'display': 'block'});
    }
  });
} // end of validation

function validationUsername(username) {
  return /^[a-zA-Z](?=.*[a-zA-Z])(?=.*[0-9]).{4,12}$/g.test(username);
}
//
// // 한글과 영어조합 체크
// function onlyNumberAndEnglish(username) {
//   return /^[A-Za-z0-9][A-Za-z0-9]*$/.test(username);
// }