document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('cadastroForm');
  
    form.addEventListener('submit', function (event) {
      event.preventDefault(); // Impede o envio do formulário para fins de validação
  
      // Obtenha os dados do formulário
      const nome = document.getElementById('nome').value;
      const email = document.getElementById('email').value;
      const cpf = document.getElementById('cpf').value;
      const telefone = document.getElementById('telefone').value;
      const idusuario = document.getElementById('idusuario').value;
  

      console.log('Dados do Doador:', {
        nome, email, cpf, telefone, idusuario
      });
  

      form.reset();
    });
  });
  