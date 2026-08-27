const msg = document.getElementById("msg");
const form = document.getElementById("formCadastro");

const url = "https://academia-2rp0.onrender.com"

document.getElementById("possuiConta").addEventListener("click", () => {
    window.location.href="../login/login.html"
})

const endpoints = {
    cadastroUser: url + "/auth/cadastro"
}


formCadastro.addEventListener("submit", async(event) => {
    event.preventDefault();

//funcion
const dados = {
    nome: formCadastro.nome.value,
    email: formCadastro.email.value,
    senha: formCadastro.senha.value
}

try {
    const response = await fetch(endpoints.cadastroUser, {
       method: "POST",
       headers: {
        "Content-Type": "application/json"},
    body: JSON.stringify(dados)
    })
    if(!response.ok){
        const erro = await response.text()
        throw new Error(erro)
    }

    msg.textContent="Cadastro realizado com sucesso!";
    msg.style.color="green";

    setTimeout(() => {
        window.location.href = "../login/login.html"
    }, 1500);

    form.reset();
} catch (error){
    msg.textContent=error.message;
    msg.style.color="red";
}
})