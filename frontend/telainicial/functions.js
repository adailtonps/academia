import { endpoints} from "./endpoints.js";

const token = localStorage.getItem("token")
if(!token){
    window.location.href="../login/login.html"
}



//verificar se o token é invalido
export async function handleResponse(response) {
    if(response.status === 401){
        alert("Sua seção expirou! Faça login novamente")
        localStorage.removeItem("token")
        setTimeout(() =>{
            window.location.href="../login/login.html"
        }, 1000)
       throw new Error("Sessão expirada!")
    }

    let data = null

    try{
        data = await response.json()
    } catch (e){

    }

    if(!response.ok){
        throw new Error(data?.mensagem||"Erro no servidor!")
    }
    return data
}


export function getRoleFromToken() {
    const token = localStorage.getItem("token");

    if(!token){
        return null
    }

    try{
        const payload = JSON.parse(atob(token.split('.')[1]))
        return payload.role
    } catch(e){
        return null
    }
}


export function mostrarSecao(id){
    document.querySelectorAll(".secao").forEach(secao => secao.style.display="none")
    document.getElementById(id).style.display="block"
}


export async function isAdmin() {
    return getRoleFromToken() === "ROLE_ADMIN"
}

export async function isUser() {
    return getRoleFromToken() === "ROLE_USER"
}

export async function listarUser() {
    if(!isAdmin()){
        alert("Você não tem permissão!")
        return
    }

    try{
        const dados = await handleResponse(await fetch(endpoints.consultarUsers, {
            method:"GET",
            headers:{"Authorization":`Bearer ${token}`,
                    "Content-Type":"application/json"}
        }))

    if(!dados || dados.length == null){
        document.getElementById("msgListarUser").textContent="Nenhum usuário cadastrado!"
        return
    }

    const dadosTabela = document.getElementById("dadosTabela")
    dadosTabela.innerHTML=""

    for(let i = 0; i < dados.length; i++){
        const usuarioAtual = dados[i]

        const linha = document.createElement("tr")

        const colunaId = document.createElement("td")
        colunaId.textContent = usuarioAtual.id_usuario
        
        const colunaMatricula = document.createElement("td")
        colunaMatricula.textContent = usuarioAtual.matricula

        const colunaNome = document.createElement("td")
        colunaNome.textContent = usuarioAtual.nome

        const colunaEmail = document.createElement("td")
        colunaEmail.textContent = usuarioAtual.email

        const colunaRole = document.createElement("td")
        colunaRole.textContent = usuarioAtual.role

        const colunaStatusUser = document.createElement("td")
        colunaStatusUser.textContent = usuarioAtual.statusUsuario
        linha.appendChild(colunaId)
        linha.appendChild(colunaMatricula)
        linha.appendChild(colunaNome)
        linha.appendChild(colunaEmail)
        linha.appendChild(colunaRole)
        linha.appendChild(colunaStatusUser)

        dadosTabela.appendChild(linha)
    }
} catch(erro){
        alert(erro.message)
    }
}

function formatarData(dataISO){
    if(!dataISO) return "Ainda não realizado!"
    const data = new Date(dataISO)
     return data.toLocaleString("pt-BR",{
        dateStyle: "short",
        timeStyle:"medium"
     })
}

export async function listarCheckinsTodos() {
    if(!isAdmin){
        alert("Você não tem permissão!")
        return
    }
    const msg = document.getElementById("msgListarCheckinTodos")
    try{
        const dados = await handleResponse(await fetch(endpoints.listarCheckinsTodos,{
            method:"GET",
            headers:{"Authorization":`Bearer ${token}`}
        }))
        if(!dados || dados.length === 0){
            msg.textContent="Nenhum checkin cadastrado!"
            msg.style.color="red"
        }
        let html = ""
        dados.forEach(checkins => {
            html += `<div class="checkinHistorico">
              <p><strong>Nome: </strong> ${checkins.nome}</p>
              <p><strong>Email: </strong> ${checkins.email}</p>
              <p><strong>Checkin: </strong> ${formatarData(checkins.checkin)}</p>
              <p><strong>Checkout: </strong> ${formatarData(checkins.checkout)}</p>
              <p><strong>ID Checkin: </strong> ${checkins.id_checkin}</p>
              <p><strong>ID Usuário: </strong> ${checkins.id_user}</p>
              <br></br>
            </div>`
        })
        msg.innerHTML=html
    } catch(erro){
        alert(erro.message)
    }
}

export async function listarCheckins() {
    if(!isUser){
        alert("Você não tem permissão!")
        return
    }
    const msg = document.getElementById("msgListarCheckin")
    try{
        const dados = await handleResponse(await fetch(endpoints.listarCheckins, {
            method:"GET",
            headers:{"Authorization":`Bearer ${token}`}
        }))

        if(!dados || dados.length === 0){
            msg.textContent="Nenhum checkin cadastrado!"
        }
        let html =""
        dados.forEach(checkins =>{
            html +=
            `<div class="checkinHistorico">
                <p><strong>Nome: </strong> ${checkins.nome}</p>
                <p><strong>Email: </strong> ${checkins.email}</p>
                <p><strong>Checkin: </strong> ${formatarData(checkins.checkin)}</p>
                <p><strong>Checkout: </strong> ${formatarData(checkins.checkout)}</p>
                <p><strong>ID Checkin: </strong> ${checkins.id_checkin}</p>
                <p><strong>ID Usuário: </strong> ${checkins.id_user}</p>
                <br><br>
            </div>`
        })
        msg.innerHTML=html
    } catch(erro){
        alert(erro.message)
    }
}

export async function desativarUser() {
    try{
        const senha = document.getElementById("confirmarSenha").value;
        const id = Number(document.getElementById("idDesativarAtivar").value);
        const msg = document.getElementById("msgDesativar")

        if(!senha || !id){
            msg.textContent="Preencha todos os campos para desativar o user!"
            msg.style.color="red"
            return
        }

        const dados = await handleResponse(await fetch(endpoints.desativarUsers(id), {
            method: "PATCH",
            headers: {"Authorization":`Bearer ${token}`,
                     "Content-Type":"application/json"},
            body: JSON.stringify({senha: senha})
        }))

        msg.textContent=dados.mensagem||"Usuário desativado com sucesso!"
        msg.style.color="green"
    
        return dados

    } catch(erro){
        alert(erro.message)

    }
}

export async function ativarUser() {
    try{
        const senha = document.getElementById("confirmarSenha").value
        const id = Number(document.getElementById("idDesativarAtivar").value)
        const msg = document.getElementById("msgDesativar")

        if(!senha || !id){
            msg.textContent="Preencha todos os campos para ativar o user!"
            msg.style.color="red"

            return
        }

        const dados = await handleResponse(await fetch(endpoints.ativarUsers(id), {
            method:"PATCH",
            headers:{"Authorization":`Bearer ${token}`,
                    "Content-Type":"application/json"},
            body:JSON.stringify({senha: senha})        
        }))

        msg.textContent=dados.mensagem||"Usuário ativado com sucesso!"
        msg.style.color="green"

        return dados
    } catch (erro){
        alert(erro.message)
    }
}

export async function deletarUser() {
    if(!isAdmin()){
        alert("Você não tem permissão!")
        return
    }

    try{
    const senha = document.getElementById("confirmarSenha2").value
    const id = Number(document.getElementById("idApagar").value)
    const msg = document.getElementById("msgDeletar")

    if(!senha || !id){
        msg.textContent="Preencha todos os campos!"
        msg.style.color="red"
        return
    }

    const dados = await handleResponse(await fetch(endpoints.deletarUsers(id),{
        method:"DELETE",
        headers:{"Authorization":`Bearer ${token}`,
                "Content-Type":"application/json"},
        body:JSON.stringify({senha: senha})
    } ))

    msg.textContent=dados.mensagem||"Usuário apagado com sucesso!"
    msg.style.color="green"
    return dados
}catch(erro){
    alert(erro.message)
}
}

export async function criarAdmin() {
    if(!isAdmin()){
        alert("Você não tem permissão!")
        return
    }

    try{
    const email = document.getElementById("email").value
    const nome = document.getElementById("nome").value
    const senha = document.getElementById("senhaAdmin").value
    const msg = document.getElementById("msgCriarAdmin")

    if(!email || !nome || !senha){
        msg.textContent="Preecha todos os campos!"
        msg.style.color="red"
    }

    const dados = await handleResponse(await fetch(endpoints.criarAdmin, {
        method:"POST",
        headers:{"Authorization":`Bearer ${token}`,
                "Content-type":"application/json"},
        body:JSON.stringify({nome:nome, email: email, senha:senha}),
    }))

    msg.textContent=dados.mensagem||"Novo admin criado com sucesso!"
    msg.style.color="green"
    return dados
} catch(erro){
    alert(erro.message)
}
}

export async function minhaConta() {
    const msg = document.getElementById("msgMinhaConta")

    try{
        const dados = await handleResponse(await fetch(endpoints.minhaConta, {
            method:"GET",
            headers:{"Authorization":`Bearer ${token}`}
        }))

        
        let infos = 
            `<div id="minhasInfos">
                <p> <strong>ID: </strong>${dados.id_usuario} </p>
                <p> <strong>Matrícula: </strong>${dados.matricula} </p>
                <p> <strong>Nome: </strong>${dados.nome} </p>
                <p> <strong>Email: </strong>${dados.email} </p>
                <p> <strong>Status: </strong>${dados.statusUsuario} </p>
            </div>`
        msg.innerHTML=infos
} catch(erro){
    alert(erro.message)
}
}

export async function atualizarUser() {
    const email = document.getElementById("emailAtualizar").value
    const nome = document.getElementById("nomeAtualizar").value
    const msg = document.getElementById("msgAlerta")

    if (!email && !nome) {
        msg.textContent = "Preencha ao menos um campo!"
        msg.style.color = "red"
        return
    }

    try {
        const dados = await handleResponse(await fetch(endpoints.atualizarAdmin, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nome: nome, emailAlterado: email })
        }))

        msg.textContent = dados.mensagem||"Usuário atualizado com sucesso!"
        msg.style.color = "green"

        if (dados.emailAlterado) {
            setTimeout(() => {
                window.location.href = "../login/login.html";
            }, 1500)
        }

        return dados

    } catch (erro) {
        alert(erro.message)
    }
}



export async function fazerCheckin() {
    const msg = document.getElementById("msgCheckin") 
    try{
        const dados = await handleResponse(await fetch(endpoints.fazerCheckin, {
            method:"POST",
            headers:{"Authorization":`Bearer ${token}`,
                    "Content-Type":"application/json"}     
        }))

        msg.textContent=dados.mensagem||"Checkin feito com sucesso! Não se esqueça do Checkout!"
        msg.style.color="green"
        return dados
    } catch(erro){
        msg.textContent=erro.message
        msg.style.color="red"
    }
} 

export async function fazerCheckout() {
    if(!isUser()){
        alert("Você não tem permissão")
        return
    }

    const msg = document.getElementById("msgCheckin")
    try{
        const dados = await handleResponse(await fetch(endpoints.fazerCheckout, {
            method:"POST",
            headers: {"Authorization": `Bearer ${token}`,
            "Content-Type":"application/json"}
        }))

        msg.textContent= dados.mensagem||"Checkout feito com sucesso!"
        msg.style.color="green"
        return dados
    } catch(erro){
        msg.textContent=erro.message
        msg.style.color="red"
    }
}