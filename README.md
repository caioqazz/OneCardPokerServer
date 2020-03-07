# OneCardPoker

## Rodando o projeto

1. Importe e inicie este projeto
2. Importe e inicie o projeto [Cliente](https://github.com/caioqazz/OneCardPokerClient).


## Jogo

O jogo escolhido foi o jogo “poker de uma carta” para o desenvolvimento do trabalho. No jogo são distribuídos duas cartas para cada jogador, onde o jogador selecionará
somente uma, no final da rodada, o jogador que tiver a maior carta dentre as selecionadas pelos jogadores vencerá a rodada. Neste jogo escolhido é usado o baralho inglês. A cada início de uma rodada haverá apostas, cada jogador começa com 100 moedas, o jogador que chegar a 0 moedas perde.

## Desenvolvimento
A comunicação entre processos foi implementada utilizando o RMI com stubs dinâmicos.

### Conexão

Após iniciar o jogo, o cliente tem a opção de conectar ao servidor clicando em um botão, caso a conexão seja estabelecida um novo painel será aberto, caso não seja estabelecida, o cliente recebe uma mensagem e continua no mesmo painel.
 
![ Painel de Conexão](https://raw.githubusercontent.com/caioqazz/OneCardPokerClient/master/imgs/img1.jpg)

Figura 1: Painel de Conexão.

### Nome

Logo após a conexão ser estabelecida, o painel que irá se abrir será o painel de nome, o usuário deve colocar um nome com no mínimo quatro letras, assim um novo painel irá se abrir.
 
 

![ Painel de Nome](https://raw.githubusercontent.com/caioqazz/OneCardPokerClient/master/imgs/img2.jpg)

Figura 2: Painel de Nome.

### Oponentes

Após o nome ser aceito, o painel que irá se abrir é o painel de oponentes, esse painel apresenta três botões fundamentais, apresentados abaixo:
 

* Atualizar Lista de Oponentes: Este botão tem a função de solicitar todos os clientes logados no servidor juntamente com seus respectivos status.
* Ajuda: Esse botão apresenta para o cliente o painel de ajuda, nele contem todas as informações relevantes ao jogo.
 
 

 ![ Painel de Ajuda](https://raw.githubusercontent.com/caioqazz/OneCardPokerClient/master/imgs/img3.jpg)

Figura 3: Painel de Ajuda.

* Solicitar partida: Esse botão é responsável por solicitar uma partida diretamente com o oponente. É necessário que o cliente que irá solicitar o jogo, selecione um oponente na tabela de oponentes, vale ressaltar, que ele não poderá enviar uma mensagem pra ele próprio, mesmo o nome aparecendo nessa tabela. Após a seleção do oponente, o cliente deve clicar no botão para enviar a solicitação. Caso o oponente aceite, a partida se iniciará.
 
 

 ![ Painel de Oponentes](https://raw.githubusercontent.com/caioqazz/OneCardPokerClient/master/imgs/img4.jpg)
 
Figura 4: Painel de Oponentes.

### Jogo iniciado
O jogo é iniciado, então a rodada começa. No inicio de cada rodada os jogadores devem entrar em um comum acordo para realização das apostas, quando decidirem quantas moedas a rodada valerá, o jogador poderá selecionar a carta que deseja jogar.
 
 ![ Painel de Aposta](https://raw.githubusercontent.com/caioqazz/OneCardPokerClient/master/imgs/img5.jpg)
 
Figura 5: Painel de Aposta.
 
 

 ![ Painel de Jogada](https://raw.githubusercontent.com/caioqazz/OneCardPokerClient/master/imgs/img6.jpg)
 
Figura 6: Painel de Jogada.

### Critérios de fim de jogo
O jogo é finalizado quando um jogador chega a 0 moedas. Assim o oponente do jogador que chegou a 0 moedas é considerado vencedor.
 
 
 ![ Painel de Fim de Jogo](https://raw.githubusercontent.com/caioqazz/OneCardPokerClient/master/imgs/img7.jpg)

Figura 7: Painel de Fim de Jogo.
