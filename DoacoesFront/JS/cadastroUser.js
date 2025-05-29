document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('cadastroForm');

  form.addEventListener('submit', function (event) {
    event.preventDefault();

    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value;

    if (!email || !senha) {
      alert('Por favor, preencha e-mail e senha.');
      return;
    }

    fetch('http://localhost:8080/usuario/criar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, senha }),
    })
      .then(async response => {
        const data = await response.json();

        if (response.status === 201) {
          alert('Usuário criado com sucesso! Faça login agora.');
          window.location.href = 'login.html';
        } else if (response.status === 409) {
          alert(data.message || 'Usuário já existe!');
        } else {
          alert(data.message || 'Erro ao criar usuário.');
        }
      })
      .catch(error => {
        console.error('Erro ao criar usuário:', error);
        alert('Erro na comunicação com o servidor. Tente novamente.');
      });
  });
});
