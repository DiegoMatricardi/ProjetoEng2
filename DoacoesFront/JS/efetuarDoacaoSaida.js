document.addEventListener('DOMContentLoaded', () => {
    const mensagemDiv = document.getElementById('mensagem');
    const beneficiariosList = document.getElementById('beneficiariosList');
    const doacoesEntradaList = document.getElementById('doacoesEntradaList');
    const efetuarButton = document.getElementById('efetuarButton');
    const logoutButton = document.getElementById('logoutButton');

    let selectedBeneficiario = null;
    let selectedDoacaoEntrada = null;

    // Função para exibir mensagem
    const showMessage = (message, isError = false) => {
        mensagemDiv.textContent = message;
        mensagemDiv.classList.remove('hidden', 'bg-green-100', 'text-green-700', 'bg-red-100', 'text-red-700');
        mensagemDiv.classList.add(isError ? 'bg-red-100' : 'bg-green-100', isError ? 'text-red-700' : 'text-green-700');
        setTimeout(() => mensagemDiv.classList.add('hidden'), 5000);
    };

    const getStatusLabel = (status) => {
        switch (status) {
            case 'P': return 'Pendente';
            case 'A': return 'Aprovada';
            case 'C': return 'Cancelada';
            default: return 'Desconhecido';
        }
    };

    // Carregar beneficiários
    const loadBeneficiarios = async () => {
        try {
            const response = await fetch('http://localhost:8080/beneficiarios/listar');
            if (!response.ok) throw new Error('Erro ao carregar beneficiários: ' + response.statusText);
            const beneficiarios = await response.json();
            beneficiariosList.innerHTML = beneficiarios.length === 0 
                ? '<p class="p-2 text-gray-500">Nenhum beneficiário encontrado.</p>'
                : beneficiarios.map(b => `
                    <div class="flex items-center p-2 border-b hover:bg-gray-50">
                        <input type="radio" name="beneficiario" value="${b.idbeneficiario}" class="mr-2">
                        <span>${b.nome} - ${b.endereco}, ${b.bairro}, ${b.cidade}${b.complemento ? ', ' + b.complemento : ''}</span>
                    </div>
                `).join('');
            document.querySelectorAll('input[name="beneficiario"]').forEach(radio => {
                radio.addEventListener('change', () => {
                    selectedBeneficiario = parseInt(radio.value);
                    updateButtonState();
                });
            });
        } catch (error) {
            showMessage(error.message, true);
        }
    };

    // Carregar doações de entrada pendentes
    const loadDoacoesEntrada = async () => {
        try {
            const response = await fetch('http://localhost:8080/doacaoEntrada/listarDoacao?status=P');
            if (!response.ok) throw new Error('Erro ao carregar doações de entrada: ' + response.statusText);
            const doacoes = await response.json();
            doacoesEntradaList.innerHTML = doacoes.length === 0 
                ? '<p class="p-2 text-gray-500">Nenhuma doação de entrada pendente encontrada.</p>'
                : doacoes.map(d => `
                    <div class="flex items-start p-2 border-b hover:bg-gray-50">
                        <input type="radio" name="doacaoEntrada" value="${d.iddoacao_entrada}" class="mr-2 mt-1">
                        <div>
                            <p><strong>Doação ID:</strong> ${d.iddoacao_entrada} - <strong>Status:</strong> ${getStatusLabel(d.status)} - <strong>Data:</strong> ${new Date(d.data_abertura).toLocaleDateString('pt-BR')}</p>
                            <p><strong>Endereço:</strong> ${d.endereco_busca || '-'}</p>
                            <ul class="list-disc ml-5">
                                ${d.itens.map(i => `
                                    <li>${i.nomeItemTipo}: ${i.valor_qtde_doacao_entrada_item} unidades - ${i.descricao}</li>
                                `).join('')}
                            </ul>
                        </div>
                    </div>
                `).join('');
            document.querySelectorAll('input[name="doacaoEntrada"]').forEach(radio => {
                radio.addEventListener('change', () => {
                    selectedDoacaoEntrada = parseInt(radio.value);
                    updateButtonState();
                });
            });
        } catch (error) {
            showMessage(error.message, true);
        }
    };

    // Atualizar estado do botão
    const updateButtonState = () => {
        efetuarButton.disabled = !(selectedBeneficiario && selectedDoacaoEntrada);
    };

    // Efetuar doação de saída
    efetuarButton.addEventListener('click', async () => {
        if (!selectedBeneficiario || !selectedDoacaoEntrada) {
            showMessage('Selecione um beneficiário e uma doação de entrada.', true);
            return;
        }
        try {
            const idUsuario = localStorage.getItem('idusuario') || '1'; // Substitua por autenticação real
            const payload = {
                idbeneficiario: selectedBeneficiario,
                iddoacaoEntrada: selectedDoacaoEntrada
            };
            const response = await fetch('http://localhost:8080/doacaoSaida/inserir', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'idusuario': idUsuario
                },
                body: JSON.stringify(payload)
            });
            if (!response.ok) {
                const errorData = await response.text();
                throw new Error(errorData || 'Erro ao efetuar doação de saída');
            }
            const data = await response.json();
            showMessage(data.message);
            selectedBeneficiario = null;
            selectedDoacaoEntrada = null;
            efetuarButton.disabled = true;
            loadBeneficiarios();
            loadDoacoesEntrada();
        } catch (error) {
            showMessage(error.message, true);
        }
    });

    // Logout
    logoutButton.addEventListener('click', () => {
        localStorage.removeItem('idusuario');
        window.location.href = '/login.html';
    });

    loadBeneficiarios();
    loadDoacoesEntrada();
});