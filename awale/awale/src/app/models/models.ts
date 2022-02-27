export class Result {
  constructor(
    public end: boolean,
    public winner: Winner | null
  ) {
  }
}

export enum Winner {
  USER = 'USER',
  PC = 'PC',
  EQUALITY = 'EQUALITY'
}


export class HoleClickEvent {
  constructor(
    public value: number,
    public user: boolean
  ) {}
}


export class Loading {
  constructor(
    public enable: boolean,
    public value: number
  ) {}
}
