document.addEventListener('DOMContentLoaded', async function () {
    const idUsuario = localStorage.getItem("idusuario");
    const usuarioTipo = localStorage.getItem('idusuario_tipo');

    if (!idUsuario) {
        alert("Usuário não está logado. Por favor, faça login.");
        window.location.href = "login.html";
        return;
    }

    if (usuarioTipo === '2') {
        const todosDoadoresLink = document.getElementById('todosDoadoresLink');
        const colaboradoresLink = document.getElementById('colaboradoresLink');
        const familiasLink = document.getElementById('familiasLink');
        const buscarLink = document.getElementById('buscarLink');
        const efetuarLink = document.getElementById('efetuarLink');
        const estoqueLink = document.getElementById('estoqueLink');
    if (todosDoadoresLink) {
      todosDoadoresLink.classList.remove('hidden');
      colaboradoresLink.classList.remove('hidden');
      familiasLink.classList.remove('hidden');
      buscarLink.classList.remove('hidden');
      efetuarLink.classList.remove('hidden');
      estoqueLink.classList.remove('hidden');
    }
  }

    
    document.getElementById("criarDoacaoBtn").addEventListener("click", function (e) {
        e.preventDefault();
        window.location.href = "criarDoacao.html";
    });

    
    document.getElementById("logoutButton").addEventListener("click", function () {
        localStorage.removeItem("idusuario");
        localStorage.removeItem("idusuario_tipo");
        window.location.href = "/login.html";
    });

    
    try {
        const response = await fetch(`http://localhost:8080/doacaoEntrada/listarPorIdUsuario/${idUsuario}`);
        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${await response.text()}`);
        }
        const doacoes = await response.json();
        renderDoacoes(doacoes);
    } catch (error) {
        console.error("Erro ao carregar doações:", error);
        alert("Erro ao carregar doações: " + error.message);
        document.getElementById("cardsContainer").innerHTML = `
            <p class="text-red-500 text-center">Erro ao carregar doações. Tente novamente mais tarde.</p>
        `;
    }
});

function renderDoacoes(doacoes) {
    const container = document.getElementById("cardsContainer");
    container.innerHTML = "";

    if (doacoes.length === 0) {
        container.innerHTML = `
            <p class="text-gray-500 text-center col-span-full">Nenhuma doação encontrada.</p>
        `;
        return;
    }

    doacoes.forEach(doacao => {
        const statusMap = {
            "P": { text: "Pendente", color: "bg-yellow-100 text-yellow-800" },
            "A": { text: "Aprovado", color: "bg-green-100 text-green-800" },
            "C": { text: "Concluído", color: "bg-blue-100 text-blue-800" }
        };
        const status = statusMap[doacao.status] || { text: "Desconhecido", color: "bg-gray-100 text-gray-800" };

        const dataAbertura = new Date(doacao.dataAbertura).toLocaleDateString("pt-BR");
        const itensHtml = doacao.itens.map(item => `
            <li>${item.nomeItemTipo}: ${item.valorQtdeDoacaoEntradaItem} unidade(s) - ${item.descricao}</li>
        `).join("");

        const card = `
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-xl font-semibold text-gray-800 mb-2">Doação #${doacao.iddoacaoEntrada}</h2>
                <p class="text-gray-600 mb-1"><strong>Data de Abertura:</strong> ${dataAbertura}</p>
                <p class="text-gray-600 mb-1"><strong>Status:</strong> <span class="${status.color} px-2 py-1 rounded">${status.text}</span></p>
                <p class="text-gray-600 mb-1"><strong>Endereço de Busca:</strong> ${doacao.enderecoBusca || "Não informado"}</p>
                <p class="text-gray-600 mb-2"><strong>Itens Doados:</strong></p>
                <ul class="list-disc pl-5 text-gray-600">${itensHtml}</ul>
            </div>
        `;
        container.insertAdjacentHTML("beforeend", card);
    });
}