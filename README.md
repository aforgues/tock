# Backlog

###### En cours
* Gestion des cartes (distribution, mélange, tour de distribution)
  * ~~Mélange des cartes lors de la création d'une partie~~
  * ~~Distribution de 5 cartes à tous les joueurs au 1er tour~~
  * Passage au 2e puis 3e tour, puis distribution de 4 cartes à chaque tour
  * Passage automatique au joueur suivant si le joueur suivant n'a plus de carte en main
  * Gestion des règles pour passer :
    * Impossible si le joueur a des cartes qu'il peut jouer
    * Jet d'1 seul carte (à choisir) si bloqué par un pion qui est pieux (stake pawn)
  * Choix du joueur qui distribue puis changement du distributeur après chaque tour complet (3 distributions)

###### A faire
* Gérer le status du jeu, avec le joueur à qui c'est le tour de jouer
  * Compter et afficher le round
  * Gérer tous les messages d'erreur en français pour le front
* interface HTML : 
  * Conserver les choix de l'utilisateur en cas d'oubli (choix du pion ou de la carte) sauf si mouvement interdit
  * Affichage de la liste des cartes en Image au lieu d'une simple dropdown
  * Affichage des déplacements possible après selection du pion et de la carte à jouer
  * lancement de l'action de déplacement
* Gérer le déplacement avec un 7 qu'on splitte
* Gérer le fait de pouvoir jouer avec les pions du joueur de son équipe si on a terminé
* Gestion de plusieurs parties
  * Ecran de listing des parties en cours
  * permettre de créer une partie
  * permettre de joindre une partie
* Frontend :
  * Migrer vers Angular ou VueJs afin d'avoir un rafraichissement plus fluide
* Animations
  * Changer l'image centrale de Patachou pour :
    * Pointer du doigt le joueur à qui c'est le tour de jouer
    * Félicite quand on sort et qu'on rentre dans la maison
    * Il est triste quand un joueur jette ses cartes (pendant 2 secondes puis pointe vers le joueur suivant)