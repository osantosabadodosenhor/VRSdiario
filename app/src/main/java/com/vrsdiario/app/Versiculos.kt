package com.vrsdiario.app

data class Versiculo(
    val referencia: String,
    val texto: String
)

object Versiculos {

    val lista = listOf(

        Versiculo(
            "Salmos 23:1",
            "O Senhor é o meu pastor; nada me faltará."
        ),

        Versiculo(
            "Filipenses 4:13",
            "Posso todas as coisas naquele que me fortalece."
        ),

        Versiculo(
            "Provérbios 3:5",
            "Confia no Senhor de todo o teu coração."
        ),

        Versiculo(
            "Mateus 6:33",
            "Mas buscai primeiro o Reino de Deus, e a sua justiça."
        ),

        Versiculo(
            "Isaías 41:10",
            "Não temas, porque eu sou contigo; não te assombres, porque eu sou teu Deus."
        ),

        Versiculo(
            "Salmos 37:5",
            "Entrega o teu caminho ao Senhor; confia nele, e ele tudo fará."
        ),

        Versiculo(
            "João 14:27",
            "Deixo-vos a paz, a minha paz vos dou."
        )

    )
}
