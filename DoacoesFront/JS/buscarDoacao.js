document.addEventListener('DOMContentLoaded', async function () {
    const idUsuario = localStorage.getItem("idusuario");
    const usuarioTipo = localStorage.getItem("idusuario_tipo");

    if (!idUsuario) {
        alert("Usuário não está logado. Por favor, faça login.");
        window.location.href = "login.html";
        return;
    }

    if (usuarioTipo !== '2') {
        alert("Acesso restrito a colaboradores.");
        window.location.href = "dashboard.html";
        return;
    }

    const todosDoadoresLink = document.getElementById('todosDoadoresLink');
    const colaboradoresLink = document.getElementById('colaboradoresLink');
    const familiasLink = document.getElementById('familiasLink');
    const buscarLink = document.getElementById('buscarLink');

    if (todosDoadoresLink && colaboradoresLink && familiasLink && buscarLink) {
        todosDoadoresLink.classList.remove('hidden');
        colaboradoresLink.classList.remove('hidden');
        familiasLink.classList.remove('hidden');
        buscarLink.classList.remove('hidden');
    } else {
        console.warn('Um ou mais elementos de navegação não foram encontrados:', {
            todosDoadoresLink: !!todosDoadoresLink,
            colaboradoresLink: !!colaboradoresLink,
            familiasLink: !!familiasLink,
            buscarLink: !!buscarLink
        });
    }

    // Configurar logout
    const logoutButton = document.getElementById("logoutButton");
    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            localStorage.removeItem("idusuario");
            localStorage.removeItem("idusuario_tipo");
            window.location.href = "login.html";
        });
    } else {
        console.warn('Botão de logout não encontrado.');
    }

    // Carregar tipos de item
    await carregarTiposItem();

    // Configurar botão de filtrar
    const filtrarBtn = document.getElementById("filtrarBtn");
    if (filtrarBtn) {
        filtrarBtn.addEventListener("click", carregarDoacoes);
    } else {
        console.warn('Botão de filtrar não encontrado.');
    }

    // Carregar doações iniciais
    await carregarDoacoes();
});

async function carregarTiposItem() {
    try {
        const response = await fetch('http://localhost:8080/doacaoEntrada/tiposItem');
        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${await response.text()}`);
        }
        const tipos = await response.json();
        const tipoItemFilter = document.getElementById('tipoItemFilter');
        if (tipoItemFilter) {
            tipos.forEach(tipo => {
                const option = document.createElement('option');
                option.value = tipo;
                option.textContent = tipo;
                tipoItemFilter.appendChild(option);
            });
        } else {
            console.warn('Elemento tipoItemFilter não encontrado.');
        }
    } catch (error) {
        console.error('Erro ao carregar tipos de item:', error);
        alert('Erro ao carregar tipos de item. Tente novamente.');
    }
}

async function carregarDoacoes() {
    try {
        const statusFilter = document.getElementById('statusFilter');
        const tipoItemFilter = document.getElementById('tipoItemFilter');
        const cpfFilter = document.getElementById('cpfFilter');

        if (!statusFilter || !tipoItemFilter || !cpfFilter) {
            console.warn('Elementos de filtro não encontrados:', {
                statusFilter: !!statusFilter,
                tipoItemFilter: !!tipoItemFilter,
                cpfFilter: !!cpfFilter
            });
            return;
        }

        const cpf = cpfFilter.value.trim();

        if (cpf !== '') {
            // Busca só por CPF
            await buscarDoacoesPorCpf(cpf);
            return;
        }

        const status = statusFilter.value;
        const tipoItem = tipoItemFilter.value;

        const params = new URLSearchParams();
        if (status !== 'Todos') params.append('status', status);
        if (tipoItem !== 'Todos') params.append('tipoItem', tipoItem);

        const response = await fetch(`http://localhost:8080/doacaoEntrada/listarDoacao?${params.toString()}`);
        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${await response.text()}`);
        }

        const doacoes = await response.json();
        renderDoacoes(doacoes);
    } catch (error) {
        console.error('Erro ao carregar doações:', error);
        const doacoesContainer = document.getElementById('doacoesContainer');
        if (doacoesContainer) {
            doacoesContainer.innerHTML = `
                <p class="text-red-500 text-center">Erro ao carregar doações: ${error.message}. Tente novamente.</p>
            `;
        }
    }
}


async function deletarDoacao(iddoacao_entrada) {
    const mensagem = document.getElementById('mensagem');
    if (!mensagem) {
        console.warn('Elemento mensagem não encontrado.');
        return;
    }
    const idUsuario = localStorage.getItem("idusuario");
    try {
        const response = await fetch(`http://localhost:8080/doacaoEntrada/excluir/${iddoacao_entrada}`, {
            method: 'DELETE',
            headers: { 
                'Content-Type': 'application/json',
                'idusuario': idUsuario
            }
        });
        let message;
        if (response.headers.get('Content-Type')?.includes('application/json')) {
            const data = await response.json();
            message = data.message || 'Erro desconhecido.';
        } else {
            message = await response.text();
        }
        mensagem.classList.remove('hidden', 'bg-red-100', 'text-red-700', 'bg-green-100', 'text-green-700');
        if (response.ok) {
            mensagem.classList.add('bg-green-100', 'text-green-700');
            mensagem.textContent = message || 'Doação excluída com sucesso!';
            await carregarDoacoes();
        } else {
            mensagem.classList.add('bg-red-100', 'text-red-700');
            mensagem.textContent = message || `Erro ao excluir doação (status ${response.status}).`;
        }
    } catch (error) {
        console.error('Erro ao excluir doação:', error);
        mensagem.classList.remove('hidden', 'bg-green-100', 'text-green-700');
        mensagem.classList.add('bg-red-100', 'text-red-700');
        mensagem.textContent = 'Erro ao excluir doação: ' + error.message;
    }
}

async function buscarDoacoesPorCpf(cpf) {
    try {
        const response = await fetch(`http://localhost:8080/doacaoEntrada/buscarPorCpf/${cpf}`);
        if (!response.ok) throw new Error(`Erro ${response.status}: ${await response.text()}`);
        const doacoes = await response.json();
        console.log(doacoes);
        renderDoacoes(doacoes);
    } catch (error) {
        console.error("Erro ao buscar por CPF:", error);
    }
}



function renderDoacoes(doacoes) {
    const tbody = document.getElementById('doacoesTabela');
    if (!tbody) {
        console.warn('Elemento doacoesTabela não encontrado.');
        return;
    }
    tbody.innerHTML = '';

    if (doacoes.length === 0) {
        tbody.innerHTML = `
            <tr><td colspan="8" class="text-center p-4 text-gray-500">Nenhuma doação encontrada.</td></tr>
        `;
        return;
    }

    const hoje = new Date();
    hoje.setHours(hoje.getHours() - 3);

    for (const doacao of doacoes) {
        const dataAbertura = doacao.data_abertura ? new Date(doacao.data_abertura).toLocaleDateString('pt-BR') : 'Não informado';
        const dataFim = doacao.data_fim ? new Date(doacao.data_fim).toLocaleDateString('pt-BR') : 'Não informado';
        const isVencida = doacao.data_fim && new Date(doacao.data_fim) < hoje;
        const statusMap = {
            'P': { text: 'Pendente', color: 'bg-yellow-100 text-yellow-800' },
            'A': { text: 'Aprovado', color: 'bg-green-100 text-green-800' },
            'C': { text: 'Concluído', color: 'bg-blue-100 text-blue-800' }
        };
        const status = statusMap[doacao.status] || { text: 'Desconhecido', color: 'bg-gray-100 text-gray-800' };

        const itensHtml = doacao.itens && doacao.itens.length > 0
            ? doacao.itens.map(item => {
                const nomeItem = item.nomeItemTipo || 'Desconhecido';
                return `<li>${nomeItem}: ${item.valor_qtde_doacao_entrada_item} unidade(s) - ${item.descricao || 'Sem descrição'}</li>`;
              }).join('')
            : 'Nenhum item';

        const row = `
            <tr class="border-t">
                <td class="p-2">${doacao.iddoacao_entrada}</td>
                <td class="p-2">${dataAbertura}</td>
                <td class="p-2 ${isVencida ? 'text-red-500' : ''}">${dataFim}${isVencida ? ' (Vencida)' : ''}</td>
                <td class="p-2"><span class="${status.color} px-2 py-1 rounded">${status.text}</span></td>
                <td class="p-2">${doacao.endereco_busca || 'Não informado'}</td>
                <td class="p-2"><ul>${itensHtml}</ul></td>
                <td class="p-2">${doacao.observacao || 'Nenhuma'}</td>
                <td class="p-2">
                    <button class="excluirBtn bg-red-600 text-white px-2 py-1 rounded hover:bg-red-700" data-id="${doacao.iddoacao_entrada}">Excluir</button>
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    }

    document.querySelectorAll('.excluirBtn').forEach(button => {
        button.addEventListener('click', function () {
            const iddoacao_entrada = this.getAttribute('data-id');
            if (confirm('Tem certeza que deseja excluir esta doação?')) {
                deletarDoacao(iddoacao_entrada);
            }
        });
    });
}