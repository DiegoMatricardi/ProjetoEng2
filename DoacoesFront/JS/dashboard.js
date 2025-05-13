document.addEventListener('DOMContentLoaded', function () {
    const idUsuarioTipo = localStorage.getItem('idusuario_tipo');
    const usuarioId = localStorage.getItem('idusuario');

    // Esconde links para doador
    if (idUsuarioTipo === '2') {
        const buscarLink = document.getElementById('buscarLink');
        const familiasLink = document.getElementById('familiasLink');
        if (buscarLink) buscarLink.style.display = 'none';
        if (familiasLink) familiasLink.style.display = 'none';
    }

    // Logout
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function () {
            localStorage.clear();
            window.location.href = '/login.html'; 
        });
    }

    const voltar = localStorage.getItem("voltarParaCriarDoacao");
    if (voltar === "true") {
        localStorage.removeItem("voltarParaCriarDoacao");
        window.location.href = "criarDoacao.html";
    }

    // Botão "Criar Nova Doação"
    const criarDoacaoBtn = document.querySelector("a[href='#']");
    if (criarDoacaoBtn) {
        criarDoacaoBtn.addEventListener("click", function (event) {
            event.preventDefault();

            if (!usuarioId) {
                alert("Usuário não logado.");
                window.location.href = "login.html";
                return;
            }

            fetch(`http://localhost:8080/api/doadores/usuario/${usuarioId}`)
                .then(response => {
                    if (response.status === 404) {
                        localStorage.setItem("voltarParaCriarDoacao", "true");
                        window.location.href = "cadastroDoador.html";
                    } else if (response.ok) {
                        window.location.href = "criarDoacao.html";
                    } else {
                        alert("Erro ao verificar doador.");
                    }
                })
                .catch(error => {
                    console.error("Erro na verificação:", error);
                    alert("Erro ao conectar com o servidor.");
                });
        });
    }
});
