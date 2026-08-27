const msg = document.getElementById("msg")
const formLogin = document.getElementById("formLogin")

document.getElementById("cadastrar").addEventListener("click", () => {
    window.location.href="../index.html"
})


const url = "https://academia-2rp0.onrender.com"

const endpoints = {
    login: url + "/auth/login"
}

formLogin.addEventListener("submit", async(event) => {
    event.preventDefault()

    const dados = {
        email: formLogin.email.value,
        senha: formLogin.senha.value
    }

    try{
        const response = await fetch(endpoints.login,{
            method: "POST",
            headers: {"Content-Type":"application/json"},
            body: JSON.stringify(dados)
        })

        let resultado = null
        let mensagem = ""

        if(response.headers.get("content-type")?. includes("application/json")){
            resultado = await response.json()
            mensagem = resultado.mensagem || mensagem
        } else {
            mensagem = await response.text()
        }

        if(!response.ok){
            throw new Error(mensagem || "Erro ao realizar login!")
        }

        localStorage.setItem("token",resultado.token)
        msg.textContent="Login realizado com sucesso!"
        msg.style.color="green"

        setTimeout(() =>{
            window.location.href="../telainicial/telainicial.html"
        }, 1000)
    } catch(error){
        msg.textContent=error.message
        msg.style.color="red"
    }

}
)

