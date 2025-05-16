document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('editarContaForm');
  const editarBtn = document.getElementById('editarBtn');
  const salvarBtn = document.getElementById('salvarBtn');

  const nomeInput = document.getElementById('nome');
  const emailInput = document.getElementById('email');
  const cpfInput = document.getElementById('cpf');
  const telefoneInput = document.getElementById('telefone');

  const idusuario = localStorage.getItem('idusuario');

  if (!idusuario) {
    alert('Usuário não logado!');
    window.location.href = 'login.html';
    return;
  }


  fetch(`http://localhost:8080/doador/usuario/${idusuario}`)
    .then(response => {
      if (!response.ok) throw new Error('Erro ao buscar dados.');
      return response.json();
    })
    .then(doador => {
      nomeInput.value = doador.nome;
      emailInput.value = doador.email;
      cpfInput.value = doador.cpf;
      telefoneInput.value = doador.telefone;
    })
    .catch(error => {
      console.error(error);
      alert('Erro ao carregar dados do doador.');
    });

  editarBtn.addEventListener('click', () => {
    nomeInput.disabled = false;
    emailInput.disabled = false;
    cpfInput.disabled = false;
    telefoneInput.disabled = false;

    nomeInput.classList.remove('bg-gray-100');
    emailInput.classList.remove('bg-gray-100');
    cpfInput.classList.remove('bg-gray-100');
    telefoneInput.classList.remove('bg-gray-100');

    editarBtn.classList.add('hidden');
    salvarBtn.classList.remove('hidden');
  });

  form.addEventListener('submit', function (e) {
    e.preventDefault();

    const dadosAtualizados = {
      nome: nomeInput.value,
      email: emailInput.value,
      cpf: cpfInput.value,
      telefone: telefoneInput.value,
      idUsuario: Number(idusuario)
    };

    fetch(`http://localhost:8080/doador/atualizar`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(dadosAtualizados),
    })
      .then(response => {
        if (!response.ok) throw new Error('Erro ao atualizar dados.');
        return response.text();
      })
      .then(msg => {
        alert('Dados atualizados com sucesso!');

        nomeInput.disabled = true;
        emailInput.disabled = true;
        cpfInput.disabled = true;
        telefoneInput.disabled = true;

        nomeInput.classList.add('bg-gray-100');
        emailInput.classList.add('bg-gray-100');
        cpfInput.classList.add('bg-gray-100');
        telefoneInput.classList.add('bg-gray-100');

        salvarBtn.classList.add('hidden');
        editarBtn.classList.remove('hidden');
      })
      .catch(error => {
        console.error(error);
        alert('Erro ao atualizar. Tente novamente.');
      });
  });
});
