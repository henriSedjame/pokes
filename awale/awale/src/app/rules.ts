
export class Rule {
  constructor(
    public title: String,
    public content: String[]
  ) {}
}

export const RULES = [
  new Rule("But du jeu", ["S’emparer d’un maximum de graines. Le joueur qui a le plus de graines a la fin de la partie l’emporte"]),
  new Rule("Nombre de joueurs", ["2"]),
  new Rule("Préparation", ["Le plateau est placé horizontalement entre les deux joueurs. Chaque joueur place 4 graines dans chacun des 6 trous devant lui."]),
  new Rule("Déroulement de la partie",
    [
      "La partie se joue sur le plateau de 2×6 trous.",
      "Le sens de la partie est antihoraire",
      "Le 1er joueur prend toutes les graines de l’un des 6 trous se trouvant de son côté et en dépose une dans chaque trou suivant celui qu’il a vidé.",
      "si la dernière graine est déposée dans un trou de l’adversaire comportant déjà 1 ou 2 graines, le joueur capture les 2 ou 3 graines résultantes. Les graines capturées sont alors sorties du jeu (grenier) et le trou est laissé vide",
      "Lorsqu’un joueur s’empare de 2 ou 3 graines, si la case précédente contient également 2 ou 3 graines, elles sont capturées aussi et ainsi de suite",
      "Si le nombre de graines prises dans un trou de départ est supérieur à 11, cela fait que l’on va boucler un tour, auquel cas, à chaque passage, la case de départ est sautée et donc toujours laissée vide",
      "Le jeu se termine lorsqu’un joueur n’a plus de graines dans son camp (et ne peut donc plus jouer). L’adversaire capture alors les graines restantes"
    ]),

]
