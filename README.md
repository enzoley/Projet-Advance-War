### Projet Advance Wars



## Binôme

LEROYER Enzo & MORAND Arthur (L2 IE - TP4)


## Description du jeu

Il s’agit d’un jeu de stratégie militaire où l’objectif de chaque joueur est de capturer le Quartier Général (QG) adverse. Les deux joueurs, rouge et bleu joueront à tour de rôle sur le même ordinateur et c'est le joueur rouge qui commence. Les joueurs contrôlent chacun leur armée qui sont composées d’unités militaires qui occupent chacune une case (il ne peut jamais y avoir deux unités par case). Une unité peut se déplacer sur les différents types de terrain, attaquer les unités ennemies ou capturer des propriétés

# Plateau de jeu
Le plateau de jeu est une grille rectangulaire dans laquelle chaque case a un type de terrain. Les types de terrain possibles sont les suivants : Plaine, Forêt, Montagne, Eau, auxquelles s’ajoutent les propriétés : QG, Ville et Usine.

# Terrain
- Normal : Plaine, Forêt, Montagne, Eau
- Propriétés : QG, Ville et Usine

# Unités
- Infanterie : Unité la plus fragile du jeu, mais qui peut capturer les propriétés adverses. Déplacements plutôt lents mais compatibles avec des terrains accidentés. Peu efficace en combat.
- Bazooka : Similaire à l’infanterie mais équipé d’un bazooka, rendant l’unité très lente mais efficace contre les unités blindées.
- Tank : L’unité offensive par excellence, rapide, robuste et puissante. Fortement ralenti par les ter- rains accidenté, il est également vulnérable aux attaques à distance et aux unités aériennes.
- DCA : Abréviation de "Défense contre l’aviation", cette unité est particulièrement efficace contre les avions et hélicoptères, mais peu efficace contre les blindés.
- Hélicoptère : Unité aérienne la plus basique, son déplacement n’est pas affecté par le terrain. Efficace contre les blindés.
- Bombardier : Avion qui peut rapidement réduire les unités terrestres en miettes. Ne peut pas attaquer les autres avions.

# Déplacements
- deplacer le curseur : utiliser les flêches directionnelles.
- sélectioner une unité : placer le curseur sur la case où se trouve votre unité et appuyer sur Entrée.
- sélectioner un chemin pour une unité : sélectioner une unité et puis dessine avec le curseur le chemin souhaité pour l’unité puis appuyer sur Entrée.
- desélectioner un chemin pour une unité : Appuyer sur Echap.
- confirmer le déplacement d'une unité : sélectioner un chemin pour une unité puis choisiser un action a l'aide de la popup.

# Actions
- attendre : deplace l'unité et ne fait rien
- attaquer : deplace l'unité et attaque. Si une seul unité adverse peut être attaquer, l'action se réalise toute seule sinon il faut choisir quelle unité attaquer. Pour changer l'unité ennemie appuyer sur Entrée ou sur les flêches directionnelles. Pour attaquer l'unité ennemie selectionée appuyer sur 'a'.
- capturer : deplace l'unité (infanteries ou bazookas) et reduit les PV de la propriétée
- produir unité : selectioner une usine vous appartenant et sans unité dessus puis choisiser une unité a l'aide de la popup.

# Joueur
- changement de joueur : appuyer 't'
- crédits : chaque joueur commencent avec 0 crédits, et toute propriété possédée par un joueur lui rapporte 1000 crédits au début de son tour. Un joueur peut utiliser ses usine pour produire des unités, à condition d’avoir les crédits nécessaires pour produire l’unité ; dans ce cas, le prix de l’unité est soustrait à ses crédits.

# Popup
Selectioner le choix qui vous interesse avec les flêches directionnelles puis appuyer sur Entrée, pour annuler appuyer sur Echap.


## Commandes particulières
- 't' : changement de joueur
- 'a' : attaquer l'unité ennemie selectionée dans le cas ou il y a plusieurs ennemies attaquables
- 'u' : placer le curseur sur une case contenant une unité que vous pouvez deplacer ou sur une usine qui peut encore produir des unites
- 'j' : changer mode auto ou manuel de fin de tour pour le joueur actuel


## Améliorations

- fin de tour automatique
- couverture de terrain
- affichage des déplacements et attaques possibles d’une unité

# Unités
- touche 'u' pour lister les unités encore utilisables + liste les usines encore utilisables
- armes multiples

# Déplacements
- conserve en mémoire les cases par lesquelles le curseur est passé jusqu’à la validation finale du trajet. Cette implémentation permet au joueur de revenir en arrière pendant le choix de déplacement

# Affichage
- Nom et nombre de crédits du joueur courant
- PV de l'unité et de la propriété pour la case où se trouve le curseur
- Etat de la partie indiqué