document.addEventListener('DOMContentLoaded', async function () {
    const idUsuario = localStorage.getItem("idusuario");
    if (!idUsuario) {
        alert("Usuário não está logado. Por favor, faça login.");
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/doador/usuario/${idUsuario}`);
        if (response.status === 404) {
            alert("Usuário não é doador. Por favor, cadastre-se como doador.");
            localStorage.setItem('voltarParaCriarDoacao', 'true');
            window.location.href = "cadastroDoador.html";
            return;
        } else if (!response.ok) {
            throw new Error(`Erro ${response.status}`);
        }
    } catch (error) {
        console.error("Erro ao verificar doador:", error);
        alert("Erro ao conectar com o servidor.");
        window.location.href = "dashboard.html";
        return;
    }

    document.getElementById("formDoacao").addEventListener("submit", async function (e) {
        e.preventDefault();

        const form = e.target;
        const formData = new FormData(form);

        const doacao = {
            idusuario: parseInt(idUsuario),
            dataentrada: formData.get("dataentrada"),
            tipoentrega: formData.get("tipoentrega") || "",
            datafim: formData.get("datafim") || "",
            datanotificacao: formData.get("datanotificacao") || "",
            endereco: formData.get("endereco") || "",
            bairro: formData.get("bairro") || "",
            cidade: formData.get("cidade") || "",
            estado: formData.get("estado") || "",
            status: formData.get("status"),
            itens: []
        };

        const linhas = document.querySelectorAll("#tabelaItens tbody tr");
        linhas.forEach(linha => {
            const idItem = linha.querySelector('select[name="itens[].idItem"]').value;
            const descricao = linha.querySelector('input[name="itens[].descricao"]').value;
            const quantidade = linha.querySelector('input[name="itens[].valorQtdeDoacaoEntradaItem"]').value;

            if (idItem && quantidade) {
                doacao.itens.push({
                    idItem: parseInt(idItem),
                    descricao: descricao || "",
                    valorQtdeDoacaoEntradaItem: parseFloat(quantidade)
                });
            }
        });

        if (!doacao.dataentrada || !doacao.status || doacao.itens.length === 0) {
            alert("Preencha a data de abertura, status e adicione pelo menos um item.");
            return;
        }

        try {
            console.log("Enviando:", JSON.stringify(doacao));
            const response = await fetch("http://localhost:8080/doacaoEntrada/inserir", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(doacao)
            });

            if (response.ok) {
                const resultado = await response.json();
                alert(resultado.message);
                form.reset();
                document.querySelector("#tabelaItens tbody").innerHTML = "";
                window.location.href = "dashboard.html";
            } else {
                const erro = await response.text();
                alert("Erro ao registrar doação: " + erro);
            }
        } catch (error) {
            console.error("Erro ao enviar:", error);
            alert("Erro na requisição: " + error.message);
        }
    });
});