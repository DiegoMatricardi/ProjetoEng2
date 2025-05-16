document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('cadastroForm');
  const cpfInput = document.getElementById('cpf');

  cpfInput.addEventListener('input', function () {
    let value = cpfInput.value.replace(/\D/g, ''); 
    if (value.length > 11) value = value.slice(0, 11); 

    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

    cpfInput.value = value;
  });

  form.addEventListener('submit', function (event) {
    event.preventDefault();

    const nome = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const cpf = document.getElementById('cpf').value.replace(/\D/g, ''); 
    const telefone = document.getElementById('telefone').value;

    const idusuario = localStorage.getItem('idusuario');

    if (!idusuario) {
      alert('Usuário não está logado!');
      return;
    }

    console.log('ID do usuário que será enviado:', idusuario);
    console.log(typeof(idusuario));

    fetch('http://localhost:8080/doador/cadastrar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nome: nome,
        email: email,
        cpf: cpf,
        telefone: telefone,
        idUsuario: Number(idusuario)
      }),
    })
      .then(response => {
        if (!response.ok) {
          return response.text().then(text => {
            throw new Error('Falha no cadastro: ' + response.status + ' - ' + text);
          });
        }
        return response.text();
      })
      .then(text => {
        console.log('Cadastro realizado com sucesso:', text);
        alert('Cadastro concluído!');
        form.reset();
      })
      .catch(error => {
        console.error('Erro ao cadastrar:', error);
        alert('Erro ao cadastrar. Verifique os dados e tente novamente.\n' + error.message);
      });
  });
});
