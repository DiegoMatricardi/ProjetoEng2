document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('loginForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // Impede o envio padrão do formulário

        const email = document.getElementById('email').value;
        const senha = document.getElementById('senha').value;

        fetch('http://localhost:8080/usuario/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
                senha: senha,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Login failed: ' + response.status);
            }
            return response.json(); 
        })
        .then((data) => {
            if (data.message === 'Login bem-sucedido!') {
                console.log('Login realizado com sucesso!', data);

        
                localStorage.setItem('idusuario_tipo',data.idusuario_tipo);
                localStorage.setItem('idusuario', String(data.idusuario)); 

                window.location.href = 'dashboard.html';
            } else {
                alert(data.message || 'Email ou senha inválidos, ou usuário inativo.');
            }
        })
        .catch((error) => {
            console.error('Erro ao realizar login:', error);
            alert('Erro ao realizar login. Verifique a conexão ou tente novamente.');
        });
    });
});
