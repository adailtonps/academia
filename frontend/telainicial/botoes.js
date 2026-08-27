import {
    mostrarSecao,
    listarUser,
    desativarUser,
    ativarUser,
    deletarUser,
    criarAdmin,
    atualizarUser,
    fazerCheckin,
    fazerCheckout,
    getRoleFromToken,
    listarCheckins,
    listarCheckinsTodos,
    minhaConta
} from "./functions.js"

document.addEventListener("DOMContentLoaded", () =>{

const role = getRoleFromToken()
if(role !== "ROLE_ADMIN"){
    const botoesAdmin = [
        "btnListarUser",
        "btnDeletarUser",
        "btnListarCheckinsTodos",
        "btnCriarAdmin",
    ]

    botoesAdmin.forEach(id =>{
        const btn = document.getElementById(id)
        if(btn) btn.style.display="none"
    })
}


if(role !== "ROLE_USER"){
    const buttonAdmin = [
        "btnFazerCheckin",
        "btnListarCheckins",
        "btnMinhaConta"
    ]

    buttonAdmin.forEach(id =>{
        const btn = document.getElementById(id)
        if(btn) btn.style.display="none"
    })
}
})

export function pegarBotoes(){
    const btnListar = document.getElementById("btnListarUser")
    if(btnListar){
        btnListar.addEventListener("click", () =>{
            mostrarSecao("listarUser")
            listarUser()
        })
    }

const btnDesativar = document.getElementById("btnDesativarAtivarUser")
if(btnDesativar){
    btnDesativar.addEventListener("click", () =>{
        mostrarSecao("desativarAtivarUser")
    })
}

const btnApagar = document.getElementById("btnDeletarUser")
if(btnApagar){
    btnApagar.addEventListener("click", () =>{
        mostrarSecao("deletarUser")
    })
}

const btnCriarAdmin = document.getElementById("btnCriarAdmin")
if(btnCriarAdmin){
    btnCriarAdmin.addEventListener("click", () =>{
        mostrarSecao("criarAdmin")
    })
}

const btnListarCheckins = document.getElementById("btnListarCheckins")
if(btnListarCheckins){
    btnListarCheckins.addEventListener("click", ()=>{
        mostrarSecao("listarCheckins")
        listarCheckins()
    })
}

const btnListarCheckinsTodos = document.getElementById("btnListarCheckinsTodos")
if(btnListarCheckinsTodos){
    btnListarCheckinsTodos.addEventListener("click", () =>{
        mostrarSecao("listarCheckinsTodos")
        listarCheckinsTodos()
    })
}

const btnFazerCheckin = document.getElementById("btnFazerCheckin")
if(btnFazerCheckin){
    btnFazerCheckin.addEventListener("click", () =>{
        mostrarSecao("fazerCheckin")
    })
}

const btnMinhaConta = document.getElementById("btnMinhaConta")
if(btnMinhaConta){
    btnMinhaConta.addEventListener("click", () =>{
        mostrarSecao("minhaConta")
        minhaConta()
    })
}

const btnAtualizarAdmin = document.getElementById("btnAtualizarAdmin")
if(btnAtualizarAdmin){
    btnAtualizarAdmin.addEventListener("click", () =>{
        mostrarSecao("atualizarUser")
    })
}

const btnSairConta = document.getElementById("btnSairDaConta")
if(btnSairConta){
    btnSairConta.addEventListener("click", ()=>{
        window.location.href="../login/login.html"
    })
}

document.getElementById("confirmarDesativar").addEventListener("click", desativarUser)
document.getElementById("confirmarCheckin").addEventListener("click",fazerCheckin)
document.getElementById("confirmarCheckout").addEventListener("click",fazerCheckout)
document.getElementById("confirmarAtivar").addEventListener("click", ativarUser)
document.getElementById("confirmarDelete").addEventListener("click", deletarUser)
document.getElementById("confirmarCriarAdmin").addEventListener("click", criarAdmin)
document.getElementById("confirmarAtualizacao").addEventListener("click", atualizarUser)
}