
export const url = "https://academia-2rp0.onrender.com/"

export const endpoints = {
    consultarUsers: url + "usuario/users",
    desativarUsers: (id) => `${url}usuario/${id}/desativar`,
    ativarUsers:(id) => `${url}usuario/${id}/ativar`,
    deletarUsers:(id) =>`${url}usuario/${id}`,
    criarAdmin: url + "auth/admin",
    atualizarAdmin: url + "usuario/me/atualizar",
    fazerCheckin: url + "usuario/me/checkin",
    fazerCheckout: url + "usuario/me/checkout",
    listarCheckins: url + "usuario/me/listarCheckins",
    listarCheckinsTodos: url + "usuario/me/listarCheckinsTodos",
    minhaConta: url + "usuario/me/minhaConta"
}