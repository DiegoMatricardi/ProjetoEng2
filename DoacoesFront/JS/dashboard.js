document.addEventListener('DOMContentLoaded', function () {
  const usuarioId = localStorage.getItem('idusuario'); 

  console.log('ID do usuário:', usuarioId);

  const criarDoacaoBtn = document.getElementById('criarDoacaoBtn');
  if (criarDoacaoBtn) {
    criarDoacaoBtn.addEventListener('click', function (event) {
      event.preventDefault();

      console.log('usuarioId para requisição:', usuarioId);
      fetch(`http://localhost:8080/doador/usuario/${usuarioId}`)
        .then(response => {
          if (response.status === 404) {
            console.log('Usuário não é doador, redirecionando para cadastro.');
            localStorage.setItem('voltarParaCriarDoacao', 'true');
            window.location.href = 'cadastroDoador.html';
          } else if (response.ok) {
            console.log('Usuário é doador, redirecionando para criar doação.');
            window.location.href = '/criarDoacao.html';
          } else {
            console.error('Erro na resposta do servidor:', response.status);
            alert('Erro ao verificar se o usuário é um doador.');
          }
        })
        .catch(error => {
          console.error('Erro na requisição:', error);
          alert('Erro ao conectar com o servidor.');
        });
    });
  }
});
