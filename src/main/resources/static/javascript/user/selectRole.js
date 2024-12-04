// 역할을 저장할 변수
let selectedRole = null;

function selectOption(button, role) {
  // 모든 버튼에서 'active' 클래스 제거
  const buttons = document.querySelectorAll('.button');
  buttons.forEach(btn => {
    btn.classList.remove('active');
  });

  // 클릭된 버튼에 'active' 클래스 추가
  button.classList.add('active');

  selectedRole = role;

  console.log("role 확인 :: " + role);

  document.getElementById('selectedRole').value = selectedRole;

}

// '다음' 버튼 클릭 시 폼이 제출될 때 역할을 전송
document.getElementById('roleForm').onsubmit = function(event) {
  // 역할이 선택되지 않았다면 폼 제출을 막고 알림
  if (!selectedRole) {
    event.preventDefault();
    alert("역할을 선택해주세요.");
  }
};